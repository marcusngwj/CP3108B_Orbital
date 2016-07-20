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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        System.out.println("name: " + global.getRoomName());
        System.out.println("roomCode: " + global.getRoomCode());

        display();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }

    private void display() {
        final Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        final String room_code = intent.getStringExtra("room_code");
        TextView room_name = (TextView)findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
        assert room_name != null;
        room_name.setText(intent.getStringExtra("room_name"));

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

        int i = 0;
        while (i < 1) {
            //Inner container
            LinearLayout innerLL = new LinearLayout(this);
            innerLL.setOrientation(LinearLayout.VERTICAL);
            innerLL.setBackgroundResource(R.drawable.white_border_transparent_background);
            innerLL.setPadding(15, 15, 15, 50);
            innerLL.setId(i);
            innerLL.setLayoutParams(lp);


            //Question - Heading_TextView
            TextView tvQuestionHeading = new TextView(this);
            tvQuestionHeading.setText("Question");
            tvQuestionHeading.setTextSize(20);
            tvQuestionHeading.setTextColor(Color.WHITE);
            tvQuestionHeading.setPadding(15, 5, 2, 2);

            //Question & Answer_Option - LinearLayout
            LinearLayout questionLL = new LinearLayout(this);
            questionLL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            qlp.setMargins(30,0,30,20);
            questionLL.setLayoutParams(qlp);

            //Question - Actual Question_TextView
            TextView tvQuestion = new TextView(this);
            tvQuestion.setText("TEMP");
            tvQuestionHeading.setTextSize(20);

            //Answer Option - TextView
            TextView tvAnswerOption = new TextView(this);
            tvAnswerOption.setText("change");
            tvAnswerOption.setTextSize(20);

            questionLL.addView(tvQuestion);
            questionLL.addView(tvAnswerOption);

            //LinearLayout for MCQoptions
            LinearLayout mcqOptionsLL = new LinearLayout(this);
            mcqOptionsLL.setOrientation(LinearLayout.HORIZONTAL);
            mcqOptionsLL.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams mcqOlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mcqOlp.setMargins(0, 0, 15, 0);

            //Option A - Button
            Button bOptionA = new Button(this);
            bOptionA.setBackgroundResource(R.drawable.white_border);
            bOptionA.setText("A");
            bOptionA.setTextSize(20);
            bOptionA.setTextColor(Color.WHITE);
            bOptionA.setLayoutParams(mcqOlp);

            //Option B - Button
            Button bOptionB = new Button(this);
            bOptionB.setBackgroundResource(R.drawable.white_border);
            bOptionB.setText("B");
            bOptionB.setTextSize(20);
            bOptionB.setTextColor(Color.WHITE);
            bOptionB.setLayoutParams(mcqOlp);

            //Option C - Button
            Button bOptionC = new Button(this);
            bOptionC.setBackgroundResource(R.drawable.white_border);
            bOptionC.setText("C");
            bOptionC.setTextSize(20);
            bOptionC.setTextColor(Color.WHITE);
            bOptionC.setLayoutParams(mcqOlp);

            //Option D - Button
            Button bOptionD = new Button(this);
            bOptionD.setBackgroundResource(R.drawable.white_border);
            bOptionD.setText("D");
            bOptionD.setTextSize(20);
            bOptionD.setTextColor(Color.WHITE);
            bOptionD.setLayoutParams(mcqOlp);

            mcqOptionsLL.addView(bOptionA);
            mcqOptionsLL.addView(bOptionB);
            mcqOptionsLL.addView(bOptionC);
            mcqOptionsLL.addView(bOptionD);


            //Time Left (Title) - TextView
            TextView tvTimeLeftTitle = new TextView(this);
            tvTimeLeftTitle.setText("Time Left");
            tvTimeLeftTitle.setTextSize(20);
            tvTimeLeftTitle.setTextColor(Color.WHITE);
            tvTimeLeftTitle.setPadding(15, 5, 2, 2);

            //Time Left (Display countdown) - TextView
            LinearLayout.LayoutParams tvTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            tvTimeLeftLL.setMargins(30,0,30,35);
            final TextView tvTimeLeft = new TextView(this);
//            tvTimeLeft.setId(View.generateViewId());
//            tvTimeLeft.getId();
            tvTimeLeft.setPadding(15, 15, 12, 12);
            tvTimeLeft.setWidth(30);
            tvTimeLeft.setBackgroundResource(R.drawable.white_bg_black_border);
            tvTimeLeft.setLayoutParams(tvTimeLeftLL);

            //Params: Total time(Need to retrieve from server, interval
            //+2000 for buffer time for transition
            final CountDownTimer timer = new CountDownTimer(5000+2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimeLeft.setText(millisUntilFinished/1000 + "");
                }

                @Override
                public void onFinish() {
                    tvTimeLeft.setText("BOOM");
                }

            }.start();


            //LinearLayout for defuse and pass
            LinearLayout defusePassLL = new LinearLayout(this);
            defusePassLL.setOrientation(LinearLayout.HORIZONTAL);
            defusePassLL.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams dplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dplp.setMargins(0, 0, 40, 0);

            //To be completed
            //Defuse - Button
            //Attempt to defuse: Check answer with server
            //If answer is correct, bomb is defused and timer cancelled
            //Else timer continues to countdown
            Button bDefuse = new Button(this);
            bDefuse.setBackgroundResource(R.drawable.green_bg_black_border);
            bDefuse.setText("Defuse");
            bDefuse.setLayoutParams(dplp);
            bDefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                    tvTimeLeft.setText("Bomb has been successfully defused");
                }
            });

            //Pass - Button
            Button bPass = new Button(this);
            bPass.setBackgroundResource(R.drawable.white_bg_black_border);
            bPass.setText("Pass");

            defusePassLL.addView(bDefuse);
            defusePassLL.addView(bPass);


            //Format of front-end in order
            innerLL.addView(tvQuestionHeading);
            innerLL.addView(questionLL);
            innerLL.addView(mcqOptionsLL);
            innerLL.addView(tvTimeLeftTitle);
            innerLL.addView(tvTimeLeft);
            innerLL.addView(defusePassLL);

            outerLL.addView(innerLL, lp);

            i++;
        }

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Background().execute((room_code+""));
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

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
