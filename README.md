# Indexer (simple crawler)

Small Java web-crawler using crawler4j and plain JDBC. Designed to run periodically, read customer/site configuration from the database, crawl site seeds, search for keywords and persist matched content.

Quickstart (Windows)

1. Ensure `lib/` contains required JARs (crawler4j, httpclient, MySQL connector, etc.).
2. Edit DB connection in `src/bitvix/indexer/util/Util.java` to match your MySQL instance.
3. Compile:

```bash
javac -d bin -cp "lib/*;src" src/bitvix/indexer/**/**/*.java
```

4. Run:

```bash
java -cp "bin;lib/*" bitvix.indexer.main.Main
```

Architecture & flow

- Entrypoint: `src/bitvix/indexer/main/Main.java` — schedules the crawling job using `java.util.Timer`.
- Orchestration: `src/bitvix/indexer/crawler/Controller.java` — reads customers from DB, builds `CrawlConfig` per `Site`, sets `CrawlerData` and starts crawler threads.
- Crawling: `src/bitvix/indexer/crawler/Crawler.java` — implements `shouldVisit` and `visit`. Extracts text/title and calls `ContentDao.insert(...)` when customer keywords are found.
- Data access: `src/bitvix/indexer/dao/ContentDao.java` and `src/bitvix/indexer/dao/ConnFactory.java` — use direct JDBC (string SQL, `Statement`, `PreparedStatement`).

Important repository conventions

- No build tool in repo root — compile/run commands above are how the project is typically built locally.
- DB configuration: `src/bitvix/indexer/util/Util.java` stores `URL`, `USERNAME`, `PASSWORD` constants used by `ConnFactory`.
- Error handling: DAO methods call `printError(...); System.exit(0)` on serious errors — changing this affects process lifecycle.
- SQL patterns: many queries are built via string concatenation (`ContentDao.insert`) — be cautious of SQL injection when modifying.
- Connection pattern: `ContentDao.connect()` opens a connection per DAO call and `close()` closes it — no pooling.

External dependencies

- crawler4j (in `lib/`) — used for crawling machinery (`CrawlController`, `PageFetcher`, `RobotstxtServer`).
- MySQL JDBC driver — required at runtime.

Key files to inspect

- `src/bitvix/indexer/main/Main.java` — scheduler
- `src/bitvix/indexer/crawler/Controller.java` — crawl setup & loop
- `src/bitvix/indexer/crawler/Crawler.java` — page parsing and keyword matching
- `src/bitvix/indexer/dao/ContentDao.java` — DB reads/writes, example SQL
- `src/bitvix/indexer/util/Util.java` — DB credentials

Suggested next steps

- Add a `pom.xml` or Gradle build to simplify dependency management and builds.
- Extract database schema (tables used: `entt_customer`, `entt_site`, `entt_content`, `entt_blacklist`, `entt_siteseed`, `entt_contentreaded`, `tbla_customersite`, `tbla_config`) into a migration SQL file.
- Consider converting raw SQL concatenations to prepared statements where user input is involved.

If you want, I can generate a minimal `pom.xml`, extract schema SQL from `ContentDao`, or convert one DAO method to use fully-parameterized prepared statements — which should I do next?
