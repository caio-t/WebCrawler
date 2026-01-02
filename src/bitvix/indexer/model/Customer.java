package bitvix.indexer.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Customer 
{
	public static final int CUSTOMER_ACTIVE = 1;
	
	private int id;
	private String name;
	private String keyword;
	private LinkedList<Site> sites;
	private ArrayList<String> siteSeed;
	private ArrayList<String> blackList;
	private ArrayList<String> contentReaded;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public LinkedList<Site> getSites() {
		return sites;
	}
	public void addSite(Site site) {
		this.sites.add(site);
	}
	
	public void addSite(LinkedList<Site> sites) {
		this.sites = sites;
	}
	public ArrayList<String> getBlackList() {
		return blackList;
	}
	public void addBlackList(ArrayList<String> blackList) {
		this.blackList = blackList;
	}
	public ArrayList<String> getSiteSeed() {
		return siteSeed;
	}
	public void addSiteSeed(ArrayList<String> arrayList) {
		this.siteSeed = arrayList;
	}
	public ArrayList<String> getContentReaded() {
		return contentReaded;
	}
	public void addContentReaded(ArrayList<String> contentReaded) {
		this.contentReaded = contentReaded;
	}
}
