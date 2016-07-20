package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PlayerView extends AppCompatActivity{
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Global global = Global.getInstance();
    final String user_id = global.getUserId();
    final String room_code = global.getRoomCode();
    final int numQuestion = global.getNumQuestion();
    final String[] questionIDArray = global.getQuestion_id();
    final String[][] createQnBoxArr = global.getString2DArr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        System.out.println("Room Name: " + global.getRoomName());
        System.out.println("Room Code: " + global.getRoomCode());

        //while-loop FOR CHECKING ONLY
        int i=0;
        while (i < numQuestion) {
            System.out.println("qnID: " + questionIDArray[i]);
            System.out.println("qn: " + createQnBoxArr[i][1]);
            i++;
        }

        display();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }

    private void display() {
        final Intent intent = getIntent();
        
        TextView room_name = (TextView)findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
        assert room_name != null;
        room_name.setText(global.getRoomName());

        //Exit button, link to RoomType
        Button bExitPlayerView = (Button)findViewById(R.id.buttonExitPlayerView);
        bExitPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                Intent intentLeave = new Intent(PlayerView.this, RoomType.class);
                intentLeave.putExtra("user_id", intent.getStringExtra("user_id"));
                startActivity(intentLeave);
            }
        });

        LinearLayout outerLL = (LinearLayout) findViewById(R.id.playerViewLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,50);


        LinearLayout[] qnLayoutArr = global.getQuestionLayoutArray();
        int i = 0;
        while (i < numQuestion) {
            outerLL.addView(qnLayoutArr[i], lp);

            withinABox(i);

            i++;
        }



        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Background().execute((room_code+""));
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

    }

    private void withinABox(final int i){
        /*final TextView tvTimeLeft = (TextView)findViewById(i + global.getId_TVTimeLeft_constant());
        final String stringTimeLeft = tvTimeLeft.getText().toString();*/


        EditText etAnswerOption = (EditText)findViewById(i + global.getId_etAnswerOption_constant());


        Button bDefuse = (Button)findViewById(i + global.getId_BDefuse_constant());
        String userAnswer = "";
        if(etAnswerOption!=null){
            userAnswer = etAnswerOption.getText().toString();
        }
        final String finalUserAnswer = userAnswer;
        /*bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qnType = createQnBoxArr[i][0];
                String correctAnswer = createQnBoxArr[i][6];

                if(qnType.equals("Multiple Choice") && global.getAnswerIsCorrect() && !stringTimeLeft.equals("BOOM")) {
                    tvTimeLeft.setText("Bomb has been successfully defused");
                }
                else if(!qnType.equals("Multiple Choice") && finalUserAnswer.equalsIgnoreCase(correctAnswer) && !stringTimeLeft.equals("BOOM")){
                    tvTimeLeft.setText("Bomb has been successfully defused");
                }
                global.setAnswerIsCorrect(false);    //To reset after each question
            }
        });*/


    }

    class Background extends AsyncTask<String, Void, Void> {
        TextView uiUpdate = (TextView)findViewById(R.id.textViewPlayerMessage);
        final Global global = Global.getInstance();

        protected void onPreExecute(Void pre) {
            uiUpdate.setText("Waiting for host to start game...");
        }

        protected Void doInBackground(String... codes) {
            OkHttpClient client = new OkHttpClient();
            RequestBody postData = new FormBody.Builder().add("room_code", codes[0]).build();
            Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updatePlayerView.php").post(postData).build();

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
                                global.setRoomStatus(result.getString("room_status"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            return null;
        }

        protected void onPostExecute(Void update) {
            Global global = Global.getInstance();
            if (global.getRoomStatus() != null && global.getRoomStatus().equals("1")) {
                System.out.println("In Activity");
                uiUpdate.setText("In room");
            }
            if (global.getRoomStatus() != null && global.getRoomStatus().equals("0")) {
                uiUpdate.setText("Room closed");
            }
        }
    }
}

















/*
The original AsyncTask using Volley
class Background extends AsyncTask<String, Void, Void> {
        TextView uiUpdate = (TextView)findViewById(R.id.textViewPlayerMessage);
        RequestQueue queue = Volley.newRequestQueue(PlayerView.this);

        protected void onPreExecute(Void pre) {
            uiUpdate.setText("Waiting for host to start game...");
        }

        protected Void doInBackground(String... codes) {
            final Global global = Global.getInstance();
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseListener = new JSONObject(response);
                        boolean success = responseListener.getBoolean("success");
                        if (success){
                            global.setRoomStatus(responseListener.getString("room_status"));
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            UpdatePlayerViewRequest updatePlayer = new UpdatePlayerViewRequest(codes[0], responseListener);
            queue.add(updatePlayer);
            return null;
        }

        protected void onPostExecute(Void update) {
            Global global = Global.getInstance();
            if (global.getRoomStatus() != null && global.getRoomStatus().equals("1")) {
                uiUpdate.setText("In room");
            }
        }
    }
    */
