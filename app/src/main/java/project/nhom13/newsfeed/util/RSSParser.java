package project.nhom13.newsfeed.util;

import android.os.AsyncTask;

import java.util.List;

import project.nhom13.newsfeed.NewsHeader;

/**
 * Created by WILL on 11/18/2016.
 */

public class RSSParser extends AsyncTask<String,Void,Void> {
    List<NewsHeader> list;
    int limit;
    String site;

    public RSSParser(List<NewsHeader> list, int limit){
        this.list = list;
        this.limit = limit;
    }

    @Override
    protected Void doInBackground(String... params) {
        /*String urlStr = params[0];
        site = params[1];

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            InputStream stream = conn.getInputStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);


            stream.close();
        }catch (Exception e){

        }*/


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
