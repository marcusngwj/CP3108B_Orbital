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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditRoom extends AppCompatActivity{
    boolean[] selected = new boolean[100000];
    Global global = Global.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        System.out.println("I AM IN EDIT ROOM!");
        display();
        plus();
        minus();
    }

    private void display() {
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
                                Intent intent = getIntent();
                                Intent addRoomIntent = new Intent(EditRoom.this, EditRoomChooseBomb.class);
                                addRoomIntent.putExtra("bomb", jsonResponse.toString());
                                addRoomIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                addRoomIntent.putExtra("room_code", intent.getStringExtra("room_code"));
                                addRoomIntent.putExtra("room_name", intent.getStringExtra("room_name"));
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
        final Intent intent = getIntent();
        ImageButton redMinus = (ImageButton)findViewById(R.id.redMinus);
        assert  redMinus != null;
        redMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                int i = 0;
                while (i < 100000) {
                    if (selected[i]) {
                        System.out.println("user id = " + intent.getStringExtra("user_id"));
                        System.out.println("question_id" + i);
                        final int idx = i;
                        RequestBody postData = new FormBody.Builder()
                                .add("user_id", intent.getStringExtra("user_id"))
                                .add("question_id", i + "")
                                .build();
                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/bombDeleteFromRoom.php").post(postData).build();
                        client.newCall(request)
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        System.out.println("FAIL");
                                    }

                                    @Override
                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                        selected[idx] = false;
                                    }
                                });
                    }
                    i++;
                }
                Intent back = new Intent(EditRoom.this, ManageRoom.class);
                back.putExtra("user_id" , intent.getStringExtra("user_id"));
                back.putExtra("room", intent.getStringExtra("room"));
                startActivity(back);
            }
        });

    }
}
