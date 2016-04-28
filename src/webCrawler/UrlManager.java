package webCrawler;

import java.util.HashSet;
import java.util.Set;


public class UrlManager {

    private int urlId = 0;
    public Set<String> visitedUrls = new HashSet<String>();
    
    public int getUrlId() {
        return this.urlId;
    }

    public void increaseUrlId() {
        this.urlId += 1;
    }
}
