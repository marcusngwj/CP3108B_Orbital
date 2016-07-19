package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HostSelection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_selection);
        Intent intent = getIntent();
        String question_id = intent.getStringExtra("question_id");
        String room_code = intent.getStringExtra("room_code");
        getPlayerList(room_code, question_id);
    }

    private void getPlayerList(String roomCode, String questionId){
        final String room_code = roomCode;
        final String question_id = questionId;
        final Global global = Global.getInstance();
        System.out.println("roomCode = " + room_code);
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder().add("room_code", room_code).build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getPlayerInRoom.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }
                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            int i = 1;
                            String[] test = new String[result.length()-2];
                            global.setPlayerId(test);
                            String[] player_id = global.getPlayerId();
                            while (i < result.length()-1) {
                                player_id[i-1] = result.getJSONObject(i+"").getString("player");
                                System.out.println(player_id[i-1]);
                                i++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        deployToRandom(room_code, question_id);
                        deployToSelected(room_code, question_id);
                    }
                });
    }

    private void deployToRandom(String room_code, String question_id) {
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayer);
        assert bRandomPlayer != null;
        bRandomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void deployToSelected(String roomCode, String questionID) {
        final Global global = Global.getInstance();
        final String[] player_id = global.getPlayerId();
        final String room_code = roomCode;
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayer);
        assert bSelectPlayer != null;
        bSelectPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                while (i < player_id.length) {
                    final String currPlayer = player_id[i];
                    System.out.println("currplayer = " + currPlayer);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody postData = new FormBody.Builder().add("player_id", currPlayer).build();
                    Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getPlayerName.php").post(postData).build();

                    client.newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    System.out.println("FAIL");
                                }
                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    try {
                                        JSONObject result = new JSONObject(response.body().string());
                                        //System.out.println(result);
                                        String first_name = result.getString("first_name");
                                        String last_name = result.getString("last_name");
                                        System.out.println("name = " + first_name);
                                        Queue player_list = global.getPlayerList();
                                        player_list.offer(first_name + " " + last_name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    i++;
                }
                Intent intent = new Intent(HostSelection.this, PlayerList.class);
                startActivity(intent);
            }
        });
    }
}
