package project.nhom13.newsfeed.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by WILL on 11/19/2016.
 */

public class ImageGetter implements Html.ImageGetter {
    ImageView imageView;

    public ImageGetter(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        DownloadImageTask task = new DownloadImageTask(imageView);
        task.execute(source);
        return drawable;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
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
            if(imageView.getDrawable()==null) imageView.setImageBitmap(result);
        }
    }
}
