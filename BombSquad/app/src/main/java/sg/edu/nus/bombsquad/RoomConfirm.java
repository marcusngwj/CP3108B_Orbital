package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class roomConfirm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_confirm);
        display();
    }

    private void display() {
        final int[] id = new int[100000];
        try {
            Intent intent = getIntent();
            JSONObject bomb = new JSONObject(intent.getStringExtra("bomb"));
            boolean[] selected = intent.getBooleanArrayExtra("selected");
            LinearLayout ll = (LinearLayout) findViewById(R.id.roomConfirmScroll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;
            int j = 0;
            while (i < 100000) {
                if (selected[i]) {
                    id[j] = Integer.parseInt(bomb.getJSONObject(i+"").getString("question_id"));
                    CheckedTextView ctv = new CheckedTextView(this);
                    ctv.setText(bomb.getJSONObject(i+"").getString("bomb_name"));
                    ctv.setTextSize(20);
                    assert ll != null;
                    ll.addView(ctv, lp);
                }
                i++;
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }


    }

}