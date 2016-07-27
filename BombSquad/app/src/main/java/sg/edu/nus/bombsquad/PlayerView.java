package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

    final ArrayList<String> questionIDList = roomBank.getQuestionIDList();
    final HashMap<String, QuestionDetail> questionHashMap = roomBank.getQuestionHashMap();
    final HashMap<String, RoomDetail> roomDetailHashMap = roomBank.getRoomDetailHashMap();

    LinearLayout outerLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //To avoid automatically appear android keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: PlayerView");
        System.out.println("Room Name: " + roomBank.getRoomName());
        System.out.println("Room Code: " + roomBank.getRoomCode());

        for (int i = 0; i < numQuestion; i++) {
            System.out.println("--------------------------------------------------");
            String qnID = questionIDList.get(i);
            System.out.println("qnID: " + questionHashMap.get(qnID).getQuestion_id());
            System.out.println("qn: " + questionHashMap.get(qnID).getQuestion());
            System.out.println("bomb name: " + questionHashMap.get(qnID).getBomb_name());
            System.out.println("Initial time: " + questionHashMap.get(qnID).getTime_limit());
            System.out.println("Points awarded: " + questionHashMap.get(qnID).getPoints_awarded());
            System.out.println("Points deducted: " + questionHashMap.get(qnID).getPoints_deducted());
            System.out.println("Num Pass: " + questionHashMap.get(qnID).getNum_pass());
            System.out.println("--------------------------------------------------");
            System.out.println();
        }

        //Display room code
        TextView tvRoomCode = (TextView)findViewById(R.id.textViewPlayerViewRoomCode);
        tvRoomCode.setText("Room Code: " + room_code);

        //Display room name
        TextView room_name = (TextView) findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
        assert room_name != null;
        room_name.setText(roomBank.getRoomName());

        //Exit button, link to RoomType
        Button bExitPlayerView = (Button) findViewById(R.id.buttonExitPlayerView);
        bExitPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduler.shutdown();

                //Remove user from "GAME" table in the database
                RoomBank.removePlayerFromGame(room_code, global.getUserId());

                Intent intentLeave = new Intent(PlayerView.this, RoomType.class);
                intentLeave.putExtra("user_id", global.getUserId());
                startActivity(intentLeave);
            }
        });

        //Layout of PlayerView
        outerLL = (LinearLayout) findViewById(R.id.playerViewLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);


        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                display();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdown();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();    //user cannot press the back button on android
    }

    private void display() {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", roomBank.getRoomCode())
                .add("my_id", global.getUserId())
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getRoomDetail.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    boolean responded;
                    String score = "";

                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        final ArrayList<LinearLayout> deployedQuestionList = new ArrayList<LinearLayout>();
                        final ArrayList<String> playerWithBombList = new ArrayList<String>();
                        final ArrayList<String> numPassList = new ArrayList<String>();
                        final TextView tvScore = (TextView) findViewById(R.id.textViewActualScore);

                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            System.out.println(result);

                            score = result.getString("total_score");

                            for (int i = 0; i < numQuestion; i++) {
                                String question_id = result.getJSONObject(i + "").getString("question_id");
                                String deploy_status = result.getJSONObject(i + "").getString("deploy_status");
                                Integer deployStatusIntegerValue = Integer.valueOf(deploy_status);
                                String time_left = result.getJSONObject(i + "").getString("time_left");
                                String player_id = result.getJSONObject(i + "").getString("player_id");
                                String num_pass = result.getJSONObject(i + "").getString("num_pass");
                                System.out.println("QUESTION_ID: " + question_id);
                                System.out.println("DEPLOY_STATUS: " + deploy_status);
                                System.out.println("NUM_PASS: " + num_pass);

                                if (deployStatusIntegerValue > 0) {
                                    LinearLayout qnLayout = questionHashMap.get(question_id).getLayout();
                                    qnLayout.setTag(time_left); //To pass to the method in " if responded"
                                    deployedQuestionList.add(qnLayout);
                                    playerWithBombList.add(player_id);
                                    numPassList.add(num_pass);
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
                                    tvScore.setText(score);
                                    outerLL.removeAllViews();
                                    int i = 0;
                                    for (LinearLayout qnLL : deployedQuestionList) {
                                        //To remove the error thrown on runtime
                                        if (qnLL != null) {
                                            ViewGroup parent = (ViewGroup) qnLL.getParent();
                                            if (parent != null) {
                                                parent.removeView(qnLL);
                                            }
                                        }

                                        outerLL.addView(qnLL);

                                        String question_id = (qnLL.getId() - QuestionDetail.ID_INNERLL_CONSTANT) + "";
                                        String time_left = qnLL.getTag().toString();
                                        String player_id = playerWithBombList.get(i);
                                        String num_pass = numPassList.get(i++);
                                        System.out.println("PLAYER_ID IS: " + player_id);
                                        withinABox(question_id, time_left, player_id, num_pass);
                                    }
                                }
                            });
                        }


                    }
                });

    }

    //Things happening inside a box of question
    private void withinABox(final String question_id, final String time_left, final String player_id, final String num_pass) {
        final int qnID = Integer.valueOf(question_id);
        final int timeLeftIntegerValue = Integer.valueOf(time_left);
        final int numPassIntegerValue = Integer.valueOf(num_pass);
        final QuestionDetail qnDetail = questionHashMap.get(question_id);

        final TextView tvTimeLeft = (TextView) findViewById(qnID + QuestionDetail.ID_TVTIMELEFT_CONSTANT);
        final EditText etAnswerOption = (EditText) findViewById(qnID + QuestionDetail.ID_ETANSWEROPTION_CONSTANT);
        final TextView tvInPossessionOfBombTitle = (TextView) findViewById(qnID + QuestionDetail.ID_TVINPOSSESSIONOFBOMBTITLE_CONSTANT);
        final TextView tvInPossessionOfBomb = (TextView) findViewById(qnID + QuestionDetail.ID_TVINPOSSESSIONOFBOMB_CONSTANT);
        final LinearLayout mcqOptionsLL = (LinearLayout) findViewById(qnID + QuestionDetail.ID_MCQOPTIONSLL_CONSTANT);

        //Button: Defusing a bomb
        final Button bDefuse = (Button) findViewById(qnID + QuestionDetail.ID_BDEFUSE_CONSTANT);
        bDefuse.setVisibility(View.GONE);
        bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qnType = qnDetail.getQuestion_type();
                String correctAnswer = qnDetail.getCorrectAnswer();
                String userAnswer = "";

                //Reading string from user
                EditText etAnswerOption = (EditText) findViewById(qnID + QuestionDetail.ID_ETANSWEROPTION_CONSTANT);
                if (etAnswerOption != null) {
                    userAnswer = etAnswerOption.getText().toString();
                    System.out.println(userAnswer);
                }

                int timeLeft = Integer.valueOf(roomDetailHashMap.get(question_id).getTimeLeft());

                //If answer is correct for any type of question
                if ((qnType.equals("Multiple Choice") && qnDetail.getAnswerIsCorrect() && timeLeft > 0) ||
                        (!qnType.equals("Multiple Choice") && userAnswer.equalsIgnoreCase(correctAnswer) && timeLeft > 0)) {
                    tvTimeLeft.setText("Bomb has been successfully defused");
                    System.out.println("Bomb has been successfully defused");
                }
            }
        });

        //Button: Passing a bomb
        final Button bPass = (Button) findViewById(qnID + QuestionDetail.ID_BPASS_CONSTANT);
        bPass.setVisibility(View.GONE);
        bPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvTimeLeft.getText().equals("YOU FAILED THIS QUESTION")){
                    Toast.makeText(getApplicationContext(), "Unable to pass", Toast.LENGTH_SHORT).show();
                }
                else if(numPassIntegerValue<=0){
                    Toast.makeText(getApplicationContext(), "You have reached the maximum number of pass", Toast.LENGTH_SHORT).show();
                }
                else {
                    //If time is not up and you have not answered correctly, you can pass the bomb,
                    // else otherwise
                    if (timeLeftIntegerValue > 0 && !tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                        onStop();
                        Intent intentBPST = new Intent(PlayerView.this, BombPassSelectionType.class);
                        roomBank.setCurrentQuestion(qnDetail.getQuestion_id());
                        startActivity(intentBPST);
                    }
                }

            }
        });


        //If user possesses the bomb, show button for defuse and pass, hide bomb possession display
        if (user_id.equals(player_id)) {
            tvInPossessionOfBombTitle.setVisibility(View.GONE);
            tvInPossessionOfBomb.setVisibility(View.GONE);
            bDefuse.setVisibility(View.VISIBLE);
            bPass.setVisibility(View.VISIBLE);

            //Question type verification
            if (questionHashMap.get(question_id).getQuestion_type().equals("Multiple Choice")) {
                mcqOptionsLL.setVisibility(View.VISIBLE);
            } else {
                etAnswerOption.setVisibility(View.VISIBLE);
            }

            //If time's up and qn is not answered correctly
            if (timeLeftIntegerValue <= 0 && !tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                tvTimeLeft.setText("YOU FAILED THIS QUESTION");
            }

            //If timer has not finished counting
            else if (!tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                tvTimeLeft.setText(timeLeftIntegerValue + "");    //Display timer; grabbed from server; live
            }

            //If qn is answered correctly tvTimeLeft will display "Bomb has been successfully defused"

        }
        //else hide both buttons, show bomb possession display
        else {
            tvInPossessionOfBombTitle.setVisibility(View.VISIBLE);
            tvInPossessionOfBomb.setVisibility(View.VISIBLE);
            tvInPossessionOfBomb.setText(player_id);

            bDefuse.setVisibility(View.GONE);
            bPass.setVisibility(View.GONE);

            //Question type verification
            if (questionHashMap.get(question_id).getQuestion_type().equals("Multiple Choice")) {
                mcqOptionsLL.setVisibility(View.GONE);
            } else {
                etAnswerOption.setVisibility(View.GONE);
            }

            //If time's up and qn is not answered correctly
            if (timeLeftIntegerValue <= 0 && !tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                tvTimeLeft.setText("THE BOMB HAS EXPLODED");
            }

            //If timer has not finished counting
            else if (!tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                tvTimeLeft.setText(timeLeftIntegerValue + "");    //Display timer; grabbed from server; live
            }
        }


    }

}


