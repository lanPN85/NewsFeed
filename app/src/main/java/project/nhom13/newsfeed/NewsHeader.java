package project.nhom13.newsfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by WILL on 11/15/2016.
 */

public class NewsHeader implements Comparable<NewsHeader>{
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
    public String getPubDateAsString(){
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

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void getImage(ImageView bmImage){
        DownloadImageTask dt = new DownloadImageTask(bmImage);
        dt.execute(imageUrl);
    }

    public String getPreview() {
        return preview;
    }
    public void setPreview(String preview) {
        this.preview = preview;
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        public DownloadImageTask() {

        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
