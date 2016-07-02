package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitingRoom extends AppCompatActivity {
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
            JSONObject game = new JSONObject(intent.getStringExtra("game"));
            LinearLayout ll = (LinearLayout) findViewById(R.id.waitingRoom_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;
            while (i < game.length()) {
                Button myButton = new Button(this);
                myButton.setText(game.getJSONObject(i+"").getString("player"));
                myButton.setId(i);
                assert ll != null;
                ll.addView(myButton, lp);
                i++;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
