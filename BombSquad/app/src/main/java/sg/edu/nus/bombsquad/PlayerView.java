package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
import java.util.ArrayList;
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

public class PlayerView extends AppCompatActivity {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();
    final String user_id = global.getUserId();
    final String room_code = roomBank.getRoomCode();
    final int numQuestion = roomBank.getNumQuestion();
    final ArrayList<QuestionDetail> questionDetailList = roomBank.getQuestionDetailList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        //To avoid automatically appear android keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: PlayerView");
        System.out.println("Room Name: " + roomBank.getRoomName());
        System.out.println("Room Code: " + roomBank.getRoomCode());

        for (int i = 0; i < numQuestion; i++) {
            System.out.println("--------------------------------------------------");
            System.out.println("qnID: " + questionDetailList.get(i).getQuestion_id());
            System.out.println("qn: " + questionDetailList.get(i).getQuestion());
            System.out.println("bomb name: " + questionDetailList.get(i).getBomb_name());
            System.out.println("Initial time: " + questionDetailList.get(i).getTime_limit());
            System.out.println("Points awarded: " + questionDetailList.get(i).getPoints_awarded());
            System.out.println("Points deducted: " + questionDetailList.get(i).getPoints_deducted());
            System.out.println("Num Pass: " + questionDetailList.get(i).getNum_pass());
            System.out.println("--------------------------------------------------");
            System.out.println();
        }

        display();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }

    private void display() {
        TextView room_name = (TextView) findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
        assert room_name != null;
        room_name.setText(global.getRoomName());

        //Exit button, link to RoomType
        Button bExitPlayerView = (Button) findViewById(R.id.buttonExitPlayerView);
        bExitPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                Intent intentLeave = new Intent(PlayerView.this, RoomType.class);
                intentLeave.putExtra("user_id", global.getUserId());
                startActivity(intentLeave);
            }
        });

        //Layout of PlayerView
        LinearLayout outerLL = (LinearLayout) findViewById(R.id.playerViewLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);

        //Initial qn shown to player
        outerLL.addView(questionDetailList.get(0).getLayout(), lp);
        questionDetailList.get(0).getTimer().start();
        withinABox(0);

        //Subsequent qn follows
        for (int i = 1; i < numQuestion; i++) {
            outerLL.addView(questionDetailList.get(i).getLayout(), lp);
            questionDetailList.get(i).getLayout().setVisibility(View.GONE);

            withinABox(i);
        }



        /*scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Background().execute((room_code+""));
            }
        }, 0, 500, TimeUnit.MILLISECONDS);*/

    }

    //Things happening inside a box of question
    private void withinABox(final int i) {
        //To display timer
        final TextView tvTimeLeft = (TextView) findViewById(i + QuestionDetail.ID_TVTIMELEFT_CONSTANT);

        //Button: Defusing a bomb
        final Button bDefuse = (Button) findViewById(i + QuestionDetail.ID_BDEFUSE_CONSTANT);
        bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qnType = questionDetailList.get(i).getQuestion_type();
                String correctAnswer = questionDetailList.get(i).getCorrectAnswer();
                String timerStatus = tvTimeLeft.getTag() + "";
                String userAnswer = "";
                CountDownTimer timer = questionDetailList.get(i).getTimer();

                //Reading string from user
                EditText etAnswerOption = (EditText) findViewById(i + QuestionDetail.ID_ETANSWEROPTION_CONSTANT);
                if (etAnswerOption != null) {
                    userAnswer = etAnswerOption.getText().toString();
                    System.out.println(userAnswer);
                }

                //If answer is correct for any type of question
                if ((qnType.equals("Multiple Choice") && questionDetailList.get(i).getAnswerIsCorrect() && timerStatus.equals("RUNNING")) ||
                        (!qnType.equals("Multiple Choice") && userAnswer.equalsIgnoreCase(correctAnswer) && timerStatus.equals("RUNNING"))) {
                    timer.cancel();
                    tvTimeLeft.setText("Bomb has been successfully defused");
                    System.out.println("Bomb " + (i + 1) + " has been successfully defused");

                    //If is not last question
                    if (i < numQuestion - 1) {
                        questionDetailList.get(i).getLayout().setVisibility(View.GONE);
                        questionDetailList.get(i+1).getLayout().setVisibility(View.VISIBLE);
                        questionDetailList.get(i+1).getTimer().start();
                    }
                }

//                global.setAnswerIsCorrect(false);    //To reset user's MCQ choice after each question
            }
        });


    }


    class Background extends AsyncTask<String, Void, Void> {
        TextView uiUpdate = (TextView) findViewById(R.id.textViewPlayerMessage);
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
