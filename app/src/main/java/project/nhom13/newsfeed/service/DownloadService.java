package project.nhom13.newsfeed.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import project.nhom13.newsfeed.R;
import project.nhom13.newsfeed.model.NewsHeader;
import project.nhom13.newsfeed.model.FeedDBHelper;

/**
 * Created by WILL on 11/20/2016.
 */

public class DownloadService extends IntentService {
    public static String HEADER_EXTRA = "header";

    public DownloadService(){
        super("Download Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NewsHeader header = (NewsHeader)intent.getSerializableExtra(HEADER_EXTRA);
        FeedDBHelper helper = new FeedDBHelper(this,null,FeedDBHelper.DB_VERSION);

        PendingIntent pIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        try{
            String html = getHtml(header.getUrl());
            if(html!=null){
                helper.add_article(html,header.getUrl(),header.getSite(),header.getPubDateAsString(),
                        header.getTitle(),header.getPreview());

                Notification note = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.notify_download_success))
                        .setContentText(header.getTitle())
                        .setContentIntent(pIntent)
                        .setSmallIcon(R.drawable.appicon)
                        .build();
                manager.notify(0,note);
            }else throw new Exception("");
        }catch (Exception e){
            e.printStackTrace();
            Notification note = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.notify_download_failure))
                    .setContentText(header.getTitle())
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.appicon)
                    .build();
            manager.notify(0,note);
        }
        helper.close();
    }

    private static String getHtml(String url) throws IOException {
        // Build and set timeout values for the request.
        URLConnection connection = (new URL(url)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        connection.connect();

        // Read and store the result line by line then return the entire string.
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder html = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            html.append(line);
        }
        in.close();

        return html.toString();
    }
}
