package sg.edu.nus.bombsquad;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class BombDepo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_depo);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: BombDepo");

        display();
    }

    private void display() {
        final Global global = Global.getInstance();
        final boolean[] selected = new boolean[100000];
        final String[] selected_name = new String[100000];
        final Intent intent = getIntent();
        try {
            final String bomb_name = intent.getStringExtra("bomb_name");
            JSONObject bomb = new JSONObject(intent.getStringExtra("bomb"));
            LinearLayout ll = (LinearLayout) findViewById(R.id.bombDepoScroll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;

            while (i < bomb.length()) {
                final String curr_bomb = bomb.getJSONObject(i + "").getString("bomb_name");
                CheckBox checkbox = (CheckBox) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox, null);
                checkbox.setText(curr_bomb);
                checkbox.setId(Integer.parseInt(bomb.getJSONObject(i + "").getString("question_id")));
                checkbox.setTextSize(25);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected[v.getId()]) {
                            selected[v.getId()] = false;
                        } else {
                            selected[v.getId()] = true;
                            selected_name[v.getId()] = curr_bomb;
                        }
                    }
                });
                assert ll != null;
                ll.addView(checkbox, lp);
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //GreenTick - Button
        ImageButton greenTick = (ImageButton) findViewById(R.id.greenTick);
        assert greenTick != null;
        greenTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject bomb = new JSONObject(intent.getStringExtra("bomb"));
                    Intent intentConfirm = new Intent(BombDepo.this, RoomConfirm.class);
                    intentConfirm.putExtra("bomb", bomb.toString());
                    intentConfirm.putExtra("user_id", intent.getStringExtra("user_id"));
                    intentConfirm.putExtra("room_name", intent.getStringExtra("room_name"));
                    intentConfirm.putExtra("room_code", intent.getStringExtra("room_code"));
                    //To check if anything is selected
                    boolean empty = true;
                    for (String name : selected_name) {
                        if (name != null) {
                            empty = false;
                        }
                    }
                    if (empty) {
                        Toast.makeText(getApplicationContext(), "Please select at least one", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(intentConfirm);
                        global.setData(selected);
                        global.setData(selected_name);
                    }

                } catch (JSONException e) {
                   // e.printStackTrace();
                }
            }
        });

        //RedCross - Button
        ImageButton redCross = (ImageButton) findViewById(R.id.redCross);
        assert redCross != null;
        redCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BombDepo.this);
                builder.setMessage("Are you sure you wish to delete this bomb? Deleted bomb cannot be retrieved anymore.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = 0;
                                while (i < 100000) {
                                    if (selected[i]) {
                                        CheckBox checkbox = (CheckBox) findViewById(i);
                                        LinearLayout ll = (LinearLayout) findViewById(R.id.bombDepoScroll);
                                        assert ll != null;
                                        ll.removeView(checkbox);
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                //Nothing here to see
                                            }
                                        };
                                        BombDeleteRequest bombDelete = new BombDeleteRequest(intent.getStringExtra("user_id"), i + "", responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(BombDepo.this);
                                        queue.add(bombDelete);

                                        BombDeleteRequest_Room bombDeleteFromRoom = new BombDeleteRequest_Room(intent.getStringExtra("user_id"), i + "", responseListener);
                                        queue = Volley.newRequestQueue(BombDepo.this);
                                        queue.add(bombDeleteFromRoom);
                                    }
                                    i++;
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });
    }
}
