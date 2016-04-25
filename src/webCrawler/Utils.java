package webCrawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	
	public static void createFile(String fileName) {
		try {
	    	FileWriter fileWriter = new FileWriter(fileName, false); // false - overwrite; true - append
	    	fileWriter.close();
	    } catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void createDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            System.out.println("Directory " + dirName + " not existed, going to create it");
            // attempt to create the directory here
            boolean successful = dir.mkdir();
            if (successful) {
                // creating the directory succeeded
                System.out.println("Directory " + dirName + " was created successfully");
            } else {
                // creating the directory failed
                System.out.println("Failed trying to create " + dirName + " directory");
            }
        } else {
            System.out.println("Directory " + dirName + " already existed");
        }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
