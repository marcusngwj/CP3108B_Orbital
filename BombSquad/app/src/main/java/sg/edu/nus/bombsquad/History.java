package sg.edu.nus.bombsquad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by user on 21/6/2016.
 */
public class History extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        display();
    }

    private void display() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.history_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < 40; i++) { //need to change to while loop to check if user had any room before not
            Button myButton = new Button(this);
            myButton.setText("Room" + i); //need to think how to pull from database
            myButton.setId(i); //pull from database
            ll.addView(myButton, lp);
        }
    }
}
