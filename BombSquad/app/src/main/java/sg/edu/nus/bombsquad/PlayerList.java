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
        display();

    }
    private void display() {
        Intent intent = getIntent();
        final String room_code = intent.getStringExtra("room_code");
        final String question_id = intent.getStringExtra("question_id");
        final Global global = Global.getInstance();
        final String[] player_id = global.getPlayerId();
        final Queue player_list = global.getPlayerList();

        LinearLayout ll = (LinearLayout) findViewById(R.id.player_list);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int i = 0;
        while (i < player_id.length) {
            final String currPlayer = player_id[i];
            Button player = new Button(this);
            player.setText(player_list.poll().toString());
            player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody postData = new FormBody.Builder()
                            .add("room_code", room_code)
                            .add("question_id", question_id)
                            .add("player_id", currPlayer)
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
                                    System.out.println("PASSED BOMB!");
                                }
                            });
                }
            });
            assert ll != null;
            ll.addView(player, lp);
            i++;
        }
    }
}
