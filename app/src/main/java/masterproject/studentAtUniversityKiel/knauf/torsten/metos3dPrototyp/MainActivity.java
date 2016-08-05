package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button optionButton;
    Button startButton;
    TextView resultView;
    Activity thisActivity;
    Boolean optionFileChanged;
    Metos3dOptionFileParser optionFileParser;

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
                     if(optionFileParser == null)
                        optionFileParser = new Metos3dOptionFileParser(Metos3d.pathToOptionFile);
                 } catch(IOException e) {
                     setErrorText(resultView, e.toString());
                     return;
                 }

                 final PopupMenu menu= new PopupMenu(thisActivity, optionButton);
                 for(Map.Entry<String, String> e : optionFileParser.getEntries().entrySet()) {
                     menu.getMenu().add(e.getKey()+" "+e.getValue());
                 }

                 menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         menu.show(); // menu disappears per default on click even if returning false (what it shouldn't after API description)
                         String line = item.getTitle().toString();
                         createEditDialog(item, optionFileParser, optionFileParser.getKey(line), optionFileParser.getValue(line)).show();
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
                    (new Metos3d(thisActivity, resultView, optionFileChanged, optionFileParser)).execute();
                } catch (ExceptionMetos3dNotInDownloads e) {
                    setErrorText(resultView, e.toString()+"\n\n Please put the full metos3d folder there");
                    /*
                    *** Not yet supported ***
                    AlertDialog.Builder alert = new AlertDialog.Builder(thisActivity);
                    alert.setTitle(e.toString());
                    alert.setMessage("Do you want to download it? Please note that it is bigger than 1GB.");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            URL[] urls={}; // Have to be prepared by one server
                            (new DownloadFilesTask(thisActivity, urls)).execute();
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // do nothing
                        }
                    });;
                    */
                }
            }
        });
    }

    private void setErrorText(TextView tv, String error) {
        tv.setTextColor(Color.RED);
        tv.setText(error);
        tv.setTextColor(Color.BLACK);
    }
}


