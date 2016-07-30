package sg.edu.nus.bombsquad;

import android.app.ProgressDialog;
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
import org.w3c.dom.Text;

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
    ProgressDialog dialog;

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
        TextView tvRoomCode = (TextView) findViewById(R.id.textViewPlayerViewRoomCode);
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

        dialog = new ProgressDialog(PlayerView.this);

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                prepareDisplay();
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

    private void prepareDisplay(){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", roomBank.getRoomCode())
                .add("my_id", global.getUserId())
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getRoomDetail.php").post(postData).build();

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
                            System.out.println(result);

                            String score = result.getString("total_score");

                            String question_id="";
                            String time_left="";
                            String player_id="";
                            String num_pass="";
                            String question_status="";

                            int countNumDeploy = 0;
                            for (int i = 0; i < numQuestion; i++) {
                                String deploy_status = result.getJSONObject(i + "").getString("deploy_status");
                                Integer deployStatusIntegerValue = Integer.valueOf(deploy_status);

                                //A question will only be displayed when its deploy status is more than 0
                                if (deployStatusIntegerValue > 0) {
                                    countNumDeploy++;
                                    question_id = result.getJSONObject(i + "").getString("question_id");
                                    time_left = result.getJSONObject(i + "").getString("time_left");
                                    player_id = result.getJSONObject(i + "").getString("player_id");
                                    num_pass = result.getJSONObject(i + "").getString("num_pass");
                                    question_status = result.getJSONObject(i + "").getString("question_status");
                                }

                            }

                            if(countNumDeploy==1){
                                if(dialog.isShowing()){ dialog.dismiss(); }

                                int numPassIntegerValue = Integer.valueOf(num_pass);
                                if(numPassIntegerValue==0) { QuestionDetail.updateQuestionStatus(room_code, question_id, QuestionDetail.REACHED_UPPER_LIMIT_OF_PASSES+"");}

                                QuestionDetail qnDetail = questionHashMap.get(question_id);
                                display(qnDetail, player_id, time_left, numPassIntegerValue, question_status, score);
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.setTitle("Waiting for host to deploy a question");
                                        dialog.setCancelable(false);
                                        dialog.show();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }


                    }
                });

    }

    private void display(final QuestionDetail qnDetail, final String idOfPlayerWithBomb, final String time_left, final int numPassIntegerValue, final String question_status, final String score){
        final String question_id = qnDetail.getQuestion_id();
        final String qnType = qnDetail.getQuestion_type();
        final String actualQuestion = qnDetail.getQuestion();
        final String[] mcqOptions = qnDetail.getOption();

        final int timeLeftIntegerValue = Integer.valueOf(time_left);
        final int questionStatusIntegerValue = Integer.valueOf(question_status);

        final TextView tvActualScore = (TextView)findViewById(R.id.textViewActualScore);
        final TextView tvActualQuestion = (TextView)findViewById(R.id.textViewActualQuestion);
        final TextView tvMcqOptionsDisplay = (TextView)findViewById(R.id.textViewMcqOptionsDisplay);
        final EditText etAnswerOpenEnded = (EditText)findViewById(R.id.editTextAnswerOpenEnded);
        final LinearLayout mcqOptionSelectableBlockLL = (LinearLayout)findViewById(R.id.linearLayoutMcqOptionSelectableBlock);
        final Button bOptionA = (Button)findViewById(R.id.buttonOptionA);
        final Button bOptionB = (Button)findViewById(R.id.buttonOptionB);
        final Button bOptionC = (Button)findViewById(R.id.buttonOptionC);
        final Button bOptionD = (Button)findViewById(R.id.buttonOptionD);

        final LinearLayout possessionBlockLL = (LinearLayout)findViewById(R.id.linearLayoutPossessionBlock);
        final TextView tvNameOfPlayerWithBomb = (TextView)findViewById(R.id.textViewNameOfPlayerWithBomb);

        final TextView tvTimeLeft = (TextView)findViewById(R.id.textViewTimeLeftDisplay);

        final Button bDefuse = (Button)findViewById(R.id.buttonDefuse);
        final Button bPass = (Button)findViewById(R.id.buttonPass);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Score display
                tvActualScore.setText(score);

                //Question and answer
                tvActualQuestion.setText(actualQuestion);
                if(qnType.equals("Multiple Choice")) {
                    String result = "A. " + mcqOptions[0] + "\nB. " + mcqOptions[1] + "\nC. " + mcqOptions[2] + "\nD. " + mcqOptions[3];
                    tvMcqOptionsDisplay.setText(result);
                    bOptionA.setTag(mcqOptions[0]);
                    bOptionB.setTag(mcqOptions[1]);
                    bOptionC.setTag(mcqOptions[2]);
                    bOptionD.setTag(mcqOptions[3]);
                }
                else {
                    tvMcqOptionsDisplay.setText("");
                }

                //Variations on display depending on user_id
                if(user_id.equals(idOfPlayerWithBomb)) {
                    if(qnType.equals("Multiple Choice")) {
                        mcqOptionSelectableBlockLL.setVisibility(View.VISIBLE);
                        etAnswerOpenEnded.setVisibility(View.GONE);
                        mcqButtonManipulation(bOptionA, bOptionB, bOptionC, bOptionD, qnDetail);
                    }
                    else {
                        mcqOptionSelectableBlockLL.setVisibility(View.GONE);
                        etAnswerOpenEnded.setVisibility(View.VISIBLE);
                    }
                    possessionBlockLL.setVisibility(View.GONE);
                    bDefuse.setVisibility(View.VISIBLE);
                    bPass.setVisibility(View.VISIBLE);
                }
                else {
                    mcqOptionSelectableBlockLL.setVisibility(View.GONE);
                    etAnswerOpenEnded.setVisibility(View.GONE);

                    possessionBlockLL.setVisibility(View.VISIBLE);
                    getPlayerName(idOfPlayerWithBomb, tvNameOfPlayerWithBomb);

                    bDefuse.setVisibility(View.GONE);
                    bPass.setVisibility(View.GONE);
                }

                setTimeLeftDisplay(qnDetail, timeLeftIntegerValue, numPassIntegerValue, questionStatusIntegerValue, tvTimeLeft, bDefuse, bPass, etAnswerOpenEnded, idOfPlayerWithBomb);
            }
        });
    }

    private void mcqButtonManipulation(final Button bOptionA, final Button bOptionB, final Button bOptionC, final Button bOptionD, final QuestionDetail qnDetail){
        final String correctAnswer = qnDetail.getCorrectAnswer();
        //Button A onClickListener
        bOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionA = (String)bOptionA.getTag();
                if(optionA.equals(correctAnswer)){
                    qnDetail.setMcqAnswer("correct");
                }
                else{
                    qnDetail.setMcqAnswer("wrong");
                }
                bOptionA.setBackgroundResource(R.drawable.green_border);
                bOptionB.setBackgroundResource(R.drawable.white_border);
                bOptionC.setBackgroundResource(R.drawable.white_border);
                bOptionD.setBackgroundResource(R.drawable.white_border);
            }
        });

        //Button B onClickListener
        bOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionB = (String)bOptionB.getTag();
                if(optionB.equals(correctAnswer)){
                    qnDetail.setMcqAnswer("correct");
                }
                else{
                    qnDetail.setMcqAnswer("wrong");
                }
                bOptionA.setBackgroundResource(R.drawable.white_border);
                bOptionB.setBackgroundResource(R.drawable.green_border);
                bOptionC.setBackgroundResource(R.drawable.white_border);
                bOptionD.setBackgroundResource(R.drawable.white_border);
            }
        });

        //Button C onClickListener
        bOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionC = (String)bOptionC.getTag();
                if(optionC.equals(correctAnswer)){
                    qnDetail.setMcqAnswer("correct");
                }
                else{
                    qnDetail.setMcqAnswer("wrong");
                }
                bOptionA.setBackgroundResource(R.drawable.white_border);
                bOptionB.setBackgroundResource(R.drawable.white_border);
                bOptionC.setBackgroundResource(R.drawable.green_border);
                bOptionD.setBackgroundResource(R.drawable.white_border);
            }
        });

        //Button D onClickListener
        bOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionD = (String)bOptionD.getTag();
                if(optionD.equals(correctAnswer)){
                    qnDetail.setMcqAnswer("correct");
                }
                else{
                    qnDetail.setMcqAnswer("wrong");
                }
                bOptionA.setBackgroundResource(R.drawable.white_border);
                bOptionB.setBackgroundResource(R.drawable.white_border);
                bOptionC.setBackgroundResource(R.drawable.white_border);
                bOptionD.setBackgroundResource(R.drawable.green_border);
            }
        });
    }

    private void getPlayerName(String player_id, final TextView tvInPossessionOfBomb) {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("player_id", player_id)
                .build();
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
                            System.out.println(result);
                            final String player_name = result.getString("first_name") + " " + result.getString("last_name");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvInPossessionOfBomb.setText(player_name);
                                }
                            });

                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }
                    }
                });
    }

    private void setTimeLeftDisplay(QuestionDetail qnDetail, int timeLeftIntegerValue, int numPassIntegerValue, int questionStatusIntegerValue, TextView tvTimeLeft, Button bDefuse, Button bPass, TextView etAnswerOption, String player_id){
        if(questionStatusIntegerValue==QuestionDetail.BOMB_HAS_BEEN_DEFUSED){
            tvTimeLeft.setText("Bomb has been successfully defused");
            bDefuse.setEnabled(false);
            bPass.setEnabled(false);
        }
        else if(questionStatusIntegerValue==QuestionDetail.BOMB_HAS_EXPLODED){
            tvTimeLeft.setText("THE BOMB HAS EXPLODED");
            bDefuse.setEnabled(false);
            bPass.setEnabled(false);
        }
        else if(timeLeftIntegerValue>0 && qnDetail.getAttemptedThisQuestion() && user_id.equals(player_id) && numPassIntegerValue>0){
            BombPassSelectionType.passBombToRandomPlayer(user_id, room_code, qnDetail.getQuestion_id());
        }
        //If time is not up
        else if(timeLeftIntegerValue>0 && !qnDetail.getAttemptedThisQuestion()){
            //Question is not answered and numPass more than 0
            if(qnDetail.getFinalAnswer().isEmpty() && numPassIntegerValue>0) {
                tvTimeLeft.setText(timeLeftIntegerValue + "");    //Display timer; grabbed from server; live
                bDefuseOnClick(bDefuse, bPass, etAnswerOption, qnDetail, tvTimeLeft, numPassIntegerValue);
                bPassOnClick(bPass, qnDetail);
            }
            else if(qnDetail.getFinalAnswer().isEmpty() && numPassIntegerValue<=0){
                tvTimeLeft.setText(timeLeftIntegerValue + "");
                bPass.setEnabled(false);    //No more pass left, cannot pass
                bDefuseOnClick(bDefuse, bPass, etAnswerOption, qnDetail, tvTimeLeft, numPassIntegerValue);
            }
        }
        //When the time is up
        else if(timeLeftIntegerValue<=0){
            QuestionDetail.updateQuestionStatus(room_code, qnDetail.getQuestion_id(), QuestionDetail.BOMB_HAS_EXPLODED+"");
            bDefuse.setEnabled(false);
            bPass.setEnabled(false);

            //No need consider positive case because it has been taken care of
            if((qnDetail.getFinalAnswer().isEmpty() && global.getUserId().equals(player_id))|| tvTimeLeft.getText().toString().equals("YOU FAILED THIS QUESTION")){
                tvTimeLeft.setText("YOU FAILED THIS QUESTION");
            }
            else{
                tvTimeLeft.setText("THE BOMB HAS EXPLODED");
            }
        }
    }

    private void checkAnswer(QuestionDetail qnDetail, String userAnswer){
        String qnType = qnDetail.getQuestion_type();
        String correctAnswer = qnDetail.getCorrectAnswer();

        if ((qnType.equals("Multiple Choice") && qnDetail.getMcqAnswer().isEmpty()) || (!qnType.equals("Multiple Choice") && userAnswer.isEmpty())){
            qnDetail.setFinalAnswer("");
        }
        else if((qnType.equals("Multiple Choice") && qnDetail.getMcqAnswer().equals("correct")) || (!qnType.equals("Multiple Choice") && userAnswer.equals(correctAnswer))) {
            qnDetail.setFinalAnswer("correct");
        }
        else {
            qnDetail.setFinalAnswer("wrong");
        }
    }

    private void updateScore(String answerStatus, String points){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("answer_status", answerStatus)
                .add("points", points)
                .add("user_id", user_id)
                .add("room_code", room_code)
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updateScore.php").post(postData).build();

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
                            System.out.println(result);


                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                        response.body().close();


                    }
                });
    }

    private void bDefuseOnClick(final Button bDefuse, final Button bPass, final TextView etAnswerOption, final QuestionDetail qnDetail, final TextView tvTimeLeft, final int numPassIntegerValue){
        final String points_awarded = qnDetail.getPoints_awarded();
        final String points_deducted = qnDetail.getPoints_deducted();
        bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = "";
                if(etAnswerOption!=null) {
                    userAnswer = etAnswerOption.getText().toString();
                }
                checkAnswer(qnDetail, userAnswer);
                if(qnDetail.getFinalAnswer().isEmpty()){
                    Toast.makeText(getApplicationContext(), "You have not answered the question", Toast.LENGTH_SHORT).show();
                }
                else if(qnDetail.getFinalAnswer().equals("correct")){
                    updateScore("correct", points_awarded);
                    tvTimeLeft.setText("Bomb has been successfully defused");
                    QuestionDetail.updateQuestionStatus(room_code, qnDetail.getQuestion_id(), QuestionDetail.BOMB_HAS_BEEN_DEFUSED+"");
                    bDefuse.setEnabled(false);
                    bPass.setEnabled(false);
                }
                else{
                    updateScore("wrong", points_deducted);
                    tvTimeLeft.setText("YOU FAILED THIS QUESTION");
                    if(numPassIntegerValue>0){
                        QuestionDetail.updateQuestionStatus(room_code, qnDetail.getQuestion_id(), QuestionDetail.PLAYER_FAILED_THIS_QUESTION+"");
                        BombPassSelectionType.passBombToRandomPlayer(user_id, room_code, qnDetail.getQuestion_id());
                    }
                    else{
                        QuestionDetail.updateQuestionStatus(room_code, qnDetail.getQuestion_id(), QuestionDetail.BOMB_HAS_EXPLODED+"");
                    }
                    bDefuse.setEnabled(false);
                    bPass.setEnabled(false);
                }
                qnDetail.setAttemptedThisQuestion(true);
            }
        });
    }

    private void bPassOnClick(final Button bPass, final QuestionDetail qnDetail){
        bPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBPST = new Intent(PlayerView.this, BombPassSelectionType.class);
                roomBank.setCurrentQuestion(qnDetail.getQuestion_id());
                startActivity(intentBPST);
            }
        });
    }

}


