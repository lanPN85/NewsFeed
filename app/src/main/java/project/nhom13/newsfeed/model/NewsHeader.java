package project.nhom13.newsfeed.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by WILL on 11/15/2016.
 */

public class NewsHeader implements Comparable<NewsHeader>, Serializable{
    private String title;
    private GregorianCalendar pubDate;
    private String site;
    private String url;
    private String preview;
    private transient Bitmap thumbnail = null;
    private boolean downloaded;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public GregorianCalendar getPubDate() {
        return pubDate;
    }
    public String getPubDateAsString(){
        if(pubDate==null) return "";
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(pubDate.getTime());
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


    public String getPreview() {
        return preview;
    }
    public void setPreview(String preview) {
        if(preview==null){
            this.preview = null;
            return;
        }
        this.preview = preview.trim();
    }

    public boolean isDownloaded() {
        return downloaded;
    }
    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int compareTo(NewsHeader o) {
        if(o.getPubDate()==null) return -1;
        else if(pubDate==null) return 1;
        else{
            if(pubDate.after(o.getPubDate())) return -1;
            else if(pubDate.before(o.getPubDate())) return 1;
            else return 0;
        }
    }
}
