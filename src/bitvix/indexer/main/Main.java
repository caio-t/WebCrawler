package bitvix.indexer.main;

import java.util.Iterator;
import java.util.LinkedList;

import bitvix.indexer.model.Config;
import bitvix.indexer.crawler.Controller;
import bitvix.indexer.crawler.Crawler;
import bitvix.indexer.dao.ContentDao;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
	
	public static void main(String[] args) {
       
		 final Timer t = new Timer();
	        t.schedule(new TimerTask() {
	            @Override
	            public void run() {
	                
	                try {
						new Controller();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	       	     }

	        }, 1000, 10000);
    }
	
	
	
}
