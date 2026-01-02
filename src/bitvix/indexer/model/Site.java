package bitvix.indexer.model;

public class Site {
	public static final int SITE_INDEXER = 4;
	public static final int SITE_NONINDEXER = 3;
	
	private String name;
	private String url;
	private String domain;
	
	private int politenessDelay;
	private int maxPagesToFetch;
	private int maxDepthOfCrawling;
	
	public int getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}
	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}
	public int getMaxPagesToFetch() {
		return maxPagesToFetch;
	}
	public void setMaxPagesToFetch(int maxPagesToFetch) {
		this.maxPagesToFetch = maxPagesToFetch;
	}
	public int getPolitenessDelay() {
		return politenessDelay;
	}
	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
