package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HostSelection extends AppCompatActivity {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler1 = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler2 = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler3 = Executors.newSingleThreadScheduledExecutor();
    Global global = Global.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_selection);
        //To show on Android Monitor onCreate
        System.out.println("Activity Name: HostSelection");
        Intent intent = getIntent();
        String question_id = intent.getStringExtra("question_id");
        String time_limit = intent.getStringExtra("time_limit");
        String room_code = intent.getStringExtra("room_code");
        deployToRandom(room_code, question_id, time_limit);
        deployToSelected(room_code, question_id, time_limit);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); so that user cannot press the back button on android! YAY!
        Intent intent = getIntent();
        Intent back = new Intent(HostSelection.this, HostView.class);
        back.putExtra("room", intent.getStringExtra("room"));
        back.putExtra("user_id", intent.getStringExtra("user_id"));
        startActivity(back);
    }

    private void deployToRandom(String roomCode, String questionId, String timeLeft) {

        final String room_code = roomCode;
        final String question_id = questionId;
        final String time_left = timeLeft;
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayer);
        assert bRandomPlayer != null;
        bRandomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("room_code", room_code)
                        .build();
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
                                    if (result.getJSONObject(0+"").getBoolean("success")) {
                                        global.setPlayerId(new String[result.length()-1]);
                                        String[] player_id = global.getPlayerId();
                                        int i = 1;
                                        while (i < result.length()-1) {
                                            player_id[i] = result.getJSONObject(i+"").getString("player");
                                            i++;
                                        }
                                        int random = (int) Math.ceil(Math.random() * (player_id.length-1));
                                        System.out.println("RANDOM: " + random);
                                        String randomPlayer = player_id[random];
                                        System.out.println("PLAYER: " + randomPlayer);
                                        OkHttpClient client = new OkHttpClient();
                                        RequestBody postData = new FormBody.Builder()
                                                .add("room_code", room_code)
                                                .add("question_id", question_id)
                                                .add("player_id", randomPlayer)
                                                .add("time_left", time_left)
                                                .build();
                                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/hostDeployBomb.php").post(postData).build();
                                        client.newCall(request)
                                                .enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        System.out.println("FAIL");
                                                    }
                                                    @Override
                                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                        Intent intent = getIntent();
                                                        Intent hostIntent = new Intent(HostSelection.this, HostView.class);
                                                        hostIntent.putExtra("room", intent.getStringExtra("room"));
                                                        hostIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                                        global.setDeployedQ(question_id, true);
                                                        new UpdateTime().execute(time_left);
                                                        startActivity(hostIntent);
                                                    }
                                                });
                                    }
                                } catch (JSONException e) {
                                    Handler h = new Handler(Looper.getMainLooper());
                                    h.post(new Runnable() {
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HostSelection.this);
                                            builder.setMessage("There are no players in the room")
                                                    .setNegativeButton("ok", null)
                                                    .create()
                                                    .show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    private void deployToSelected(String roomCode, String questionId, String timeLeft) {
        final Global global = Global.getInstance();
        final String room_code = roomCode;
        final String question_id = questionId;
        final String time_left = timeLeft;
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayer);
        assert bSelectPlayer != null;
        bSelectPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("room_code", room_code)
                        .build();
                Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getPlayer.php").post(postData).build();
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
                                    if (result.getJSONObject(0+"").getBoolean("success")) {
                                        global.setPlayerId(new String[result.length()-1]);
                                        global.setPlayerList(new String[result.length()-1]);
                                        int i = 1;
                                        String[] player_id = global.getPlayerId();
                                        String[] player_list = global.getPlayerList();
                                        while (i < result.length()-1) {
                                            player_id[i] = result.getJSONObject(i+"").getString("player");
                                            player_list[i] = result.getJSONObject(i+"").getString("first_name") + " "
                                                    + result.getJSONObject(i+"").getString("last_name");
                                            global.pushPlayerInRoom(player_id[i], player_list[i]);
                                            i++;
                                        }
                                        Intent intent = getIntent();
                                        Intent intent1 = new Intent(HostSelection.this, PlayerList.class);
                                        intent1.putExtra("room_code", room_code);
                                        intent1.putExtra("question_id", question_id);
                                        intent1.putExtra("time_left", time_left);
                                        intent1.putExtra("room", intent.getStringExtra("room"));
                                        intent1.putExtra("user_id", intent.getStringExtra("user_id"));
                                        startActivity(intent1);
                                    }
                                } catch (JSONException e) {
                                    Handler h = new Handler(Looper.getMainLooper());
                                    h.post(new Runnable() {
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HostSelection.this);
                                            builder.setMessage("There are no players in the room")
                                                    .setNegativeButton("ok", null)
                                                    .create()
                                                    .show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    class UpdateTime extends AsyncTask<String, Void, Void> {

        final Global global = Global.getInstance();
        protected Void doInBackground(String... times) {
            final String time = times[0];
            global.setTimeLeft(Integer.parseInt(time) + 1);
            scheduler3.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (global.getTimeLeft() > 0) {
                        global.setTimeLeft(global.getTimeLeft() - 1);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody postData = new FormBody.Builder()
                                .add("room_code", global.getRoomCode())
                                .add("question_id", global.getCurrQuestionId())
                                .add("time_left", Integer.toString(global.getTimeLeft()))
                                .build();
                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updateTimeLeft.php").post(postData).build();
                        client.newCall(request)
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        System.out.println("FAIL");
                                    }

                                    @Override
                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                        response.body().close();
                                    }
                                });
                    }
                    if (global.getTimeLeft() <= 0) {
                        global.undeployQ(global.getCurrQuestionId());
                        scheduler3.shutdown();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
            return null;
        }
    }
}





