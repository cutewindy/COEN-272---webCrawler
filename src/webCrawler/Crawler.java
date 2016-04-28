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
		urlManager.visitedUrls.add(seedUrl);

		// init workers
		ArrayList<CrawlerWorker> workers = new ArrayList<CrawlerWorker>();
		for (int i = 0; i < Main.WORKER_NUM; i++) {
			workers.add(new CrawlerWorker(bq,
					                      "worker " + Integer.toString(i),
										  urlManager,
										  reportManager,
										  this));
		}

		// run workers
		for (int i = 0; i < Main.WORKER_NUM; i++) {
			workers.get(i).start();
		}
		
		// stop workers
		for (int i = 0; i < Main.WORKER_NUM; i++) {
			workers.get(i).join();
		}
	}
}

