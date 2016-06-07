package masterproject.studentAtUniversityKiel.knauf.torsten.metos3dPrototyp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button startButton;
    TextView resultView;
    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisActivity = this;
        this.startButton = (Button) super.findViewById(R.id.startMetos3d);
        this.resultView = (TextView) super.findViewById(R.id.tv);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    (new Metos3d(thisActivity, resultView)).execute();
                } catch(ExceptionMetos3dNotInDownloads e) {
                    resultView.setTextColor(Color.RED);
                    resultView.setText(e.toString() + "\n\n Please put the folder there and restart this App");
                    resultView.setTextColor(Color.BLACK);
                }
            }
        });
    }
}


