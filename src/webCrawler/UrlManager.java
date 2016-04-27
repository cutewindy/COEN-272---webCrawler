package webCrawler;

/**
 * Created by xili on 4/27/16.
 */
public class UrlManager {

    private int urlId = 0;

    public int getUrlId() {
        return this.urlId;
    }

    public void increaseUrlId() {
        this.urlId += 1;
    }
}
