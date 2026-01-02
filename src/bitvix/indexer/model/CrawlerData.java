package bitvix.indexer.model;

import java.util.ArrayList;

public class CrawlerData {
	private Customer customer;
	private Site site;
	private ArrayList<String> blackList;
	private ArrayList<String> siteSeed;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public ArrayList<String> getBlackList() {
		return blackList;
	}
	public void setBlackList(ArrayList<String> blackList) {
		this.blackList = blackList;
	}
	public ArrayList<String> getSiteSeed() {
		return siteSeed;
	}
	public void setSiteSeed(ArrayList<String> siteSeed) {
		this.siteSeed = siteSeed;
	}
	
}