//Old code
/*private void display() {
    TextView room_name = (TextView) findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
    assert room_name != null;
    room_name.setText(global.getRoomName());

    //Exit button, link to RoomType
    Button bExitPlayerView = (Button) findViewById(R.id.buttonExitPlayerView);
    bExitPlayerView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scheduler.shutdown();

            //Remove user from "GAME" table in the database
            OkHttpClient client = new OkHttpClient();
            RequestBody postData = new FormBody.Builder()
                    .add("room_code", room_code)
                    .add("player", global.getUserId())
                    .build();
            Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/removePlayerFromGame.php").post(postData).build();

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

            Intent intentLeave = new Intent(PlayerView.this, RoomType.class);
            intentLeave.putExtra("user_id", global.getUserId());
            startActivity(intentLeave);
        }
    });

    //Layout of PlayerView
    LinearLayout outerLL = (LinearLayout) findViewById(R.id.playerViewLinearLayout);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    lp.setMargins(0, 0, 0, 50);

    //Individual questions
    for (int i = 0; i < numQuestion; i++) {
        outerLL.addView(questionDetailList.get(i).getLayout(), lp);
        questionDetailList.get(i).getLayout().setVisibility(View.GONE);

        withinABox(i);
    }

    scheduler.scheduleAtFixedRate(new Runnable() {
        public void run() {
            new Background().execute();
        }
    }, 0, 1000, TimeUnit.MILLISECONDS);

}

    //Things happening inside a box of question
    private void withinABox(final int i) {
        //To display timer
        final TextView tvTimeLeft = (TextView) findViewById(i + QuestionDetail.ID_TVTIMELEFT_CONSTANT);

        //Button: Defusing a bomb
        final Button bDefuse = (Button) findViewById(i + QuestionDetail.ID_BDEFUSE_CONSTANT);
        bDefuse.setVisibility(View.GONE);
        bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qnType = questionDetailList.get(i).getQuestion_type();
                String correctAnswer = questionDetailList.get(i).getCorrectAnswer();
                String userAnswer = "";

                //Reading string from user
                EditText etAnswerOption = (EditText) findViewById(i + QuestionDetail.ID_ETANSWEROPTION_CONSTANT);
                if (etAnswerOption != null) {
                    userAnswer = etAnswerOption.getText().toString();
                    System.out.println(userAnswer);
                }

                int timeLeft = Integer.valueOf(roomBank.getRoomDetailList().get(i).getTimeLeft(i));

                //If answer is correct for any type of question
                if ((qnType.equals("Multiple Choice") && questionDetailList.get(i).getAnswerIsCorrect() && timeLeft > 0) ||
                        (!qnType.equals("Multiple Choice") && userAnswer.equalsIgnoreCase(correctAnswer) && timeLeft > 0)) {
                    tvTimeLeft.setText("Bomb has been successfully defused");
                    System.out.println("Bomb " + (i + 1) + " has been successfully defused");
                }
            }
        });

        //Button: Passing a bomb
        final Button bPass = (Button) findViewById(i + QuestionDetail.ID_BPASS_CONSTANT);
        bPass.setVisibility(View.GONE);
        bPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBPST = new Intent(PlayerView.this, BombPassSelectionType.class);
                roomBank.setCurrentQuestion(questionDetailList.get(i).getQuestion_id());
                startActivity(intentBPST);
            }
        });


    }


class Background extends AsyncTask<Void, Void, Void> {
    String[] deployStatusArray = new String[numQuestion];
    String[] timeLeftArray = new String[numQuestion];
    String[] playerIDArray = new String[numQuestion];

    protected void onPreExecute(Void pre) {
    }

    protected Void doInBackground(Void... param) {
        System.out.println("**************************************");
        for (int i = 0; i < numQuestion; i++) {
            deployStatusArray[i] = roomDetailList.get(i).getDeployStatus(i);
            timeLeftArray[i] = roomDetailList.get(i).getTimeLeft(i);
            playerIDArray[i] = roomDetailList.get(i).getPlayerID(i);

            System.out.println(i + ":");
            System.out.println("Deploy_status: " + deployStatusArray[i]);
            System.out.println("Time_left: " + timeLeftArray[i]);
            System.out.println("Player who got the bomb: " + playerIDArray[i]);
            System.out.println("....................");
        }
        System.out.println("**************************************");
        System.out.println();
        return null;
    }

    protected void onPostExecute(Void update) {
        for (int i = 0; i < numQuestion; i++) {
            final EditText etAnswerOption = (EditText) findViewById(i + QuestionDetail.ID_ETANSWEROPTION_CONSTANT);
            final LinearLayout mcqOptionsLL = (LinearLayout) findViewById(i + QuestionDetail.ID_MCQOPTIONSLL_CONSTANT);
            final TextView tvInPossessionOfBombTitle = (TextView) findViewById(i + QuestionDetail.ID_TVINPOSSESSIONOFBOMBTITLE_CONSTANT);
            final TextView tvInPossessionOfBomb = (TextView) findViewById(i + QuestionDetail.ID_TVINPOSSESSIONOFBOMB_CONSTANT);
            final TextView tvTimeLeft = (TextView) findViewById(i + QuestionDetail.ID_TVTIMELEFT_CONSTANT);
            final Button bDefuse = (Button) findViewById(i + QuestionDetail.ID_BDEFUSE_CONSTANT);
            final Button bPass = (Button) findViewById(i + QuestionDetail.ID_BPASS_CONSTANT);

            int deployStatusIntegerValue = Integer.valueOf(deployStatusArray[i]);
            int timeLeftIntegerValue = Integer.valueOf(timeLeftArray[i]);

            //If a question is being deployed
            if (deployStatusIntegerValue > 0) {
                questionDetailList.get(i).getLayout().setVisibility(View.VISIBLE);

                //If time's up and qn is not answered correctly
                if (timeLeftIntegerValue <= 0 && !tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                    tvTimeLeft.setText("YOU FAILED THIS QUESTION");
                }

                //If timer has not finished counting
                else if (!tvTimeLeft.getText().equals("Bomb has been successfully defused")) {
                    tvTimeLeft.setText(timeLeftIntegerValue + "");    //Display timer; grabbed from server; live
                }

                //If qn is answered correctly
                //tvTimeLeft will display "Bomb has been successfully defused"
            } else {
                questionDetailList.get(i).getLayout().setVisibility(View.GONE);
            }

            //If user possesses the bomb, show button for defuse and pass, hide bomb possession display
            if (user_id.equals(playerIDArray[i])) {
                tvInPossessionOfBombTitle.setVisibility(View.GONE);
                tvInPossessionOfBomb.setVisibility(View.GONE);
                bDefuse.setVisibility(View.VISIBLE);
                bPass.setVisibility(View.VISIBLE);

                //Question type verification
                if (questionDetailList.get(i).getQuestion_type().equals("Multiple Choice")) {
                    mcqOptionsLL.setVisibility(View.VISIBLE);
                } else {
                    etAnswerOption.setVisibility(View.VISIBLE);
                }
            }
            //else hide both buttons, show bomb possession display
            else {
                tvInPossessionOfBombTitle.setVisibility(View.VISIBLE);
                tvInPossessionOfBomb.setVisibility(View.VISIBLE);
                tvInPossessionOfBomb.setText(playerIDArray[i]);

                bDefuse.setVisibility(View.GONE);
                bPass.setVisibility(View.GONE);

                //Question type verification
                if (questionDetailList.get(i).getQuestion_type().equals("Multiple Choice")) {
                    mcqOptionsLL.setVisibility(View.GONE);
                } else {
                    etAnswerOption.setVisibility(View.GONE);
                }
            }
        }


    }
}*/














    /*class Background extends AsyncTask<String, Void, Void> {
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
    }*/





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
