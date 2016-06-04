package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button startButton;
//"metos3d/arch/metos3d-simpack-N-DOP.exe"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tv = (TextView) super.findViewById(R.id.tv);
        this.startButton = (Button) super.findViewById(R.id.startMetos3d);

        //String publicPathToMetos3d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/m3d";
        String pathToM3d = getExternalFilesDir(null).getAbsolutePath()+"/m3d";

        //copyExeInPrivateStorage(pathToM3d, pathToExe);

        /*try {
            copyDirectory(new File(publicPathToMetos3d), new File(pathToM3d));
        } catch(IOException e) {
            Log.d("HIER", "error copy dirs: "+e.toString());
        }*/

        final String pathToExe = getFilesDir().getPath()+"/metos3d-simpack-N-DOP.exe";
        final String pathToOptionFile=pathToM3d+"/model/N-DOP/option/test.N-DOP.option.txt";

        copyNewOptionFileFromAssets(pathToOptionFile);
        tv.setText("exe: "+(new File(pathToExe)).exists()+" optionFile: "+(new File(pathToOptionFile)).exists());

        this.startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String makeExecutable = "/system/bin/chmod 777 "+pathToExe;
                //Log.d("HIER", executeCmd(makeExecutable));
                Log.d("HIER", ""+(new File(pathToExe)).setExecutable(true));
                tv.setText(executeCmd("."+pathToExe+" "+pathToOptionFile));
            }
        });
    }

    private void copyNewOptionFileFromAssets(String pathToOptionFile) {
        File old = new File(pathToOptionFile);
        if(!old.exists()) {
            Log.d("HIER", "old does not exists");
            return;
        }
        old.delete();
        if(old.exists()) {
            Log.d("HIER", "delete hasn't worked");
            return;
        }

        AssetManager assetManager = super.getAssets();
        String err="";
        InputStream is=null;
        OutputStream os=null;
        try {
            is = assetManager.open("test.N-DOP.option.txt");
            File newFile = new File(pathToOptionFile);
            os = new FileOutputStream(newFile);
            copyFile(is, os);
        } catch(IOException e) {
            err=e.toString();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
        Log.d("HIER", "success replacing option File");
    }

    private void copyExeInPrivateStorage(String pathToM3d, String pathToExe) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(pathToM3d + "/metos3d-simpack-N-DOP.exe"));
            os = new FileOutputStream(pathToExe);
            copyFile(is, os);
        } catch (IOException e) {
            Log.d("HIER", "error copy exe: " + e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private String executeCmd(String c) {
        StringBuffer result = new StringBuffer();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(c);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                result.append(line+"\n");
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                result.append("_myError_ "+line+"\n");
            }
            bre.close();

            p.waitFor();
        }
        catch (Exception err) {
            result.append("_myException_ "+err.toString()+"\n");
        }

        return result.toString();
    }
}
