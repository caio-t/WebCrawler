package bitvix.indexer.crawler;

import java.util.Iterator;
import java.util.LinkedList;

import bitvix.indexer.dao.ContentDao;
import bitvix.indexer.model.CrawlerData;
import bitvix.indexer.model.Customer;
import bitvix.indexer.model.Site;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	public Controller() throws Exception
	{
		this.executeCrawler();
	}
	
	public void executeCrawler() throws Exception
	{
		 	String crawlStorageFolder = "/data/crawl/root";
	        
	        ContentDao contentDao = new ContentDao();
	        
	        contentDao.fetchCustomer();
	        
	        CrawlerData crawlerData = new CrawlerData();
	        
	        LinkedList<Customer> customers = contentDao.getCustomers();
	        
	        Site site = null;
	        
	        Iterator<Customer> iterator = customers.iterator();
	        
	        while(iterator.hasNext())
	        {
	        	Customer customer = new Customer();
	        	
	        	customer = iterator.next();
	        	
	        	crawlerData.setCustomer(customer);
	        	
	        	System.out.println("Search: " + customer.getName());
	        	System.out.println("Keywords: " + customer.getKeyword());
		        	
	        	LinkedList<Site> sites = customer.getSites();
	        	
	        	Iterator<Site> iterator2 = sites.iterator();
	        	
	        	contentDao.setCurrentCustomer(customer);
	        	
	        	while(iterator2.hasNext())
		        {
	        		CrawlConfig configCrawl;
		 	        
	        		int i = 1;
		 	       
		 	        PageFetcher pageFetcher;
		 	        
		 	        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 	        
		 	        RobotstxtServer robotstxtServer;
		 	        
		 	        CrawlController controller;
		 	        
		 	        site = iterator2.next();
		 	        
		 	        crawlerData.setSite(site);
		 	        
		 	        configCrawl = new CrawlConfig();
		        	
		        	configCrawl.setPolitenessDelay(site.getPolitenessDelay());
		        	
		        	configCrawl.setMaxPagesToFetch(site.getMaxPagesToFetch());
		        	
		        	configCrawl.setCrawlStorageFolder(crawlStorageFolder + "/crawler"+i);
		        	
		        	configCrawl.setMaxDepthOfCrawling(site.getMaxDepthOfCrawling());
		        	
		        	pageFetcher = new PageFetcher(configCrawl);
		        	
		        	robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		        	
		        	controller = new CrawlController(configCrawl, pageFetcher, robotstxtServer);
		        	
		       
		            controller.setCustomData(crawlerData);
		            
		            controller.addSeed(site.getUrl());
		            
		            controller.start(Crawler.class, 5);
	        		
	        		System.out.println("Site: " + site.getName() + " ==> " + site.getUrl());
	        		
		        }
	        	
	        	contentDao.updateCountResults(customer);
	        	
	        }
	       
	      
	}
}
