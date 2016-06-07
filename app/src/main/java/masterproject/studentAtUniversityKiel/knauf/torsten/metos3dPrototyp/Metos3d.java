package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Metos3d extends AsyncTask<Void, Void, String>{
    final String pathToM3d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/Metos3d";
    final String pathToOptionFile = pathToM3d + "/model/N-DOP/option/test.N-DOP.option.txt";
    final String nameOfExe = "metos3d-simpack-N-DOP.exe";
    final String pathToExe;

    Context context;
    TextView resultView;
    ProgressDialog progressDialog;

    public Metos3d(Context c, TextView tv) throws ExceptionMetos3dNotInDownloads {
        if(!(new File(pathToM3d)).isDirectory())
            throw new ExceptionMetos3dNotInDownloads("\n\n"+pathToM3d + " does not exist");

        this.context = c;
        this.resultView = tv;
        // private App storage. There we have permission to make it executable!
        pathToExe = c.getFilesDir().getAbsolutePath()+"/"+nameOfExe;
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog = ProgressDialog.show(this.context, "", "Calculating N-DOP");
        System.gc();
    }

    @Override
    protected String doInBackground(Void... params) {
        String pathToExeInM3dFolder = pathToM3d+"/"+nameOfExe;
        File exe = new File(pathToExe);
        if(!(exe.exists())) {
            try {
                CopyHelper.copyFile(new File(pathToExeInM3dFolder), exe);
            } catch(IOException e) {
                return e.toString();
            }
            if(!exe.setExecutable(true)) {
                return "Couldn't make "+exe.getAbsolutePath()+" make executable";
            }
        }

        return executeCmd(pathToExe+" "+pathToOptionFile, pathToM3d);
    }

    @Override
    protected void onPostExecute(String result) {
        this.resultView.setText(result);
        System.gc();
        this.progressDialog.dismiss();
    }

    private String executeCmd(String command, String workingDir) {
        StringBuffer result = new StringBuffer();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(command, null, new File(workingDir));
            BufferedReader terminalOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((line = terminalOutput.readLine()) != null) {
                result.append(line+"\n");
            }
            terminalOutput.close();

            while ((line = errorStream.readLine()) != null) {
                result.append("_myError_: "+line+"\n");
            }
            errorStream.close();

            p.waitFor(); // That's ok because we are in an AsyncTask
        }
        catch(Exception e) { // IOException and InterruptedException
            result.append("\n"+e.toString()+"\n");
        }

        return result.toString();
    }
}
