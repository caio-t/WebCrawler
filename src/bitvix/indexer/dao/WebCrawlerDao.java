package bitvix.indexer.dao;

import java.util.LinkedList;

import bitvix.indexer.model.Customer;
import bitvix.indexer.model.Site;

interface WebCrawlerDao {
	
	 void insert(int customerId,String keyword, String anchor,String text, String url, String domain, String subDomain, String parentUrl);
	 void delete();	
	 void fetchCustomer();
	 LinkedList<Site> getSitesCustomer(Customer customer);
	 LinkedList<Customer> getCustomers();
}
