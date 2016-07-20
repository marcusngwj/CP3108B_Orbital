package sg.edu.nus.bombsquad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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

public class HostView extends AppCompatActivity {
    Global global = Global.getInstance();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);

        final Global global = Global.getInstance();
        final String room_code = global.getRoomCode();
        final String room_name = global.getRoomName();
        final int numQuestion = global.getNumQuestion();
        global.setRoomCode(room_code);
        global.setHostViewPossession(new TextView[numQuestion]);
        global.setTvTimeLefts(new TextView[numQuestion]);
        global.setPlayerPossessBomb(new int[numQuestion]);
        global.setTimeLefts(new int[numQuestion]);
        final String[] questionIDArray = global.getQuestion_id();    //Array that contains all the question_ids

        String[][] stringArr = new String[numQuestion][8];  //Initialising String2DArray in global
        global.setData(stringArr);  //Initialising String2DArray in global

        System.out.println("ROOM CODE: " + room_code);
        System.out.println("ROOM NAME: " + room_name);
        System.out.println("NUM QUESTION: " + numQuestion);


        display(room_code, room_name, numQuestion, questionIDArray);

    }

    protected void onStop() {
        super.onStop();
        global.setUpdateHostViewBoolean(false);
        scheduler.shutdown();

    }


    //To display the entire layout of hostview
    private void display(String room_code, String room_name, int numQuestion, String[] questionIDArray) {
        //Display room id
        TextView tvHostViewBattlefieldRoomName = (TextView) findViewById(R.id.textViewHostViewBattlefieldRoomName);
        tvHostViewBattlefieldRoomName.setText(room_name + "");

        //Outer Container
        LinearLayout outerLL = (LinearLayout) findViewById(R.id.linearLayoutHostView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);

        getQuestionsData(numQuestion, questionIDArray, outerLL, lp);

    }


    //Main purpose: To retrieve question data from database
    public void getQuestionsData(int numQuestion, final String[] questionIDArray, final LinearLayout outerLL, final LinearLayout.LayoutParams lp){
        global.setCounter(0);

        int i = 0;
        while (i < numQuestion) {
            System.out.println("qnID: " + questionIDArray[i]);


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String[][] tempArr = global.getString2DArr();

                        int i = global.getCounter();
                        tempArr[i][0] = jsonResponse.getJSONObject(0 + "").getString("question_type");
                        tempArr[i][1] = jsonResponse.getJSONObject(0 + "").getString("question");
                        tempArr[i][2] = jsonResponse.getJSONObject(0 + "").getString("option_one");
                        tempArr[i][3] = jsonResponse.getJSONObject(0 + "").getString("option_two");
                        tempArr[i][4] = jsonResponse.getJSONObject(0 + "").getString("option_three");
                        tempArr[i][5] = jsonResponse.getJSONObject(0 + "").getString("option_four");
                        tempArr[i][6] = jsonResponse.getJSONObject(0 + "").getString("answer");
                        tempArr[i][7] = jsonResponse.getJSONObject(0 + "").getString("time_limit");

                        outerLL.addView(createQuestionBox(lp, i, questionIDArray, tempArr));
                        global.setCounter(++i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            QuestionAnswerOptionRequest questionAnswerOptionRequest = new QuestionAnswerOptionRequest(questionIDArray[i], responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(HostView.this);
            requestQueue.add(questionAnswerOptionRequest);

            i++;
        }
    }


    //To create an individual box that contain details of a particular question
    private LinearLayout createQuestionBox(LinearLayout.LayoutParams lp, int i, String[] questionIDArray, final String[][] createQnBoxArr){
        TextView[] possession = global.getHostViewPossession();
        TextView[] tvTimeLefts = global.getTvTimeLefts();
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
        if(qnType.equals("Multiple Choice")){
            for(int j=2; j<6; j++){
                result += createQnBoxArr[i][j] + "\n";
            }
        }
        else{
            result = createQnBoxArr[i][6];
        }
        tvAnswerOption.setText(result);
        tvAnswerOption.setTextSize(20);

        questionLL.addView(tvQuestion);
        questionLL.addView(tvAnswerOption);


        //In possession of - TextView
        TextView tvInPossessionOf = new TextView(this);

        tvInPossessionOf.setText("In possession of");
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

        //Time Left - EditText
        LinearLayout.LayoutParams etTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etTimeLeftLL.setMargins(30, 0, 30, 35);
        tvTimeLefts[i] = new TextView(this);
        tvTimeLefts[i].setPadding(15, 15, 12, 12);
        tvTimeLefts[i].setWidth(30);
        tvTimeLefts[i].setBackgroundResource(R.drawable.white_bg_black_border);
        tvTimeLefts[i].setLayoutParams(etTimeLeftLL);

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
        final int idx = i;
        bDeploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostView.this, HostSelection.class);
                String question_id = (String) bDeploy.getTag();
                System.out.println("question_id: " + question_id);
                intent.putExtra("question_id", question_id);
                global.setCurrQuestionId(question_id);
                intent.putExtra("time_limit", createQnBoxArr[idx][7]);
                intent.putExtra("room_code", global.getRoomCode());
                HostView.this.startActivity(intent);
            }
        });
        bDeploy.setLayoutParams(ddlp);

        //Defuse - Button
        Button bDefuse = new Button(this);
        bDefuse.setBackgroundResource(R.drawable.green_bg_black_border);
        bDefuse.setText("Defuse");
        bDefuse.setLayoutParams(ddlp);

        //Detonate - Button
        Button bDetonate = new Button(this);
        bDetonate.setBackgroundResource(R.drawable.red_bg_black_border);
        bDetonate.setText("Detonate");

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
        innerLL.addView(defuseDetonateLL);

        if (i == global.getNumQuestion()-1) {
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (global.getRunScheduler()) { //check if there was a response previously so that it will not run until there's a response
                        new UpdateHostView().execute();
                    }
                }
            }, 0, 600, TimeUnit.MILLISECONDS);
        }
        return innerLL;
    }

    class UpdateHostView extends AsyncTask<Void, Void, Void> {
        final TextView[] possession = global.getHostViewPossession();
        final int[] playerPossessBomb = global.getPlayerPossessBomb();
        final TextView[] tvTimeLefts = global.getTvTimeLefts();
        final int[] timeLefts = global.getTimeLefts();

        public Void doInBackground(Void... unused) {
            global.setRunScheduler(false); //if no response, scheduler will not run
            global.setCounter1(0);
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder postDataBuilder = new FormBody.Builder();
            Request.Builder requestBuilder = new Request.Builder();
            while (global.getCounter1() < global.getNumQuestion()) {
                final int count = global.getCounter1(); //so that the indx of arr in OnResponse is static
                RequestBody postData = postDataBuilder
                        .add("room_code", global.getRoomCode())
                        .add("question_id", global.getQuestion_id()[global.getCounter1()])
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
                                    global.setRunScheduler(true); //to indicate in scheduler that a response was received
                                    global.setUpdateHostViewBoolean(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                global.setCounter1(global.getCounter1()+1);
            }
            global.setCounter1(0);
            return null;
        }
        public void onPostExecute(Void unused) {
            int i = 0;
            if (global.getUpdateHostViewBoolean()) {
                while (i < global.getNumQuestion()) {
                    possession[i].setText(Integer.toString(playerPossessBomb[i]));
                    tvTimeLefts[i].setText(Integer.toString(timeLefts[i]));
                    i++;
                }
                global.setUpdateHostViewBoolean(false);
            }
        }
    }

}


