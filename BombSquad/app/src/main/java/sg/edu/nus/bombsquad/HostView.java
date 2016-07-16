package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);
        display();
    }

    private void display() {
        final Intent intent = getIntent();
        final String room_code = intent.getStringExtra("room_code");
        final String room_name = intent.getStringExtra("room_name");
        final Global global = Global.getInstance();
        global.setRoom_code(room_code);


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                new Background().execute();

                //For now only can do 1 qn id
                if (global.getQuestion_id() != null) {
//                    System.out.println("QNID: " + global.getQuestion_id());
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);


        //Display room id
        TextView tvHostViewBattlefieldRoomName = (TextView) findViewById(R.id.textViewHostViewBattlefieldRoomName);
        tvHostViewBattlefieldRoomName.setText(room_name + "");

        //Outer Container
        LinearLayout outerLL = (LinearLayout) findViewById(R.id.linearLayoutHostView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);

        //While loop to display all questions in a selected room
        //For now, this is a dummy one which only generates 2 qns
        int i = 0;
        while (i < 1) {
            System.out.println(global.getQuestion_id()[i]);
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
            LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            qlp.setMargins(30, 0, 30, 20);
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

            //Defuse - Button
            Button bDefuse = new Button(this);
            bDefuse.setBackgroundResource(R.drawable.green_bg_black_border);
            bDefuse.setText("Defuse");
            bDefuse.setLayoutParams(ddlp);

            //Detonate - Button
            Button bDetonate = new Button(this);
            bDetonate.setBackgroundResource(R.drawable.red_bg_black_border);
            bDetonate.setText("Detonate");

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

            outerLL.addView(innerLL, lp);

            i++;
        }
    }

    class Background extends AsyncTask<Void, Void, Void> {
        final Global global = Global.getInstance();
        final String[] question_id = global.getQuestion_id();
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
                                int i = 0;
                                //This while loop creates a lot of android run time warning(?).... will look into how to improve later...
                                while (i < jsonResponse.length()) {
                                    if (jsonResponse.getJSONObject(i + "").getBoolean("success")) {
                                        question_id[i] = (jsonResponse.getJSONObject(i + "").getString("question_id"));
                                    }
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
            if (question_id != null) {
                global.setQuestion_id(global.getQuestion_id());
            }
        }
    }
}



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