package sg.edu.nus.bombsquad;


import android.content.Intent;
import android.os.Bundle;
 import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditRoom extends AppCompatActivity{
    Global global = Global.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        display();
        plus();
        minus();
    }

    private void display() {
        final boolean[] selected = new boolean[100000];
        LinearLayout ll = (LinearLayout)findViewById(R.id.editRoomScroll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int i = 0;
        while (global.getQuestionName()[i] != null) {
            CheckBox checkbox = (CheckBox) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox, null);
            checkbox.setText(global.getQuestionName()[i]);
            checkbox.setId(Integer.parseInt(global.getQuestion_id()[i]));
            checkbox.setTextSize(25);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected[v.getId()]) {
                        selected[v.getId()] = false;
                    } else {
                        selected[v.getId()] = true;
                    }
                }
            });
            assert ll != null;
            ll.addView(checkbox, lp);
            i++;
        }
    }

    private void plus() {
        ImageButton greenPlus = (ImageButton)findViewById(R.id.greenPlus);
        assert greenPlus != null;
        greenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setEditRoomBoolean(true);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getJSONObject("0").getBoolean("success");

                            if(success){
                                Intent addRoomIntent = new Intent(EditRoom.this, BombDepo.class); //will be another class not bomb depo but similar
                                addRoomIntent.putExtra("bomb", jsonResponse.toString());
                                startActivity(addRoomIntent);
                            }
                        } catch (JSONException e) {
                         //e.printStackTrace();
                        }
                    }
                };
                Intent intent = getIntent();
                BombDepoRequest bombDepoRequest = new BombDepoRequest(intent.getStringExtra("user_id"), responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditRoom.this);
                queue.add(bombDepoRequest);




            }
        });

    }

    private void minus() {

    }
}
