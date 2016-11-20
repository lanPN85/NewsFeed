package project.nhom13.newsfeed.util;

import android.os.AsyncTask;

import org.mcsoxford.rss.RSSException;
import org.mcsoxford.rss.RSSFault;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import java.util.GregorianCalendar;
import java.util.List;

import project.nhom13.newsfeed.NewsHeader;

/**
 * Created by WILL on 11/18/2016.
 */

public class RSSParser extends AsyncTask<String, Void, Void> {
    List<NewsHeader> list;
    List<String> articles;
    int limit;
    public static int threads_left;

    public RSSParser(List<NewsHeader> list, List<String> articles, int limit) {
        this.list = list;
        this.limit = limit;
        this.articles = articles;
    }

    @Override
    protected Void doInBackground(String... params) {
        String url = params[0];
        String site = params[1];

        RSSReader reader = new RSSReader();
        try {
            RSSFeed feed = reader.load(url);
            List<RSSItem> items = feed.getItems();

            int count = 0;
            for (RSSItem item : items) {
                NewsHeader header = new NewsHeader();
                header.setUrl(item.getLink().toString());
                header.setSite(site);
                header.setTitle(item.getTitle());
                header.setPreview(item.getDescription());

                boolean downloaded = (articles.contains(header.getUrl()));
                header.setDownloaded(downloaded);

                if(item.getPubDate()!=null){
                    GregorianCalendar pubDate = new GregorianCalendar();
                    pubDate.setTime(item.getPubDate());
                    header.setPubDate(pubDate);
                }else header.setPubDate(null);

                list.add(header);
                count++;
                if (count == limit) break;
            }
        } catch (RSSException | RSSFault e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        threads_left--;
    }
}
