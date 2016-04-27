package webCrawler;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;

/**
 * 1. Crawl the page(make an HTTP request and parse the page)
 * 2. Collect all the links on that pages
 * 3. Collect all the words on that pages (Not done)
 * 4. Search for a word
 * 5. Return all the links on the page
 * @author Wendi
 *
 */
public class CrawlerWorker extends Thread {
	// use a fake USER_AGENT so the web server thinks the robot is a normal web browser
	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	


	private BlockingQueue<Url> bq;
	private int urlId = 0;
	UrlManager urlManager;
	ReportManager reportManager;
	Crawler crawlManager;
	public CrawlerWorker(BlockingQueue<Url> bq, String threadName, UrlManager urlManager, ReportManager reportManager, Crawler crawlManager){
		this.bq = bq;
		setName(threadName);
		this.urlManager = urlManager;
		this.reportManager = reportManager;
		this.crawlManager = crawlManager;
	}

	@Override
	public void run() {
		try {
			while(!bq.isEmpty()) {
				synchronized (this.crawlManager) {
					crawl();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1. makes an HTTP request, checks the response and collects all the links on that page.
	 * 2. Collect words after the successful crawl.
	 * @return whether of not the crawl was successful
	 * @throws Exception 
	 */
//	public boolean crawl(String url, int fileId) throws Exception {
	public synchronized boolean crawl() throws Exception {
		Url urlObj = getUrl();
		System.out.println("\n" + this.getName() + " getUrl: " + urlObj.url + " urlId:" + urlObj.urlId);
		System.out.println("inside crawl");
		String url  = urlObj.url;
		if (!isAllowed(url)) {
			return false;
		}
		try {
			System.out.println("**Visiting**");
			Connection.Response response = Jsoup.connect(url)
												.userAgent(USER_AGENT)
												.timeout(100000)
												.ignoreHttpErrors(true)
												.execute();
			int statusCode = response.statusCode();
			System.out.println("statusCode: " + statusCode);
			Document htmlDocument = response.parse();
			
			String title = htmlDocument.title();
			int numofLinks = -1;
			int numofImages = -1;
			if (statusCode == 200) {  // 200 is the HTTP OK status code

				if (!response.contentType().contains("text/html")) {
					System.out.println("**Failure**\nRetrieved something other than HTML");
					return false;
				}
				
				
				System.out.println("title: " + title);

//				ArrayList<String> links = new ArrayList<String>(); // a list of URLs
				Elements linksOnPage = htmlDocument.select("a[href]");
				numofLinks = linksOnPage.size();
				System.out.println("Found (" + linksOnPage.size() + ") links");
				// store the links in a private field
				for (Element link: linksOnPage) {
					String crawledUrl = link.absUrl("href");
					if (!crawledUrl.isEmpty()) {
//						links.add(crawledUrl);
						addUrl(crawledUrl);
					}
				}
				System.out.println(bq.toString());

				Elements imagesOnPage = htmlDocument.select("img");
				numofImages = imagesOnPage.size();	
				
				int bodySize = response.bodyAsBytes().length;
				
				// check duplicate
				if (PageFilter.contain(title, numofLinks, numofImages, bodySize)) {
					return true;
				}
				else {
					PageFilter.save(title, numofLinks, numofImages, bodySize);
				}
				
				// save pages info as html
				generateHtmlFile(urlObj.urlId, htmlDocument);
			}
			

			// save pages info in report
//			synchronized (this.reportManager) {
			System.out.println("calling reportM");
				reportManager.save(urlObj.urlId, title, url, statusCode, numofLinks, numofImages);
//			}
			return true;
		}
		catch (IOException ioe) {  // not successful in HTTP request
			ioe.printStackTrace();
			System.out.println(ioe.getMessage());
			System.out.println(ioe.getLocalizedMessage());
			return false;
		}
		catch (Exception e) {  // not successful in HTTP request
			e.printStackTrace();
			return false;
		}

	}
	
	
	/**
	 * use robots.txt to check out whether is allowed to crawl that server 
	 * @param url
	 * @return
	 */
	public static boolean isAllowed(String url) {
		try {
			URL URL = new URL(url);
			String domain = URL.getHost();
			String robotsUrl = URL.getProtocol() + "://" + domain + "/robots.txt";
			Connection.Response response = Jsoup.connect(robotsUrl)
												.userAgent(USER_AGENT)
												.timeout(100000)
												.ignoreHttpErrors(true)
												.execute();
			Document robotDocument = response.parse();
			SimpleRobotRulesParser parser = new SimpleRobotRulesParser();
			BaseRobotRules rules = parser.parseContent(
					domain, robotDocument.toString().getBytes("UTF-8"),
					"text/plain", USER_AGENT);
//			System.out.println("allowed: " + rules.isAllowed(url));
			return rules.isAllowed(url);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}
	
	
//	/*false*
//	 * return a list of all the URLs on the pages
//	 * @return list
//	 */
//	public List<String> getLinks() {
////		List<String> list = new LinkedList<String>();
//		return this.links;
//	}
	
	
	/**
	 * generate html file for each page in directory
	 * @param fileId
	 * @param htmlDocument
	 */
	public void generateHtmlFile(int fileId, Document htmlDocument) {
		try {
			String fileName = String.format("%s/%d.html", Main.REPO, fileId);
        	FileWriter fileWriter = new FileWriter(fileName, false);
        	fileWriter.write(htmlDocument.toString());
        	fileWriter.close();
        	
        } catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public Url getUrl() {
		Url url = null;
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				url = bq.take();
				if (url != null && url.url.length() != 0) {
//					System.out.println(this.getName() + " getUrl: " + url.url + " urlId:" + url.urlId);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public void addUrl(String urlStr) {
        try {
            int urlId;
//            synchronized (this.urlManager) {
                this.urlManager.increaseUrlId();
                urlId = this.urlManager.getUrlId();
//            }
            Url url = new Url(urlStr, urlId);
            bq.put(url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	

}

