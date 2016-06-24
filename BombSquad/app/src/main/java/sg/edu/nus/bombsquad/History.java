package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

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

        try {
            Intent intent = getIntent();
            final String room_name = intent.getStringExtra("room_name");
            JSONObject room = new JSONObject(intent.getStringExtra("room"));
            System.out.println(room);
            System.out.println(room.length());
            LinearLayout ll = (LinearLayout) findViewById(R.id.history_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;
            while (i < room.length()) {
                Button myButton = new Button(this);
                myButton.setText(room.getJSONObject(i+"").getString("room_name"));
                myButton.setId(i);
                ll.addView(myButton, lp);
                i++;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
