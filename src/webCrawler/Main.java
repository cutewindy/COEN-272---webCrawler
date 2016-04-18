package webCrawler;

public class Main {
	public static final String REPO = "repository";
	public static final String REPORT = "report.html";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils.createDir(REPO);
		Utils.createFile(REPORT);
		
		Crawler crawler = new Crawler();
		String seed = "https://www.scu.edu/";
		crawler.run(seed);
	}

}
