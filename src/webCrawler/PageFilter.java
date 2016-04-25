package webCrawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PageFilter {
	
	private static HashMap<String, ArrayList<ArrayList<Integer>>> visitedPages = new HashMap<String, ArrayList<ArrayList<Integer>>>();
	
	/**
	 * save page info into hashMap
	 * @param title
	 * @param numofLinks
	 * @param numofImages
	 * @param fileSize
	 */
	public static void save(String title, int numofLinks, int numofImages, int fileSize) {
		ArrayList<Integer> pageInfo = new ArrayList<Integer>(Arrays.asList(numofLinks, numofImages, fileSize));
		if (!visitedPages.containsKey(title)) {
			visitedPages.put(title, new ArrayList<ArrayList<Integer>>());		
		}
		visitedPages.get(title).add(pageInfo);
		return;
	}
	
	/**
	 * Check out whether it is a duplicated page
	 * @param title
	 * @param numofLinks
	 * @param numofImages
	 * @param bodySize
	 * @return true or false
	 */
	public static boolean contain(String title, int numofLinks, int numofImages, int bodySize) { 
		if (visitedPages.containsKey(title)) {
			for (int i = 0; i < visitedPages.get(title).size(); i++) {
				if (visitedPages.get(title).get(i).get(0) == numofLinks && 
					visitedPages.get(title).get(i).get(1) == numofImages &&
					visitedPages.get(title).get(i).get(2) == bodySize) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		save("ab", 1, 2, 3);
		save("ab", 1, 2, 4);
		save("cd", 1, 2, 3);
		System.out.println(contain("ab", 1, 2, 3));
//		contain("ab", 1, 2, 4);
//		contain("cd", 1, 2, 3);		
		System.out.println(visitedPages.get("ab").get(1));
		

	}

}