//Old AsyncTask code
    /*class Background extends AsyncTask<Void, Void, Void> {
        final Global global = Global.getInstance();
//        final String[] question_id = global.getQuestion_id();
        protected Void doInBackground(Void... unused) {
            OkHttpClient client = new OkHttpClient();
            RequestBody postData = new FormBody.Builder().add("room_code", global.getRoom_code()).build();
            Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getQuestionID.php").post(postData).build();

            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("FAIL");
                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
//                                System.out.println("jsonResponseLength: " + jsonResponse.length());
//                                System.out.println("jsonResponse: " + jsonResponse);
                                String[] question_id = new String[jsonResponse.length()];
                                int count = 0;  //Count total number of questions in a room

                                int i = 0;
                                //This while loop creates a lot of android run time warning(?).... will look into how to improve later...
                                while (i < jsonResponse.length()) {
                                    if (jsonResponse.getJSONObject(i + "").getBoolean("success")) {
                                        question_id[i] = (jsonResponse.getJSONObject(i + "").getString("question_id"));

                                        count++;
                                    }
                                    i++;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            return null;
        }

        protected void onPostExecute(Void update) {
            Global global = Global.getInstance();
//            if (question_id != null) {
//                global.setQuestion_id(global.getQuestion_id());
//
//
//                for(int i=0; i<question_id.length; i++){
//                    System.out.println("qn id is: " + question_id[i]);
//                }*//*
//            }
        }
    }*/


//Old AsyncTask Code using volley...
/*
    //Testing using okHTTP instead of volley...
    class Background extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void... unused) {
            final Global global = Global.getInstance();
            final String[] question_id = global.getQuestion_id();
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getJSONObject(0 + "").getBoolean("success");
                        System.out.println("SUCCESS");
                        System.out.println(jsonResponse);

                        //Currently can only access 1 qn per room
                        if (success) {
                            int i = 0;
                            while (i < jsonResponse.length()) {
                                if (jsonResponse.getJSONObject(i+"").getBoolean("success")) {
                                    question_id[i] = (jsonResponse.getJSONObject(i + "").getString("question_id"));
                                }
                            }
//                            System.out.println("hello baby " + global.getQuestion_id());
                        } else {
                            System.out.println("Error");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };
            String room_code = global.getRoom_code();
            GetQuestionIDRequest getQnID = new GetQuestionIDRequest(room_code, responseListener);
            RequestQueue queue = Volley.newRequestQueue(HostView.this);
            queue.add(getQnID);
            return null;
        }

        protected void onPostExecute(Void update) {
            Global global = Global.getInstance();
            if(global.getQuestion_id()!=null){
                global.setQuestion_id(global.getQuestion_id());
            }
        }
    }
} */