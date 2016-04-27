package webCrawler;

/**
 * Created by xili on 4/27/16.
 */
public class Url {
    String url;
    int urlId;

    Url(String url, int urlId) {
        this.url = url;
        this.urlId = urlId;
    }

    public String toString() {
        return this.url;
    }
}
