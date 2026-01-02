# Copilot Instructions for Indexer Crawler

## Project Overview
This is a **Java web crawler** that periodically fetches and indexes web content matching customer keywords. It uses `crawler4j` for crawling machinery and plain JDBC for MySQL persistence.

**Key architecture**: Timer-based scheduler → Controller (orchestrates per-customer crawls) → Crawler instances (page parsing + keyword matching) → ContentDao (direct JDBC writes).

## Build & Run

### Prerequisites
- Java compiler (`javac`)
- MySQL 8.0 instance running locally (or via `docker-compose up -d`)
- All JARs in `lib/` (crawler4j, httpclient, mysql-connector, etc.)

### Local Build & Run
```bash
# Compile to bin/ folder with classpath including lib/ and src/
javac -d bin -cp "lib/*;src" src/bitvix/indexer/**/**/*.java

# Run the main entry point (starts Timer-based scheduler)
java -cp "bin;lib/*" bitvix.indexer.main.Main
```

### Docker
```bash
# Start MySQL container with schema auto-migration
docker-compose up -d

# Check MySQL health
docker-compose logs -f mysql
```

**Database location**: `localhost:3306/webcrawler` (user: `root`, empty password)

## Critical Conventions & Patterns

### 1. **Database Configuration**
- Credentials hardcoded in [src/bitvix/indexer/util/Util.java](src/bitvix/indexer/util/Util.java): `URL`, `USERNAME`, `PASSWORD` constants
- **If you modify these**, update `docker-compose.yml` environment vars to match
- Connection pattern: `ContentDao.connect()` opens per-call, `close()` closes (no pooling)

### 2. **Error Handling Philosophy**
- DAO methods call `printError(...); System.exit(0)` on serious DB errors
- **Changing this changes process lifecycle** — consider timing implications for scheduled crawls
- No try-catch recovery; failures are fatal

### 3. **SQL Injection Risk**
- Many queries use **string concatenation** (e.g., `"WHERE cus_chp_customer = " + customer.getId()`)
- When modifying DAO SQL, prefer `PreparedStatement` for user-input values
- Example in [ContentDao.java](src/bitvix/indexer/dao/ContentDao.java): `rs = stm.executeQuery(sql)` — vulnerable if SQL is user-built

### 4. **Naming Conventions (Hungarian-inspired)**
- Table/column names follow pattern: `tbl/entt + var/int/chp_ + lowercase_name`
- Examples: `cus_chp_customer` (customer ID), `con_var_keyword` (content keyword text)
- **Maintain this** when creating new tables or columns

## Data Flow & Key Files

### Timer & Scheduling
- **[Main.java](src/bitvix/indexer/main/Main.java)**: Runs `Controller()` every 10 seconds (initial delay 1 sec)
- Decision: Timer vs. external scheduler (Quartz, Spring) not addressed; note hard-coded intervals

### Orchestration
- **[Controller.java](src/bitvix/indexer/crawler/Controller.java)**: 
  - Fetches all customers from DB
  - For each customer, fetches related sites
  - Creates `CrawlConfig` per site (max depth, page fetch limit, politeness delay)
  - Starts `CrawlController` with `Crawler` instances

### Crawling & Keyword Matching
- **[Crawler.java](src/bitvix/indexer/crawler/Crawler.java)** (extends `WebCrawler`):
  - `shouldVisit()`: Filters by domain + blacklist + file extensions (blocks .bmp, .gif, .jpg, .png, .css, .js, .rss, .xml)
  - `visit()`: Extracts text/title, checks if any customer keyword matches
  - Calls `ContentDao.insert()` on match
  - Uses `CrawlerData` thread-local holder for per-crawler context

### Data Access
- **[ContentDao.java](src/bitvix/indexer/dao/ContentDao.java)**:
  - `fetchCustomer()`: Loads all customers + related sites into memory
  - `insert()`: Inserts matched content into `entt_content` table
  - No transaction support; auto-commit mode

## Database Schema
Tables in `migration/001_init.sql`:
- `entt_customer`: customers with keywords, situation flag, result count
- `entt_site`: crawl targets (URL, domain, depth, page limits, politeness delay)
- `tbla_customersite`: junction table (customer ↔ site many-to-many)
- `entt_content`: matched content rows (customer, keyword, text, URL, extracted metadata)
- `entt_blacklist`: per-customer blacklisted URLs
- `entt_siteseed`: seed URLs for crawlers
- `entt_contentreaded`: tracks already-fetched URLs per customer (prevents re-indexing)
- `tbla_config`: runtime config (e.g., `cof_int_currentcustomer` for state)

## Known Gaps & Improvement Opportunities
- **No build tool**: Use `pom.xml` or `gradle.build` to manage `lib/` dependencies instead of manual JAR management
- **Connection pooling**: Currently opens/closes per DAO call; consider HikariCP or C3P0
- **Prepared statements**: Convert string SQL concatenations to prepared statements
- **Threading**: `Crawler` threads may not be explicitly joined; potential resource leaks or incomplete indexing

## Common Tasks

### Add a new customer-specific field
1. Add column to `entt_customer` in `migration/001_init.sql` (use naming convention)
2. Update `Customer.java` model with getter/setter
3. Update `ContentDao.fetchCustomer()` to populate new field from ResultSet
4. Reference in [Controller.java](src/bitvix/indexer/crawler/Controller.java) or [Crawler.java](src/bitvix/indexer/crawler/Crawler.java) as needed

### Change crawl frequency or max pages
- Edit Timer interval in [Main.java](src/bitvix/indexer/main/Main.java) line ~25 (currently 10 sec)
- Edit max pages per site: `CrawlConfig.setMaxPagesToFetch()` in [Controller.java](src/bitvix/indexer/crawler/Controller.java)

### Debug a crawl not indexing content
1. Check `shouldVisit()` logic in [Crawler.java](src/bitvix/indexer/crawler/Crawler.java): domain matching, blacklist, extensions
2. Check keyword matching in `visit()`: case sensitivity, whitespace
3. Verify `ContentDao.insert()` is being called: add debug print before `conn.createStatement()`
4. Check DB connection in [Util.java](src/bitvix/indexer/util/Util.java): port, database name, credentials

## External Dependencies
- **crawler4j** (in `lib/`): CrawlController, WebCrawler, Page, WebURL, HtmlParseData
- **MySQL JDBC driver** (in `lib/`): `java.sql.*` classes
- **Apache HttpClient** (in `lib/`): used by crawler4j for HTTP fetch

Ensure `lib/` contains all required JARs before compilation.
