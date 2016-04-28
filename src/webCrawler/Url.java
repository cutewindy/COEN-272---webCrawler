package webCrawler;

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
