package sg.edu.nus.bombsquad;

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

//        display();

        bombPassPlayerSelectionLL = (LinearLayout)findViewById(R.id.LinearLayoutBombPassPlayerSelection);

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Background().execute();
            }
        }, 0, 2500, TimeUnit.MILLISECONDS);

    }

//    private void display(){



        /*scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {

            }
        }, 0, 2500, TimeUnit.MILLISECONDS);*/

//    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }

    class Background extends AsyncTask<Void, Void, Void> {
        ArrayList<String> playersInGameList;

        protected void onPreExecute(Void pre) {
        }

        protected Void doInBackground(Void... param) {
            playersInGameList = roomBank.getPlayersInGameList();
            return null;
        }

        protected void onPostExecute(Void update) {
            if(playersInGameList!=null) {
                int numPlayers = playersInGameList.size();

                System.out.println("NUMPLAYERS: " + numPlayers);


                for (int i = 0; i < numPlayers; i++) {
                    String player_id = playersInGameList.get(i);

                    Button bPlayer = new Button(BombPassPlayerSelection.this);
                    bPlayer.setText(player_id);

                    bombPassPlayerSelectionLL.addView(bPlayer);
                }
            }


        }
    }


}
