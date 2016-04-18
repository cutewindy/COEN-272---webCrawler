package webCrawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Report {
	
	private static int currUrlId = 1;
//	private static HashMap<Integer, String> map = new HashMap<Integer, String>();
	 
	public static int getCurrUrlId() {
		currUrlId++;
		return currUrlId - 1;
	}
	
	public static void save(
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
	
//	public static void show() {
//		System.out.println("Report:");
//		Set<Entry<Integer, String>> hashSet=map.entrySet();
//        for(Entry entry:hashSet ) {
//            System.out.println("Key="+entry.getKey()+", Value="+entry.getValue());
//        }
//        System.out.println("-----------------------------");
//        // Print the size of hashMap
//         System.out.println("HashMap size="+map.size());
//
//	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
