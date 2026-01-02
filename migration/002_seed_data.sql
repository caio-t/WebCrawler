-- Seed data for Indexer crawler (demo data)
-- Inserts a demo customer, two sites, relations, seeds, blacklist and initial config

SET FOREIGN_KEY_CHECKS=0;

-- Demo customer (explicit id to make later references stable)
INSERT INTO entt_customer (cus_chp_customer, cus_var_name, cus_var_keyword, cus_int_situation, cus_int_numberofresults)
VALUES (1, 'Demo Customer', 'keyword1,keyword2', 1, 0);

-- Demo sites
INSERT INTO entt_site (sit_chp_site, sit_var_name, sit_var_url, sit_var_domain, sit_int_maxdepthofcrawling, sit_int_maxpagestofetch, sit_int_politenessdelay)
VALUES
  (1, 'Globo.com', 'https://globo.com', 'Globo.com', 2, 1000, 200),
  (2, 'Sapo', 'https://sapo.pt', 'Sapo', 3, 500, 300);

-- Link customer to sites
INSERT INTO tbla_customersite (id, cus_chp_customer, sit_chp_site)
VALUES (1, 1, 1), (2, 1, 2);

-- Site seeds (the code reads all rows from entt_siteseed)
INSERT INTO entt_siteseed (sid_chp_site_seed, sid_var_url)
VALUES (1, 'https://globo.com/'), (2, 'https://sapo.pt/');

-- Blacklist entry for demo customer
INSERT INTO entt_blacklist (bll_chp_blacklist, cus_chp_customer, bll_var_url)
VALUES (1, 1, 'https://example.com/ignore');

-- Mark one URL as already read
INSERT INTO entt_contentreaded (cor_chp_id, cus_chp_customer, cor_var_url)
VALUES (1, 1, 'https://example.com/already-read');

-- Initial config pointing to demo customer
INSERT INTO tbla_config (cof_chp_id, cof_int_currentcustomer)
VALUES (1, 1);

SET FOREIGN_KEY_CHECKS=1;

-- NOTE: adjust IDs or remove explicit IDs if your environment relies on auto-increment sequencing.
