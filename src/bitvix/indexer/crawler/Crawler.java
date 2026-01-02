package bitvix.indexer.crawler;


import java.util.regex.Pattern;

import org.apache.http.Header;

import bitvix.indexer.dao.ContentDao;
import bitvix.indexer.model.CrawlerData;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
	private static final Pattern BLOCKED_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png|css|js|rss|xml)$");
	private CrawlerData crawlerData;
	private ContentDao contentDao = new ContentDao();

	/**
	   * You should implement this function to specify whether the given url
	   * should be crawled or not (based on your crawling logic).
	   */
	
		@Override
	  public void onStart() {
	    crawlerData = (CrawlerData) myController.getCustomData();
	  }
	
	  @Override
	  public boolean shouldVisit(Page referringPage, WebURL url) {
	    String href = url.getURL();
	    String domain = url.getDomain();
	    // Ignore the url if it has an extension that matches our defined set of image extensions.
	    if (BLOCKED_EXTENSIONS.matcher(href).matches()) {
	      return false;
	    }
	    
	   
	    if (crawlerData.getCustomer().getBlackList().contains(href)) {
		      return false;
		}
		
	    return crawlerData.getSite().getDomain().contains(domain);
	      
	  }

	  /**
	   * This function is called when a page is fetched and ready to be processed
	   * by your program.
	   */
	  @Override
	  public void visit(Page page) {
	    int docid = page.getWebURL().getDocid();
	  
	    String url = page.getWebURL().getURL();
	    String domain = page.getWebURL().getDomain();
	    String path = page.getWebURL().getPath();
	    String subDomain = page.getWebURL().getSubDomain();
	    String parentUrl = page.getWebURL().getParentUrl();
	    String anchor = page.getWebURL().getAnchor();
	    

	    System.out.println("Docid: " + docid);
	    System.out.println("URL: " + url);
	    
	    System.out.println("Domain:" + domain);
	    System.out.println("Sub-domain: " + subDomain);
	    System.out.println("Path" + path);
	    System.out.println("Parent page: " + parentUrl);
	    System.out.println("Anchor text: " + anchor);
	    

	    if (page.getParseData() instanceof HtmlParseData) {
	      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	      String text = htmlParseData.getText();
	      String title = htmlParseData.getTitle();
	      String titleForRelease;
	      
	      if (anchor != null)
	      {
	    	  titleForRelease = anchor;
	      }
	      else
	      {
	    	  titleForRelease = title;
	      }
	      
	      text = text.replaceAll("\r", ""); 
	      text = text.replaceAll("\t", "");
	      text = text.replaceAll("\n", "");
	      
	      String[] keywords = crawlerData.getCustomer().getKeyword().toLowerCase().split(",");
	      
	      int keywordCount = keywords.length;
	      int i = 0;
	      while(i < keywordCount)
	      {
	    	  
		      if (text.toLowerCase().indexOf(keywords[i].toLowerCase()) != -1)
		      {
		    	  if (!crawlerData.getCustomer().getSiteSeed().contains(url)) 
		    	  {
		    		  if (!crawlerData.getCustomer().getContentReaded().contains(url)) 
		    		  {
		    			  System.out.println("Found "+keywords[i]);
		    	  
		    			  contentDao.insert(crawlerData.getCustomer().getId(), keywords[i], titleForRelease, text, url, domain, subDomain, parentUrl);
		    		  }
		    		  else
		    		  {
		    			  System.out.println("Reading: " + url);
		    		  }
		    	  }
		    	  else
		    	  {
		    		  System.out.println("Anchor: " + url);
		    	  
		    	  }
		      }
	      	  i++;
	      }
	    
		    System.out.println("========================================================");
	
		    Header[] responseHeaders = page.getFetchResponseHeaders();
		    if (responseHeaders != null) {
		    	System.out.println("Response headers:");
		      for (Header header : responseHeaders) {
		    	  System.out.println("\t: " + header.getName() + " - " + header.getValue());
		      }
		    }
	
		    System.out.println("=============");
	  }
	 }
	  
}
