package sg.edu.nus.bombsquad;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PlayerList extends AppCompatActivity {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: PlayerList");

        display();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed(); so that user cannot press the back button on android! YAY!
        Intent intent = getIntent();
        Intent back = new Intent(PlayerList.this, HostSelection.class);
        back.putExtra("room_code", intent.getStringExtra("room_code"));
        back.putExtra("question_id", intent.getStringExtra("question_id"));
        back.putExtra("time_limit", intent.getStringExtra("time_left"));
        back.putExtra("user_id", intent.getStringExtra("user_id"));
        back.putExtra("room", intent.getStringExtra("room"));
        startActivity(back);
    }

    private void display() {
        Intent intent = getIntent();
        final String room_code = intent.getStringExtra("room_code");
        final String question_id = intent.getStringExtra("question_id");
        final String time_left = intent.getStringExtra("time_left");
        final Global global = Global.getInstance();
        final String[] player_id = global.getPlayerId();
        final String[] player_list = global.getPlayerList();

        LinearLayout ll = (LinearLayout) findViewById(R.id.player_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int i = 0;
        while (i < player_id.length) {
            final String currPlayer = player_id[i];
            Button player = new Button(this);
            player.setText(player_list[i]);
            player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody postData = new FormBody.Builder()
                            .add("room_code", room_code)
                            .add("question_id", question_id)
                            .add("player_id", currPlayer)
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
                                    new UpdateTime().execute(time_left);
                                    global.setDeployedQ(question_id, true);
                                    Intent intent = new Intent(PlayerList.this, HostView.class);
                                    startActivity(intent);
                                }
                            });
                }
            });
            assert ll != null;
            ll.addView(player, lp);
            i++;
        }
    }

    class UpdateTime extends AsyncTask<String, Void, Void> {
        final Global global = Global.getInstance();
        protected Void doInBackground(String... times) {
            final String time = times[0];
            global.setTimeLeft(Integer.parseInt(time) + 1);
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    global.setTimeLeft(global.getTimeLeft()-1);
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
                    if (global.getTimeLeft() <= 0) {
                        global.undeployQ(global.getCurrQuestionId());
                        scheduler.shutdown();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
            return null;
        }
    }
}
