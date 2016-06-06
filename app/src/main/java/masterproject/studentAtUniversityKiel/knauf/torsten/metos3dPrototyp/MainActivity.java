package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    TextView resultView;
    Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.self = this;
        this.startButton = (Button) super.findViewById(R.id.startMetos3d);
        this.resultView = (TextView) super.findViewById(R.id.tv);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*final String pathToM3d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Metos3d";
                final String pathToOptionFile = pathToM3d + "/model/N-DOP/option/test.N-DOP.option.txt";
                final String nameOfExe = "metos3d-simpack-N-DOP.exe";
                final String pathToExe = getFilesDir().getAbsolutePath() + "/" + nameOfExe;
                File exe = new File(pathToExe);
                Log.d("HIER", ""+exe.exists()+exe.setExecutable(true)+exe.length());
                resultView.setText(executeCmd(pathToExe, null)); // + " " + pathToOptionFile, pathToM3d)); */
                try {
                    (new Metos3d(self, resultView)).execute();
                } catch(ExceptionMetos3dNotInDownloads e) {
                    resultView.setTextColor(Color.RED);
                    resultView.setText(e.toString() + "\n\n Please put the folder there and restart this App");
                }
            }
        });
    }

    private String executeCmd(String command, String workingDir) {
        StringBuffer result = new StringBuffer();
        try {
            String line;
            Log.d("HIER", command);
            Process p = Runtime.getRuntime().exec(command); //, null, new File(workingDir));
            BufferedReader terminalOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((line = terminalOutput.readLine()) != null) {
                result.append(line + "\n");
            }
            terminalOutput.close();

            while ((line = errorStream.readLine()) != null) {
                result.append("_myError_: " + line + "\n");
            }
            errorStream.close();

            Log.d("HIER", ""+p.waitFor()); // That's ok because we are in an AsyncTask
        } catch (Exception e) {
            result.append("\n" + e.toString() + "\n");
        }

        String r = result.toString();
        Log.d("HIER", "HI"+r);
        return r;
    }
}


