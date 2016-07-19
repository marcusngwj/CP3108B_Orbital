package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class HostSelection extends AppCompatActivity {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler1 = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler2 = Executors.newSingleThreadScheduledExecutor();
    final ScheduledExecutorService scheduler3 = Executors.newSingleThreadScheduledExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_selection);
        Intent intent = getIntent();
        String question_id = intent.getStringExtra("question_id");
        String room_code = intent.getStringExtra("room_code");
        new GetPlayerInRoom().execute((room_code+""));
        deployToRandom(room_code, question_id);
        deployToSelected(room_code, question_id);
    }


    private void deployToRandom(String roomCode, String questionId) {

        final Global global = Global.getInstance();
        final String room_code = roomCode;
        final String question_id = questionId;
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayer);
        assert bRandomPlayer != null;
        bRandomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduler2.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        if (global.getBooleanAccess()) {
                            final String[] player_id = global.getPlayerId();
                            int random = (int) Math.floor(Math.random() * (player_id.length));
                            String randomPlayer = player_id[random];
                            Intent intent = new Intent(HostSelection.this, HostView.class);
                            global.setBooleanAccess(false);
                            OkHttpClient client = new OkHttpClient();
                            RequestBody postData = new FormBody.Builder()
                                    .add("room_code", room_code)
                                    .add("question_id", question_id)
                                    .add("player_id", randomPlayer)
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
                                            Intent intent = new Intent(HostSelection.this, HostView.class);
                                            startActivity(intent);
                                        }
                                    });
                            scheduler2.shutdown();
                            startActivity(intent);
                        }
                    }
                }, 0, 500, TimeUnit.MILLISECONDS);
            }
        });
    }

    private void deployToSelected(String roomCode, String questionId) {
        final Global global = Global.getInstance();
        final String room_code = roomCode;
        final String question_id = questionId;
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayer);
        assert bSelectPlayer != null;
        bSelectPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    scheduler1.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            if (global.getBooleanAccess()) {
                                Intent intent = new Intent(HostSelection.this, PlayerList.class);
                                intent.putExtra("room_code", room_code);
                                intent.putExtra("question_id", question_id);
                                global.setBooleanAccess(false);
                                scheduler1.shutdown();
                                startActivity(intent);

                            }
                        }
                    }, 0, 500, TimeUnit.MILLISECONDS);
            }
        });

    }

    class GetPlayerInRoom extends AsyncTask<String, Void, Void> {
        final Global global = Global.getInstance();

        protected Void doInBackground(String... codes) {
            OkHttpClient client = new OkHttpClient();
            RequestBody postData = new FormBody.Builder().add("room_code", codes[0]).build();
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

                                global.setPlayerId(new String[result.length()-2]);
                                global.setPlayerList(new String[result.length()-2]);
                                String[] player_id = global.getPlayerId();
                                while (i < result.length()-1) {
                                    player_id[i-1] = result.getJSONObject(i+"").getString("player");
                                    i++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            global.setBooleanVar(true);
                        }
                    });
            return null;
        }

        protected void onPostExecute(Void update) {

            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (global.getBooleanVar()) {
                        new GetPlayerList().execute();
                        global.setBooleanVar(false);
                        scheduler.shutdown();
                    }
                }
            }, 0, 500, TimeUnit.MILLISECONDS);

        }
    }

    class GetPlayerList extends AsyncTask<Void, Void, Void> {
        final Global global = Global.getInstance();
        String[] player_id = global.getPlayerId();
        String[] player_list = global.getPlayerList();
        protected Void doInBackground(Void... unused) {
            int i = 0;
            while (i < player_id.length) {
                final String currPlayer = player_id[i];
                final int curr = i;
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
                                    String first_name = result.getString("first_name");
                                    String last_name = result.getString("last_name");
                                    player_list[curr] = first_name+ " " + last_name;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                global.setBooleanAccess(true);
                            }
                        });
                i++;
            }
            return null;
        }
    }
}





