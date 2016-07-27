package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BombPassPlayerSelection extends AppCompatActivity {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();
    LinearLayout bombPassPlayerSelectionLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_pass_player_selection);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: BombPassPlayerSelection");

        bombPassPlayerSelectionLL = (LinearLayout) findViewById(R.id.LinearLayoutBombPassPlayerSelection);

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                display();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

    }

    private void display() {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", roomBank.getRoomCode())
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/playersInGame.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    boolean responded;

                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        ArrayList<String> playersInGameList = new ArrayList<String>();
                        final ArrayList<Button> playersWhoCanReceiveBombButtonList = new ArrayList<Button>();
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            System.out.println(result);

                            int numPlayers = Integer.valueOf(result.getString("numRow"));

                            for (int i = 0; i < numPlayers; i++) {
                                String player_id = result.getJSONObject(i + "").getString("player");
                                String player_firstName = result.getJSONObject(i + "").getString("first_name");
                                String player_lastName = result.getJSONObject(i + "").getString("last_name");
                                playersInGameList.add(player_id);

                                //Usually is the person who created the room, hence, all host should be same
                                //If host == -1 (default value in database), it means user is a player
                                String host = result.getJSONObject(i + "").getString("host");

                                //If user is not the person who currently got the bomb
                                // and he is not the host
                                // add to the list of players who can receive the bomb
                                if (!player_id.equals(global.getUserId()) && !player_id.equals(host)) {
                                    final Button bPlayer = new Button(BombPassPlayerSelection.this);
                                    bPlayer.setTag(player_id);
                                    bPlayer.setText(player_firstName + " " + player_lastName);

                                    bPlayer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            OkHttpClient client = new OkHttpClient();
                                            RequestBody postData = new FormBody.Builder()
                                                    .add("room_code", roomBank.getRoomCode())
                                                    .add("question_id", roomBank.getCurrentQuestion())
                                                    .add("player_id", bPlayer.getTag()+"")
                                                    .build();
                                            final Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updateBombPossession.php").post(postData).build();

                                            client.newCall(request)
                                                    .enqueue(new Callback() {
                                                        boolean responded;

                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            System.out.println("FAIL");
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                            JSONObject result = null;
                                                            try {
                                                                result = new JSONObject(response.body().string());
                                                            } catch (JSONException e) {
                                                                /*e.printStackTrace();*/
                                                            }
                                                            System.out.println("HERE IS IT!: " + result);
                                                            response.body().close();
                                                        }
                                                    });

//                                            onBackPressed();
                                            onStop();
                                            Intent intent = new Intent(BombPassPlayerSelection.this, PlayerView.class);
                                            startActivity(intent);
                                        }
                                    });

                                    playersWhoCanReceiveBombButtonList.add(bPlayer);
                                }
                            }
                            responded = true;
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }
                        if (responded) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LinearLayout tempLL = new LinearLayout(BombPassPlayerSelection.this);
                                    tempLL.setOrientation(LinearLayout.VERTICAL);
                                    for (Button bTemp : playersWhoCanReceiveBombButtonList) {
                                        tempLL.addView(bTemp);
                                    }
                                    bombPassPlayerSelectionLL.removeAllViews();
                                    bombPassPlayerSelectionLL.addView(tempLL);
                                }
                            });


                        }

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }


}
