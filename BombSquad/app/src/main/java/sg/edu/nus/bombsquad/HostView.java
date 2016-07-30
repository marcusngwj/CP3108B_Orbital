package sg.edu.nus.bombsquad;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HostView extends AppCompatActivity {
    Global global = Global.getInstance();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);

        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Global global = Global.getInstance();
        final String room_code = global.getRoomCode();
        final String room_name = global.getRoomName();
        final int numQuestion = global.getNumQuestion();
        final String[] questionIDArray = global.getQuestion_id();    //Array that contains all the question_ids
        global.setRoomCode(room_code);
        global.setHostViewPossession(new TextView[numQuestion]);
        global.setQnStatusCode(new int[numQuestion]);
        global.setTvTimeLefts(new TextView[numQuestion]);
        global.setPlayerPossessBomb(new int[numQuestion]);
        global.setTvQnStat(new TextView[numQuestion]);
        global.setTimeLefts(new int[numQuestion]);


        //To show on Android Monitor onCreate
        System.out.println("Activity Name: HostView");
        System.out.println("ROOM CODE: " + room_code);
        System.out.println("ROOM NAME: " + room_name);
        System.out.println("NUM QUESTION: " + numQuestion);

        display(room_code, room_name, numQuestion, questionIDArray);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HostView.this);
        builder.setMessage("Pressing back closes the room. All players will be kicked out of the room.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeRoom();
                        Intent intent = getIntent();
                        Intent back = new Intent(HostView.this, ManageRoom.class);
                        back.putExtra("room", intent.getStringExtra("room"));
                        back.putExtra("user_id", intent.getStringExtra("user_id"));
                        global.clearDeployed();
                        global.setTimeLeft(0);
                        scheduler.shutdown();
                        startActivity(back);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    protected void onStop() {
        super.onStop();
        global.setUpdateHostViewBoolean(false);
        scheduler.shutdown();
    }

    //When closing a room
    private void closeRoom(){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", global.getRoomCode())
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/closeRoom.php").post(postData).build();
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
    }

    //To display the entire layout of hostview
    private void display(String room_code, String room_name, int numQuestion, String[] questionIDArray) {
        //Display room code
        TextView tvRoomCode = (TextView)findViewById(R.id.textViewHostViewRoomCode);
        tvRoomCode.setText("Room Code: " + room_code);

        //Display room id
        TextView tvHostViewBattlefieldRoomName = (TextView) findViewById(R.id.textViewHostViewBattlefieldRoomName);
        assert tvHostViewBattlefieldRoomName != null;
        tvHostViewBattlefieldRoomName.setText(room_name);

        Button exit = (Button)findViewById(R.id.buttonEndBattle);
        assert exit != null;
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Outer Container
        LinearLayout outerLL = (LinearLayout) findViewById(R.id.linearLayoutHostView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);

        getQuestionsData(numQuestion, questionIDArray, outerLL, lp);

    }

    //Main purpose: To retrieve question data from database
    public void getQuestionsData(int numQuestion, final String[] questionIDArray, final LinearLayout outerLL, final LinearLayout.LayoutParams lp) {
        int i = 0;
        while (i < numQuestion) {
            outerLL.addView(createQuestionBox(lp, i, questionIDArray, global.getString2DArr()));
            if (i == numQuestion-1) {
                global.setRunScheduler(true);
            }
            i++;
        }
    }

    //To create an individual box that contain details of a particular question
    private LinearLayout createQuestionBox(LinearLayout.LayoutParams lp, int i, final String[] questionIDArray, final String[][] createQnBoxArr) {
        final int idx = i;
        TextView[] possession = global.getHostViewPossession();
        TextView[] tvTimeLefts = global.getTvTimeLefts();
        TextView[] tvQnStat = global.getTvQnStat();

        //Inner container
        LinearLayout innerLL = new LinearLayout(this);
        innerLL.setOrientation(LinearLayout.VERTICAL);
        innerLL.setBackgroundResource(R.drawable.white_border_transparent_background);
        innerLL.setPadding(15, 15, 15, 50);
        innerLL.setId(i);
        innerLL.setLayoutParams(lp);


        //Question - Heading_TextView
        TextView tvQuestionHeading = new TextView(this);
        int questionNo = i + 1;
        tvQuestionHeading.setText("Question " + questionNo);
        tvQuestionHeading.setTextSize(20);
        tvQuestionHeading.setTextColor(Color.WHITE);
        tvQuestionHeading.setPadding(15, 5, 2, 2);

        //Question & Answer_Option - LinearLayout
        LinearLayout questionLL = new LinearLayout(this);
        questionLL.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        qlp.setMargins(30, 0, 30, 20);
        questionLL.setLayoutParams(qlp);

        //Question - Actual Question_TextView
        TextView tvQuestion = new TextView(this);
        String qn = createQnBoxArr[i][1];
        tvQuestion.setText(qn);
        tvQuestion.setTextSize(20);


        //Answer Option - TextView
        TextView tvAnswerOption = new TextView(this);
        String result = "";
        String qnType = createQnBoxArr[i][0];
        if (qnType.equals("Multiple Choice")) {
            for (int j = 2; j < 6; j++) {
                result += createQnBoxArr[i][j] + "\n";
            }
            result += "Answer: " + createQnBoxArr[i][6];
        } else {
            result = "Answer: " + createQnBoxArr[i][6];
        }
        tvAnswerOption.setText(result);
        tvAnswerOption.setTextSize(20);

        questionLL.addView(tvQuestion);
        questionLL.addView(tvAnswerOption);


        //In possession of - TextView
        TextView tvInPossessionOf = new TextView(this);

        tvInPossessionOf.setText("In possession of bomb");
        tvInPossessionOf.setTextSize(20);
        tvInPossessionOf.setTextColor(Color.WHITE);
        tvInPossessionOf.setPadding(15, 5, 2, 2);

        //In possession of - EditText
        LinearLayout.LayoutParams etIPOLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etIPOLL.setMargins(30, 0, 30, 20);
        possession[i] = new TextView(this);
        possession[i].setPadding(15, 15, 12, 12);
        possession[i].setWidth(30);
        possession[i].setBackgroundResource(R.drawable.white_bg_black_border);
        possession[i].setLayoutParams(etIPOLL);

        //Time Left - TextView
        TextView tvTimeLeft = new TextView(this);
        tvTimeLeft.setText("Time Left");
        tvTimeLeft.setTextSize(20);
        tvTimeLeft.setTextColor(Color.WHITE);
        tvTimeLeft.setPadding(15, 5, 2, 2);

        //Time Left - Display countdown timing - textview
        LinearLayout.LayoutParams etTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etTimeLeftLL.setMargins(30, 0, 30, 35);
        tvTimeLefts[i] = new TextView(this);
        tvTimeLefts[i].setPadding(15, 15, 12, 12);
        tvTimeLefts[i].setWidth(30);
        tvTimeLefts[i].setBackgroundResource(R.drawable.white_bg_black_border);
        tvTimeLefts[i].setLayoutParams(etTimeLeftLL);

        //Question status - header
        TextView tvQuestionStatus = new TextView(this);
        tvQuestionStatus.setText("Question Status");
        tvQuestionStatus.setTextSize(20);
        tvQuestionStatus.setTextColor(Color.WHITE);
        tvQuestionStatus.setPadding(15, 5, 2, 2);

        //Question status - body
        LinearLayout.LayoutParams etQnStatLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etQnStatLL.setMargins(30, 0, 30, 35);
        tvQnStat[i] = new TextView(this);
        tvQnStat[i].setPadding(15, 15, 12, 12);
        tvQnStat[i].setWidth(30);
        tvQnStat[i].setBackgroundResource(R.drawable.white_bg_black_border);
        tvQnStat[i].setLayoutParams(etQnStatLL);



        //LinearLayout for defuse and detonate
        LinearLayout defuseDetonateLL = new LinearLayout(this);
        defuseDetonateLL.setOrientation(LinearLayout.HORIZONTAL);
        defuseDetonateLL.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams ddlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ddlp.setMargins(0, 0, 40, 0);

        //Deploy - Button (So host will see all the question in the room and then choose to deploy it from here)
        final Button bDeploy = new Button(this);
        bDeploy.setBackgroundResource(R.drawable.white_bg_black_border);
        bDeploy.setText("Deploy");
        bDeploy.setTag(questionIDArray[i]);
        bDeploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question_id = (String) bDeploy.getTag();
                if (global.isDeployedQ(question_id)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HostView.this);
                    builder.setMessage("Bomb already deployed!")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                } else if (global.existDeployedQ()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HostView.this);
                    builder.setMessage("There is already a deployed bomb!")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                } else {
                    Intent intent = getIntent();
                    Intent intentDeploy = new Intent(HostView.this, HostSelection.class);
                    intentDeploy.putExtra("question_id", question_id);
                    global.setCurrQuestionId(question_id);
                    intentDeploy.putExtra("time_limit", global.getTimeLimit(question_id));
                    intentDeploy.putExtra("room_code", global.getRoomCode());
                    intentDeploy.putExtra("room", intent.getStringExtra("room"));
                    intentDeploy.putExtra("user_id", intent.getStringExtra("user_id"));
                    HostView.this.startActivity(intentDeploy);
                }
            }
        });
        bDeploy.setLayoutParams(ddlp);

        //Defuse - Button
        Button bDefuse = new Button(this);
        bDefuse.setBackgroundResource(R.drawable.green_bg_black_border);
        bDefuse.setText("Defuse");
        bDefuse.setLayoutParams(ddlp);
        bDefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("room_code", global.getRoomCode())
                        .add("question_id", questionIDArray[idx])
                        .add("question_status", "2")
                        .build();
                Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updateQuestionStatus.php").post(postData).build();
                client.newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println("FAIL");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                global.undeployQ(questionIDArray[idx]);
                                global.setTimeLeft(1);
                                global.getPlayerPossessBomb()[idx] = 0; //To remove the bomb from player possession
                            }
                        });
            }
        });
        //Detonate - Button
        Button bDetonate = new Button(this);
        bDetonate.setBackgroundResource(R.drawable.red_bg_black_border);
        bDetonate.setText("Detonate");
        bDetonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                RequestBody postData = new FormBody.Builder()
                        .add("room_code", global.getRoomCode())
                        .add("question_id", questionIDArray[idx])
                        .add("question_status", "3")
                        .build();
                Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/updateQuestionStatus.php").post(postData).build();
                client.newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println("FAIL");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                global.undeployQ(questionIDArray[idx]);
                                global.setTimeLeft(1);
                            }
                        });

            }
        });

        defuseDetonateLL.addView(bDeploy);
        defuseDetonateLL.addView(bDefuse);
        defuseDetonateLL.addView(bDetonate);

        //Format of front-end in order
        innerLL.addView(tvQuestionHeading);
        innerLL.addView(questionLL);
        innerLL.addView(tvInPossessionOf);
        innerLL.addView(possession[i]);
        innerLL.addView(tvTimeLeft);
        innerLL.addView(tvTimeLefts[i]);
        innerLL.addView(tvQuestionStatus);
        innerLL.addView(tvQnStat[i]);
        innerLL.addView(defuseDetonateLL);

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (global.getRunScheduler()) { //check if there was a response previously so that it will not run until there's a response
                    new UpdateHostView().execute();
                }
            }
        }, 0, 600, TimeUnit.MILLISECONDS);

        return innerLL;
    }

    class UpdateHostView extends AsyncTask<Void, Void, Void> {
        final TextView[] possession = global.getHostViewPossession();
        final int[] playerPossessBomb = global.getPlayerPossessBomb();
        final int[] qnStatCode = global.getQnStatusCode();
        final TextView[] tvTimeLefts = global.getTvTimeLefts();
        final TextView[] tvQnStat = global.getTvQnStat();
        final int[] timeLefts = global.getTimeLefts();

        public Void doInBackground(Void... unused) {
            global.setRunScheduler(false); //if no response, scheduler will not run
            global.setCounter1(0);

            OkHttpClient client = global.getClient();
            FormBody.Builder postDataBuilder = new FormBody.Builder();
            Request.Builder requestBuilder = global.getRBuilder();
            int i = 0;
            while (global.getCounter1() < global.getNumQuestion()) {
                final int count = global.getCounter1(); //so that the indx of arr in OnResponse is static
                RequestBody postData = postDataBuilder
                        .add("room_code", global.getRoomCode())
                        .add("question_id", global.getQuestion_id()[count])
                        .build();
                Request request = requestBuilder.url("http://orbitalbombsquad.x10host.com/updateHostView.php").post(postData).build();
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
                                    playerPossessBomb[count] = Integer.parseInt(result.getString("player_id"));
                                    timeLefts[count] = Integer.parseInt(result.getString("time_left"));
                                    qnStatCode[count] = Integer.parseInt(result.getString("question_status"));
                                    global.setRunScheduler(true); //to indicate in scheduler that a response was received
                                    global.setUpdateHostViewBoolean(true);
                                } catch (JSONException e) {
                                    //e.printStackTrace();
                                } finally {
                                    response.body().close();
                                }
                            }
                        });
                global.setCounter1(global.getCounter1() + 1);
            }
            global.setCounter1(0);
            return null;
        }

        public void onPostExecute(Void unused) {
            int i = 0;
            if (global.getUpdateHostViewBoolean()) {
                while (i < global.getNumQuestion()) {
                    possession[i].setText(global.getPlayerInRoom(Integer.toString(playerPossessBomb[i])));
                    tvTimeLefts[i].setText(Integer.toString(timeLefts[i]));
                    tvQnStat[i].setText(global.getQnStatus()[qnStatCode[i]]);
                    i++;
                }
                global.setUpdateHostViewBoolean(false);
            }
        }
    }
}