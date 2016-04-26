package webCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
public class CrawlerWorker {
	// use a fake USER_AGENT so the web server thinks the robot is a normal web browser
	private static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	
	private List<String> links = new LinkedList<String>(); // a list of URLs
	
	/**
	 * 1. makes an HTTP request, checks the response and collects all the links on that page.
	 * 2. Collect words after the successful crawl.
	 * @param nextURL
	 * @return whether of not the crawl was successful
	 * @throws Exception 
	 */
	public boolean crawl(String url, int fileId) throws Exception {
		if (!isAllowed(url)) { 
			return false;
		}
		try {
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
				System.out.println("**Visiting**\nReceived web page: " + url);
				
				if (!response.contentType().contains("text/html")) {
					System.out.println("**Failure**\nRetrieved something other than HTML");
					return false;
				}
				
				
				System.out.println("title: " + title);
				
				Elements linksOnPage = htmlDocument.select("a[href]");
				numofLinks = linksOnPage.size();
				System.out.println("Found (" + linksOnPage.size() + ") links");
				// store the links in a private field
				for (Element link: linksOnPage) {
					String crawledUrl = link.absUrl("href");
					if (!crawledUrl.isEmpty()) {
						this.links.add(crawledUrl);
					}
				}
				
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
				generateHtmlFile(fileId, htmlDocument); 
			}
			

			// save pages info in report
			Report.save(fileId, title, url, statusCode, numofLinks, numofImages);
			return true;
		}
		catch (IOException ioe) {  // not successful in HTTP request
			ioe.printStackTrace();
			System.out.println(ioe.getMessage());
			System.out.println(ioe.getLocalizedMessage());
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
	
	
	/**
	 * return a list of all the URLs on the pages
	 * @return list
	 */
	public List<String> getLinks() {
//		List<String> list = new LinkedList<String>();
		return this.links;
	}
	
	
	/**
	 * generate html file for each page in directory
	 * @param url
	 * @param fileId
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
	
	

}

