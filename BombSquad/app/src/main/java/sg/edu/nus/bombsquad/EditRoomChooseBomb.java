package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import sg.edu.nus.bombsquad.Global;
import sg.edu.nus.bombsquad.R;

public class EditRoomChooseBomb extends AppCompatActivity {
    Global global = Global.getInstance();
    boolean[] selected = new boolean[100000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room_choose_bomb);
        display();
        addToRoom();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        Intent back = new Intent(EditRoomChooseBomb.this, EditRoom.class);
        back.putExtra("user_id", intent.getStringExtra("user_id"));
        back.putExtra("room_code", intent.getStringExtra("room_code"));
        back.putExtra("room_name", intent.getStringExtra("room_name"));
        back.putExtra("room", intent.getStringExtra("room"));
        startActivity(back);
    }

    private void display() {
        Intent intent = getIntent();
        try {
            JSONObject bomb = new JSONObject(intent.getStringExtra("bomb"));
            LinearLayout ll = (LinearLayout) findViewById(R.id.editRoomChooseRoomScroll);
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
                        }
                    }
                });
                assert ll != null;
                ll.addView(checkbox, lp);
                i++;
            }
        } catch (JSONException e){
            //e.printStackTrace();
        }
    }

    private void addToRoom() {
        ImageButton greenTick = (ImageButton) findViewById(R.id.greenTick);
        assert greenTick != null;
        greenTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                Intent intent = getIntent();
                int i = 0;
                while (i < 100000) {
                    if (selected[i]) {
                        RequestBody postData = new FormBody.Builder()
                                .add("user_id", intent.getStringExtra("user_id"))
                                .add("room_name", intent.getStringExtra("room_name"))
                                .add("room_code", intent.getStringExtra("room_code"))
                                .add("question_id", i+"")
                                .add("deploy_status", 0+"")
                                .add("time_left", 0+"")
                                .add("player_id", 0+"")
                                .build();
                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/createRoom.php").post(postData).build();
                        client.newCall(request)
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        System.out.println("FAIL");
                                    }

                                    @Override
                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                        //Nothing
                                    }
                                });
                    }
                    i++;
                }
                Intent back = new Intent(EditRoomChooseBomb.this, ManageRoom.class);
                back.putExtra("user_id" , intent.getStringExtra("user_id"));
                back.putExtra("room", intent.getStringExtra("room"));
                startActivity(back);
            }
        });
    }
}