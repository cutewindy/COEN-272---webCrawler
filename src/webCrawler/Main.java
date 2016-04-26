package webCrawler;

public class Main {
	public static final String REPO = "repository";
	public static final String REPORT = "report.html";
	public static final String PROCESSED_REPO = "processed_repository";

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils.createDir(REPO);
		Utils.createFile(REPORT);
		Utils.createDir(PROCESSED_REPO);
		
		Crawler crawler = new Crawler();
		String seed = "https://www.scu.edu/";
//		String seed = "https://www.scu.edu/abc"; // 404 testing
		System.out.println("Seed: " + seed);
		System.out.println("\n\n****************************BEGIN****************************");
		
		crawler.run(seed);
		
		System.out.println("\n\n****************************END****************************");
	}

}
