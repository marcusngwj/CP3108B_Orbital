package sg.edu.nus.bombsquad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreparingPlayerView extends AppCompatActivity {
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();
    final String user_id = global.getUserId();
    final String room_code = roomBank.getRoomCode();
    final int numQuestion = roomBank.getNumQuestion();
    final ArrayList<String> questionIDList = roomBank.getQuestionIDList();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing_player_view);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: PreparingPlayerView");
        System.out.println("RoomBank.roomname: " + roomBank.getRoomName());
        System.out.println("RoomBank.roomcode: " + roomBank.getRoomCode());


        Task task = new Task(); //Extends Asynctask
        task.execute();


    }

    class Task extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PreparingPlayerView.this);
            dialog.setTitle("Please wait...");
            dialog.setMessage("Preparing questions...");
            dialog.setCancelable(false);
            dialog.show();

            ArrayList<QuestionDetail> questionDetailList = new ArrayList<QuestionDetail>();
            roomBank.setQuestionDetailList(questionDetailList);


        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            global.setCounter(0);

            int i = 0;
            while (i < numQuestion) {
                System.out.println("qnID: " + questionIDList.get(i));
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            ArrayList<QuestionDetail> questionDetailList = roomBank.getQuestionDetailList();

                            System.out.println(jsonResponse);

                            int i = global.getCounter();

//                            String question_id = jsonResponse.getJSONObject(0+"").getString("question_id");   //Used to be this, but suddenly this way cannot work
                            String question_id = jsonResponse.getString("question_id");
                            String bomb_name = jsonResponse.getString("bomb_name");
                            String question_type = jsonResponse.getString("question_type");
                            String question = jsonResponse.getString("question");
                            String option_one = jsonResponse.getString("option_one");
                            String option_two = jsonResponse.getString("option_two");
                            String option_three = jsonResponse.getString("option_three");
                            String option_four = jsonResponse.getString("option_four");
                            String correctAnswer = jsonResponse.getString("answer");
                            String time_limit = jsonResponse.getString("time_limit");
                            String points_awarded = jsonResponse.getString("points_awarded");
                            String points_deducted = jsonResponse.getString("points_deducted");
                            String num_pass = jsonResponse.getString("num_pass");

                            questionDetailList.add(new QuestionDetail(PreparingPlayerView.this, i, question_id, bomb_name, question_type,
                                    question, option_one, option_two, option_three, option_four, correctAnswer, time_limit, points_awarded,
                                    points_deducted, num_pass));

                            roomBank.setQuestionDetailList(questionDetailList);

                            global.setCounter(++i);

                            System.out.println("i: " + i);
                            System.out.println("numQn: " + numQuestion);

                            if(i==numQuestion){
                                dialog.dismiss();
                                Intent intent = new Intent(PreparingPlayerView.this, PlayerView.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }
                    }
                };
                QuestionAnswerOptionRequest questionAnswerOptionRequest = new QuestionAnswerOptionRequest(questionIDList.get(i), responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(PreparingPlayerView.this);
                requestQueue.add(questionAnswerOptionRequest);

                i++;
            }
        }


    }
}
