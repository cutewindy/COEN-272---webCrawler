package webCrawler;

import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
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
	
	
	private static final int MAX_PAGE_TO_SEARCH = 10000;
		
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public static final int WORKER_NUM = 4;

	
	/**
	 * Create CrawlerLeg to crawl the web page and collect all the links on that page
	 * @param seedUrl
	 */
	public void run(String seedUrl) throws InterruptedException {

		// init managers
		UrlManager urlManager = new UrlManager();
		ReportManager reportManager = new ReportManager();

		// init bq
		ArrayBlockingQueue<Url> bq = new ArrayBlockingQueue<Url>(1000000);
		bq.put(new Url(seedUrl, 0));

		// init workers
		ArrayList<CrawlerWorker> workers = new ArrayList<CrawlerWorker>();
		for (int i = 0; i < WORKER_NUM; i++) {
			workers.add(new CrawlerWorker(bq,
					                      "worker " + Integer.toString(i),
										  urlManager,
										  reportManager,
										  this));
		}

		// run workers
		for (int i = 0; i < WORKER_NUM; i++) {
			workers.get(i).start();
		}



//
//		this.pagesToVisit.add(url);
//		while (this.pagesVisited.size() < MAX_PAGE_TO_SEARCH && this.pagesToVisit.size() > 0) {
//			String currURL;
//			CrawlerWorker crawlerWorker = new CrawlerWorker();
//			currURL = this.nextURL();
//
//			// crawl the page of currURL
//			int fileId = Report.getCurrUrlId();
//			try {
//				System.out.println("\n== Crawling " + currURL + " ==");
//				crawlerWorker.crawl(currURL, fileId);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			try {
////				TimeUnit.SECONDS.sleep(3);  // wait 3 second
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} //
//			System.out.println("pagesVisited.size() = " + this.pagesVisited.size());
//
//			this.pagesToVisit.addAll(crawlerWorker.getLinks());
//		}
//		System.out.println(String.format("**Done** Visited %s web page(s)",
//				this.pagesVisited.size()));
	}
	
	
	/**
	 * return the next URL to visit
	 * @return nextURL
	 */
	private String nextURL() {
		String nextURL;
		// make sure that nextURL has not already been visited
		do {
			nextURL = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextURL));
		this.pagesVisited.add(nextURL);
		return nextURL;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

