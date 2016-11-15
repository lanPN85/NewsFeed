package project.nhom13.newsfeed;

import java.util.GregorianCalendar;

/**
 * Created by WILL on 11/15/2016.
 */

public class NewsHeader {
    private String title;
    private GregorianCalendar pubDate;
    private String site;
    private String url;
    private String imageUrl;
    private String preview;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public GregorianCalendar getPubDate() {
        return pubDate;
    }
    public void setPubDate(GregorianCalendar pubDate) {
        this.pubDate = pubDate;
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreview() {
        return preview;
    }
    public void setPreview(String preview) {
        this.preview = preview;
    }
}
