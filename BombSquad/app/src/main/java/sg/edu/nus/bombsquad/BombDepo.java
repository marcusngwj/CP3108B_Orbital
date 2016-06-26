package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class BombDepo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_depo);
        display();
    }

    private void display() {

        try {
            Intent intent = getIntent();
            final String bomb_name = intent.getStringExtra("bomb_name");
            JSONObject bomb = new JSONObject(intent.getStringExtra("bomb"));
            System.out.println(bomb);
            System.out.println(bomb.length());
            LinearLayout ll = (LinearLayout) findViewById(R.id.bombDepoScroll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;
            final boolean[] selected = new boolean[100000];
            while (i < bomb.length()) {
                CheckBox checkbox = (CheckBox)((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox,null);
                checkbox.setText(bomb.getJSONObject(i+"").getString("bomb_name"));
                checkbox.setId(Integer.parseInt(bomb.getJSONObject(i+"").getString("question_id")));
                checkbox.setTextSize(25);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selected[v.getId()] = true;
                    }
                });
                assert ll != null;
                ll.addView(checkbox, lp);
                i++;
            }

            ImageButton greenTick = (ImageButton)findViewById(R.id.greenTick);
            assert greenTick != null;
            greenTick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("HELLO");
                    for(int i = 0; i < 100000; i++) {
                        if(selected[i]){
                            System.out.println(i);
                        }
                    }
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
