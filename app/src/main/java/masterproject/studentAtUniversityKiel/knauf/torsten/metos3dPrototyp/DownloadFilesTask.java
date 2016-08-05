package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class DownloadFilesTask extends AsyncTask<Void, Integer, Void> {
    private Context context;
    URL[] urls;
    private ProgressDialog progressDialog;
    private long urlsCount;

    public DownloadFilesTask(Context context, URL[] urls) {
        this.context = context;
        this.urls = urls;
        this.urlsCount = urls.length;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < this.urlsCount; i++) {
            publishProgress(i);
            String targetDir = "";
            this.download(urls[i], targetDir);
            if (isCancelled()) break;
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog = ProgressDialog.show(this.context, "", "0 from "+this.urlsCount);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        this.progressDialog.setMessage(String.format("%1$d from %2$d", progress[0], this.urlsCount));
    }

    @Override
    protected void onPostExecute(Void v) {
        this.progressDialog.dismiss();
    }

    private void download(URL url, String target) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(target);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            Log.d("HIER", e.toString());
        }
    }
}
