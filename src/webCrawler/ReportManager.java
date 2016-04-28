package webCrawler;

import java.io.FileWriter;
import java.io.IOException;

public class ReportManager {
	
	public void save(
			int fileId,
			String title, 
			String url, 
			int statusCode, 
			int numofLinks,
			int numofImages) {
		
		try {
	    	FileWriter fileWriter = new FileWriter(Main.REPORT, true); // false - overwrite; true - append
//	    	<a href="url">title</a>
	    	String _title = String.format("%s", title);
	    	String _url = String.format("%s", url);
	    	String _statusCode = String.format("%d", statusCode);
	    	String _numofLinks = String.format("%d", numofLinks);
	    	String _numofImages = String.format("%d", numofImages);
	    	String delimiter = ", ";
	    	fileWriter.write(
//	    			"<a href=\"" + _url + "\">title</a>"
	    			String.format("<a href=\"%s\">%s</a>", url, title) + delimiter +
	    			String.format("<a href=\"%s\">%s</a>", Main.REPO + "/" + fileId + ".html", fileId + ".html") + delimiter +
	    			_statusCode + delimiter +
	    			_numofLinks + delimiter + 
	    			_numofImages + 
	    			"<br>\n"
	    	);
	    	
	    	fileWriter.close();
	    } catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
