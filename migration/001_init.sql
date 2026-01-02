-- Initial schema for Indexer crawler
-- Tables referenced by the code: entt_customer, entt_site, tbla_customersite,
-- entt_content, entt_blacklist, entt_siteseed, entt_contentreaded, tbla_config

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tbla_config;
DROP TABLE IF EXISTS entt_contentreaded;
DROP TABLE IF EXISTS entt_siteseed;
DROP TABLE IF EXISTS entt_blacklist;
DROP TABLE IF EXISTS entt_content;
DROP TABLE IF EXISTS tbla_customersite;
DROP TABLE IF EXISTS entt_site;
DROP TABLE IF EXISTS entt_customer;

CREATE TABLE entt_customer (
  cus_chp_customer INT AUTO_INCREMENT PRIMARY KEY,
  cus_var_name VARCHAR(255) NOT NULL,
  cus_var_keyword TEXT,
  cus_int_situation TINYINT NOT NULL DEFAULT 1,
  cus_int_numberofresults INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entt_site (
  sit_chp_site INT AUTO_INCREMENT PRIMARY KEY,
  sit_var_name VARCHAR(255) NOT NULL,
  sit_var_url VARCHAR(2083) NOT NULL,
  sit_var_domain VARCHAR(255),
  sit_int_maxdepthofcrawling INT DEFAULT 2,
  sit_int_maxpagestofetch INT DEFAULT 1000,
  sit_int_politenessdelay INT DEFAULT 200
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tbla_customersite (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cus_chp_customer INT NOT NULL,
  sit_chp_site INT NOT NULL,
  UNIQUE KEY ux_customer_site (cus_chp_customer, sit_chp_site),
  FOREIGN KEY (cus_chp_customer) REFERENCES entt_customer(cus_chp_customer) ON DELETE CASCADE,
  FOREIGN KEY (sit_chp_site) REFERENCES entt_site(sit_chp_site) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entt_content (
  con_chp_content INT AUTO_INCREMENT PRIMARY KEY,
  cus_chp_customer INT NOT NULL,
  con_var_keyword VARCHAR(255),
  con_var_anchor VARCHAR(255),
  con_var_text LONGTEXT,
  con_var_url VARCHAR(2083),
  con_var_domain VARCHAR(255),
  con_var_subdomain VARCHAR(255),
  con_var_parenturl VARCHAR(2083),
  con_var_dateregister DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (cus_chp_customer) REFERENCES entt_customer(cus_chp_customer) ON DELETE CASCADE,
  INDEX idx_customer_keyword (cus_chp_customer, con_var_keyword(50)),
  INDEX idx_url (con_var_url(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entt_blacklist (
  bll_chp_blacklist INT AUTO_INCREMENT PRIMARY KEY,
  cus_chp_customer INT NOT NULL,
  bll_var_url VARCHAR(2083) NOT NULL,
  FOREIGN KEY (cus_chp_customer) REFERENCES entt_customer(cus_chp_customer) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entt_siteseed (
  sid_chp_site_seed INT AUTO_INCREMENT PRIMARY KEY,
  sid_var_url VARCHAR(2083) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE entt_contentreaded (
  cor_chp_id INT AUTO_INCREMENT PRIMARY KEY,
  cus_chp_customer INT NOT NULL,
  cor_var_url VARCHAR(2083) NOT NULL,
  FOREIGN KEY (cus_chp_customer) REFERENCES entt_customer(cus_chp_customer) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tbla_config (
  cof_chp_id INT AUTO_INCREMENT PRIMARY KEY,
  cof_int_currentcustomer INT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS=1;

-- Optional: sample data (uncomment to use)
-- INSERT INTO entt_customer (cus_var_name, cus_var_keyword, cus_int_situation) VALUES ('Demo Customer', 'keyword1,keyword2', 1);
