package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ManageRoom extends AppCompatActivity {
    final boolean[] selected = new boolean[100000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_room);
        display();
        deleteRoom();
        startRoom();
    }

    private void display() {
        try {
            Intent intent = getIntent();
            JSONObject room = new JSONObject(intent.getStringExtra("room"));
            LinearLayout ll = (LinearLayout) findViewById(R.id.manageRoomScroll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0;
            while (i < room.length()) {
                CheckBox checkbox = (CheckBox)((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox,null);
                checkbox.setText(room.getJSONObject(i+"").getString("room_name"));
                checkbox.setId(Integer.parseInt(room.getJSONObject(i+"").getString("room_id")));
                checkbox.setTextSize(25);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected[v.getId()]) {
                            System.out.println(v.getId());
                            selected[v.getId()] = false;
                        }
                        else {
                            selected[v.getId()] = true;
                        }
                    }
                });
                assert ll != null;
                ll.addView(checkbox, lp);
                i++;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void deleteRoom() {
        final Intent intent = getIntent();
        ImageButton redCross = (ImageButton)findViewById(R.id.RMredCross);
        assert redCross != null;
        redCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = 0;
                while (j < 100000) {
                    if (selected[j]) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent backIntent = new Intent(ManageRoom.this, RoomType.class);
                                backIntent.putExtra("room", intent.getStringExtra("room"));
                                backIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                startActivity(backIntent);
                            }
                        };
                        RoomDeleteRequest roomDelete = new RoomDeleteRequest(intent.getStringExtra("user_id"), j+"", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ManageRoom.this);
                        queue.add(roomDelete);
                    }
                    j++;
                }
            }
        });
    }

    private void startRoom() {
        final Intent intent = getIntent();
        ImageButton greenTick = (ImageButton)findViewById(R.id.RMgreenTick);
        assert greenTick != null;
        greenTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = 0;
                while (k < 100000) {
                    if (selected[k]) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent hostIntent = new Intent(ManageRoom.this, HostView.class);
                                startActivity(hostIntent);
                            }
                        };
                        GameRequest game = new GameRequest(intent.getStringExtra("user_id"), "1", intent.getStringExtra("room_id"), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ManageRoom.this);
                        queue.add(game);
                    }
                    k++;
                }

            }
        });
    }
}