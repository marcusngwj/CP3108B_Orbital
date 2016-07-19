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
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);

        final Intent intent = getIntent();
        final String room_code = intent.getStringExtra("room_code");
        final String room_name = intent.getStringExtra("room_name");
        final int numQuestion = Integer.valueOf(intent.getStringExtra("numQuestion"));
//        final Global global = Global.getInstance();

        global.setRoom_code(room_code);
        final String[] questionIDArray = global.getQuestion_id();    //Array that contains all the question_ids

        String[][] stringArr = new String[numQuestion][7];  //Initialising String2DArray in global
        global.setData(stringArr);  //Initialising String2DArray in global

        System.out.println("ROOM CODE: " + room_code);
        System.out.println("ROOM NAME: " + room_name);
        System.out.println("NUM QUESTION: " + numQuestion);


        display(room_code, room_name, numQuestion, questionIDArray);

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
    private LinearLayout createQuestionBox(LinearLayout.LayoutParams lp, int i, String[] questionIDArray, String[][] createQnBoxArr){
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
        EditText etIPO = new EditText(this);
        etIPO.setPadding(15, 15, 12, 12);
        etIPO.setWidth(30);
        etIPO.setBackgroundResource(R.drawable.white_bg_black_border);
        etIPO.setLayoutParams(etIPOLL);

        //Time Left - TextView
        TextView tvTimeLeft = new TextView(this);
        tvTimeLeft.setText("Time Left");
        tvTimeLeft.setTextSize(20);
        tvTimeLeft.setTextColor(Color.WHITE);
        tvTimeLeft.setPadding(15, 5, 2, 2);

        //Time Left - EditText
        LinearLayout.LayoutParams etTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        etTimeLeftLL.setMargins(30, 0, 30, 35);
        EditText etTimeLeft = new EditText(this);
        etTimeLeft.setPadding(15, 15, 12, 12);
        etTimeLeft.setWidth(30);
        etTimeLeft.setBackgroundResource(R.drawable.white_bg_black_border);
        etTimeLeft.setLayoutParams(etTimeLeftLL);

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
                Intent intent = new Intent(HostView.this, HostSelection.class);
                String question_id = (String) bDeploy.getTag();
                System.out.println("question_id: " + question_id);
                intent.putExtra("question_id", question_id);
                intent.putExtra("room_code", global.getRoom_code());
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
        innerLL.addView(etIPO);
        innerLL.addView(tvTimeLeft);
        innerLL.addView(etTimeLeft);
        innerLL.addView(defuseDetonateLL);

        return innerLL;
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