package webCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FormatReport {
	public static void reformatReport() {
		BufferedReader br = null;
	    String line = null;

	    FileWriter fileWriter = null;
	    BufferedWriter bufferedWriter = null;
	    
	    
	    // read in the report.html file
		try {
            br = new BufferedReader(new FileReader(Main.REPORT));
        } catch (Exception e) {
            e.printStackTrace();
        }  
        
		
        // rewrite the report.html file
		try {
			fileWriter = new FileWriter("report_fmt.html");
	        bufferedWriter = new BufferedWriter(fileWriter);
	        
	        bufferedWriter.write("<table border=\"1\"><tr><th>URL</th><th>crawled page</th><th>port status</th><th>link tag number</th><th>img tag number</th></tr>");
	        // read the file line by line, and split by space on each line to count tags and tokens
	        do {
					line = br.readLine();
					if (line != null) {
		                String[] temItems = line.split(","); 
		                if (temItems.length == 5) {
			                bufferedWriter.write("<tr><td>" + temItems[0] + "</td><td>" + temItems[1] + "</td><td>" + temItems[2] + "</td><td>" + temItems[3] + "</td><td>" + temItems[4] + "</td></tr>");
		                } else if (temItems.length > 5){
			                bufferedWriter.write("<tr><td>" + temItems[0] + "</td><td>" + temItems[temItems.length-4] + "</td><td>" + temItems[temItems.length-3] + "</td><td>" + temItems[temItems.length-2] + "</td><td>" + temItems[temItems.length-1] + "</td></tr>");
		                }
					}
	        } while (line != null);
	        
	        bufferedWriter.write("</table>");
	        
	        bufferedWriter.flush();
	    	bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
        
	}
                       
}
