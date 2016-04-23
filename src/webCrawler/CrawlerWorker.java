package webCrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	private Document htmlDocument;
	
	/**
	 * 1. makes an HTTP request, checks the response and collects all the links on that page.
	 * 2. Collect words after the successful crawl.
	 * @param nextURL
	 * @return whether of not the crawl was successful
	 */
	public boolean crawl(String url, int fileId) {
		try {
			System.out.println("'" + url + "'");
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			int statusCode = connection.response().statusCode();
			if (statusCode == 200) {  // 200 is the HTTP OK status code
				System.out.println("\n**Visiting**\nReceived web page at " + url);
				generateHtmlFile(url, fileId); 
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("**Failure**\nRetrieved something other than HTML");
				return false;
			}
			
			String title = htmlDocument.title();
			System.out.println("title: " + title);
			
			Elements linksOnPage = htmlDocument.select("a[href]");
			int numofLinks = linksOnPage.size();
			System.out.println("Found (" + linksOnPage.size() + ") links");
			// store the links in a private field
			for (Element link: linksOnPage) {
				String crawledUrl = link.absUrl("href");
				if (!crawledUrl.isEmpty()) {
					this.links.add(crawledUrl);
				}
			}
			
			Elements imagesOnPage = htmlDocument.select("img");
			int numofImages = imagesOnPage.size();			
			Report.save(fileId, title, url, statusCode, numofLinks, numofImages);
//			System.out.print(this.htmlDocument.body().text());
			return true;
		}
		catch (IOException ioe) {  // not successful in HTTP request
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
	public void generateHtmlFile(String url, int fileId) {
		try {
			String fileName = String.format("%s/%d.html", Main.REPO, fileId);
        	FileWriter fileWriter = new FileWriter(fileName, false);
//        	fileWriter.write(htmlDocument.select("a").remove().toString());

        	fileWriter.write(htmlDocument.toString());
        	fileWriter.close();
        	
        } catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	

}

