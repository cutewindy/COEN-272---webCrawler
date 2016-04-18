package webCrawler;

import java.io.File;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 1 Retrieve a web page from a website
 * 2 Collect all the links on that page
 * 3 Collect all the words on that page(not done)
 * 4 Visit the next link
 * 5 Keep track of pages that already visited
 * 6 Put a limit on the number of pages to search so this doesn't run for eternity
 * @author Wendi
 * @return
 */
public class Crawler {
	
	
	private static final int MAX_PAGE_TO_SEARCH = 1;
		
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisisted = new LinkedList<String>();
	
	
	/**
	 * Create CrawlerLeg to crawl the web page and collect all the links on that page
	 * @param url
	 */
	public void run(String url) {
		while (this.pagesVisited.size() < MAX_PAGE_TO_SEARCH) {
			String currURL;
			CrawlerWorker crawlerWorker = new CrawlerWorker();
			if (this.pagesToVisisted.isEmpty()) {
				currURL = url;
				this.pagesVisited.add(url);
			}
			else {
				currURL = this.nextURL();
			}
			// crawl the page of currURL
			int fileId = Report.getCurrUrlId();
			crawlerWorker.crawl(currURL, fileId);
//			try {
//				TimeUnit.SECONDS.sleep(3);  // wait 3 second
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} //
			System.out.println("pagesVisited.size() = " + this.pagesVisited.size());
			
			this.pagesToVisisted.addAll(crawlerWorker.getLinks());
		}
		System.out.println(String.format("**Done** Visited %s web page(s)",
				this.pagesVisited.size()));
	}
	
	
	
	
	/**
	 * return the next URL to visit
	 * @return nextURL
	 */
	private String nextURL() {
		String nextURL;
		// make sure that nextURL has not already been visited
		do {
			nextURL = this.pagesToVisisted.remove(0);
		} while (this.pagesVisited.contains(nextURL));
		this.pagesVisited.add(nextURL);
		return nextURL;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
