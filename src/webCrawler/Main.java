package webCrawler;

public class Main {
	public static final String REPO = "repository";
	public static final String REPORT = "report.html";
	public static final String PROCESSED_REPO = "processedCON";

	public static final int MAX_PAGE_TO_SEARCH = 1000;
	public static final int WORKER_NUM = 4;

	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Utils.createDir(REPO);
		Utils.createFile(REPORT);
		Utils.createDir(PROCESSED_REPO);
		
		Crawler crawler = new Crawler();
		String seed = "https://www.scu.edu/";
//		String seed = "https://www.scu.edu/abc"; // 404 testing
		System.out.println("Seed: " + seed);
		System.out.println("\n\n*************************BEGIN CRAWLING****************************");
		crawler.run(seed);
		System.out.println("\n\n**************END CRAWLING, AND START PROCESSING FILES*************");
		ProcessContent.process();          // process crawler content
		FormatReport.reformatReport();     // reformat report.html
		System.out.println("\n\n*************************REPORT GENERATED**************************");
		System.out.println("\n\n****************************END************************************");
	}

}
