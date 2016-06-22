package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button optionButton;
    Button startButton;
    TextView resultView;
    Activity thisActivity;
    Boolean optionFileChanged;
    Metos3dOptionFileParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisActivity = this;
        this.optionFileChanged = false;
        this.resultView = (TextView) super.findViewById(R.id.tv);
        this.setupOptionButton();
        this.setupStartButton();

    }

    private void setupOptionButton() {
        this.optionButton = (Button) super.findViewById(R.id.editOption);
        this.optionButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 try {
                     if(parser==null)
                        parser = new Metos3dOptionFileParser(Metos3d.pathToOptionFile);
                 } catch(IOException e) {
                     setErrorText(resultView, e.toString());
                     return;
                 }

                 final PopupMenu menu= new PopupMenu(thisActivity, optionButton);
                 for(Map.Entry<String, String> e : parser.getEntries().entrySet()) {
                     menu.getMenu().add(e.getKey()+" "+e.getValue());
                 }

                 menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         menu.show(); // menu disappears per default on click even if returning false
                         String line = item.getTitle().toString();
                         createEditDialog(item, parser, parser.getKey(line), parser.getValue(line)).show();
                         return true; // meaning event is handled
                     }
                 });

                 menu.show();
             }
         });
    }

    private AlertDialog.Builder createEditDialog(final MenuItem item, final Metos3dOptionFileParser parser, final String key, String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(key);
        alert.setMessage(value);
        final EditText input = new EditText(this);
        input.setText(value);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                parser.editEntry(key, input.getText().toString());
                item.setTitle(key+" "+input.getText());
                optionFileChanged = true;
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });

        return alert;
    }

    private void setupStartButton() {
        this.startButton = (Button) super.findViewById(R.id.startMetos3d);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    (new Metos3d(thisActivity, resultView, optionFileChanged, parser)).execute();
                } catch (ExceptionMetos3dNotInDownloads e) {
                    setErrorText(resultView, e.toString() + "\n\n Please put the folder there and restart this App");
                }
            }
        });
    }

    private void setErrorText(TextView tv, String error) {
        tv.setTextColor(Color.RED);
        tv.setText(error + "\n\n Please put the folder there and restart this App");
        tv.setTextColor(Color.BLACK);
    }
}


