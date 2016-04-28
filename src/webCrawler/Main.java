package webCrawler;

public class Main {
	public static final String REPO = "repository";
	public static final String REPORT = "report.html";
	public static final String PROCESSED_REPO = "processed_repository";

	public static final int MAX_PAGE_TO_SEARCH = 10;
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

		crawler.run(seed);
	}

}
