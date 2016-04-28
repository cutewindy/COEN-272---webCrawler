package webCrawler;

public class UrlManager {

    private int urlId = 0;

    public int getUrlId() {
        return this.urlId;
    }

    public void increaseUrlId() {
        this.urlId += 1;
    }
}
