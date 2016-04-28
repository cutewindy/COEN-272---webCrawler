package webCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class ProcessContent {	
    public static String processOnePage(String filePath, String filePathAf) {
    			BufferedReader br = null;
    	        String line = null;

    	        FileWriter fileWriter = null;
    	        BufferedWriter bufferedWriter = null;

    	        // in the matrix, [][1] is the count of tags, [][0] is the count of token
    	        int[][] matrixAccum = new int[10000][2];        // record the accumulation of tags and tokens
    	        int[][] matrixStep = new int[10000][2];         // record the tags and tokens at each step
    	        String[] tokens = new String[10000];            // record all the tokens
    	        int kg = 0;
    	        
    	        int maxg = 0, ig = 0, jg = 0;
    	        int part1 = 0, part2 = 0;
    	        int sumtem = 0;
    	        
    	        String stringG ="";
    	        String stringGM ="";
    	        
    	        
    	        // read in the .txt file
    	        try {
    	            br = new BufferedReader(new FileReader(filePath));
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }  
    	        
    	        
    	        // read the file line by line, and split by space on each line to count tags and tokens
    	        do {
    	            try {
    					line = br.readLine();
    					if (line != null && !line.equals("")){
    						for (String splitedString : line.split(" ")) {     // tokenize by space
    							if (splitedString.length() != 0 && splitedString.length() < 40 && kg < 10000-1) {    // remove the case when the token item is empty or very long
    								int tagCount = StringUtils.countMatches(splitedString, "<>");    // count tags in a token item
    								String tem = StringUtils.remove(splitedString, "<>");            // remove tags in a token item
    								
    								if (tem.length() == splitedString.length()) {   
    									// in this case, there is only one word in this token
    									matrixAccum[kg+1][0] = matrixAccum[kg][0] + 1;              // token + 1
    									matrixAccum[kg+1][1] = matrixAccum[kg][1];                  // tag no change
    									matrixStep[kg+1][0] = 1;
    									matrixStep[kg+1][1] = 0;
    								} else if (tem.length() == 0) {	
    									// in this case, all tags, no word
    									matrixAccum[kg+1][0] = matrixAccum[kg][0] + tagCount;       // token + tag counts
    									matrixAccum[kg+1][1] = matrixAccum[kg][1] + tagCount;       // tag + tag counts
    									matrixStep[kg+1][0] = tagCount;
    									matrixStep[kg+1][1] = tagCount;
    								} else {                                  
    									// in this case, there is one word and several tags in this token
    									matrixAccum[kg+1][0] = matrixAccum[kg][0] + 1 + tagCount;   // token + 1 + tag counts
    									matrixAccum[kg+1][1] = matrixAccum[kg][1] + tagCount;       // tag + tag counts
    									matrixStep[kg+1][0] = 1 + tagCount;
    									matrixStep[kg+1][1] = tagCount;
    								}
    								
    								tokens[kg+1] = splitedString;
    								kg++;
    							}
    		
    						}
    		            } 
    				} catch (IOException e) {
    					e.printStackTrace();
    				}     
    	        } while (line != null);
    	        
    	        
    			// write output of the count matrixAccum, so the plot can be opened by other software
    	        try {
    	        	fileWriter = new FileWriter(filePathAf);
    	            bufferedWriter = new BufferedWriter(fileWriter);
    	            for (int t = kg; t >= 0; t--) {
    	            	bufferedWriter.write(matrixAccum[kg - t][0] + ", " + matrixAccum[kg - t][1]);
    	            	bufferedWriter.newLine();
    	            }
    	        	
    	        	bufferedWriter.flush();
    	        	bufferedWriter.close();
    	        } catch (IOException e) {
    				e.printStackTrace();
    			}  
    	  

    	        // estimate i and j, by the maximation of formular learned in class
    	        for (int i = 0; i <= kg; i++) {     // part1, sigma bn from 0 to end
    	        	part1 += matrixStep[i][1];
    	        }
    	        
    	        for (int i = 0; i <= kg; i++) {       // part 2, (j-i) + sigma bn from i to j
    	        	for (int j = i+1; j <= kg; j++) {
    	        		sumtem = 0;
    	        		for (int m = i; m <= j; m++) {         //sigma bn from i to j
    	        			sumtem += matrixStep[m][1];
    	        		}
    	        		if (maxg < part1 + (j-i) - 2 * sumtem) {
    	        			maxg = part1 + (j-i) - 2 * sumtem;
    	        			ig = i;
    	        			jg = j;
    	        		}
    	        	}
    	        }
    	        //System.out.println("in the maximation condition, i is " + ig + ", j is " + jg);
    	        
    	        
    	        // get the content between i and j, which is built as a string, remove noise like <>, special character, stopping words like a, an, the, and
    	        for (int i = ig; i <= jg; i++) {
    	        	stringG += tokens[i] + " ";      // get the content from i to j as a single string, including the tags
    	        }
    	        
    	        stringGM = StringUtils.remove(stringG, "<>");     // remove the tags, etc 
    	        stringGM = StringUtils.remove(stringGM, ".");
    	        stringGM = StringUtils.remove(stringGM, ",");
    	        stringGM = StringUtils.remove(stringGM, ";");
    	        stringGM = StringUtils.remove(stringGM, ":");
    	        stringGM = StringUtils.remove(stringGM, "’");
    	        stringGM = StringUtils.remove(stringGM, "‘");
    	        stringGM = StringUtils.remove(stringGM, "”");
    	        stringGM = StringUtils.remove(stringGM, "“");
    	        stringGM = StringUtils.remove(stringGM, "");
    	        stringGM = StringUtils.remove(stringGM, "");
    	        stringGM = StringUtils.lowerCase(stringGM);      // lowercase

    	        stringGM = stringGM.replaceAll("&\\s*[^&\\s]*","");
    	        stringGM = stringGM.replaceAll("\"", "");    // actually remove the word with double quotes
    	        //stringGM = stringGM.replaceAll(" a ", " ");      // remove stopper like a
    	        
		return stringGM;
	}

    
	@SuppressWarnings("deprecation")
    public static void process() {
		String stringOfAllPages = "";
        String filePath, filePathAf;
        
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        
        FileWriter fileWriterTem = null;
        BufferedWriter bufferedWriterTem = null;
        
        
        // get all the crawled files and process the content, to get a final string as stringOfAllPages
        for (int i = 1; i <= Main.MAX_PAGE_TO_SEARCH; i++) {
        	filePath = "processedCON/" + i + ".txt";
        	filePathAf = "processedCON/processed" + i + ".txt";
        	
        	// check if the file exist, reference to http://stackoverflow.com/questions/11220678/checking-if-file-exists-in-a-specific-directory
        	File f = new File(filePath);
        	if(f.exists()){   
        	    stringOfAllPages += processOnePage(filePath, filePathAf) + " ";
        	}
        }
        
     	System.out.println(stringOfAllPages);
        
     	
        // use hashmap to get word and the corresponding frequency
     	for (String splitedString : stringOfAllPages.split(" ")) {     // tokenize by space
     		splitedString.replaceAll("\\s+","");                       // remove whitespace
     		
     		if (splitedString.length() != 0) {
     			if (hmap.containsKey(splitedString)) {
         			hmap.put(splitedString, hmap.get(splitedString) + 1);
         		} else {
         			hmap.put(splitedString, 1);
         		}
     		}
     		
     	}
        System.out.println(hmap);
        
        
		// sort the values in hashmap
        hmap = sortByValue(hmap);
        System.out.println(hmap);
        
        
        // write output to file "zipfData.txt"
        int index = 1;
        try {
        	fileWriterTem = new FileWriter("zipfData.txt");
            bufferedWriterTem = new BufferedWriter(fileWriterTem);
            bufferedWriterTem.write("rank, word, frequency");
            bufferedWriterTem.newLine();
            
            for (Map.Entry<String, Integer> mapEntry : hmap.entrySet()) {
                bufferedWriterTem.write(index + ", " + mapEntry.getKey() + ", " + mapEntry.getValue());
            	bufferedWriterTem.newLine();
            	index++;
            }
        	
        	bufferedWriterTem.flush();
        	bufferedWriterTem.close();
        } catch (IOException e) {
			e.printStackTrace();
		}  
        
        System.out.println("\n\n********************FINISH FILE PROCESSING******************");
	}
	
	
	// sort based on the values of hashmap, reference to http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java?page=1&tab=votes#tab-top
	public static <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue( Map<String, Integer> map ) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 ){
            	int compare = o1.getValue().compareTo( o2.getValue());
            	// change the order so max frequency will be in the 1st place
            	if (compare == -1) return 1;
            	else if (compare == 1) return -1;
            	else return 0;
            }
        } );

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
	
}

