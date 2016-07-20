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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PreparingPlayerView extends AppCompatActivity {
    Global global = Global.getInstance();
    final String user_id = global.getUserId();
    final String room_code = global.getRoomCode();
    final int numQuestion = global.getNumQuestion();
    final String[] questionIDArray = global.getQuestion_id();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing_player_view);

        String[][] temp2DArr = new String[numQuestion][7];
        global.setData(temp2DArr);

        Task task = new Task();
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

            LinearLayout[] qnLayoutArray = new LinearLayout[numQuestion];
            global.setQuestionLayoutArray(qnLayoutArray);
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
                System.out.println("qnID: " + questionIDArray[i]);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String[][] tempArr = global.getString2DArr();
                            LinearLayout[] layoutArr = global.getQuestionLayoutArray();

                            int i = global.getCounter();
                            tempArr[i][0] = jsonResponse.getJSONObject(0 + "").getString("question_type");
                            tempArr[i][1] = jsonResponse.getJSONObject(0 + "").getString("question");
                            tempArr[i][2] = jsonResponse.getJSONObject(0 + "").getString("option_one");
                            tempArr[i][3] = jsonResponse.getJSONObject(0 + "").getString("option_two");
                            tempArr[i][4] = jsonResponse.getJSONObject(0 + "").getString("option_three");
                            tempArr[i][5] = jsonResponse.getJSONObject(0 + "").getString("option_four");
                            tempArr[i][6] = jsonResponse.getJSONObject(0 + "").getString("answer");

                            layoutArr[i] = createQuestionBox(i, questionIDArray);

                            global.setData(tempArr);
                            global.setQuestionLayoutArray(layoutArr);
                            global.setCounter(++i);


                            if(i==numQuestion){
                                dialog.dismiss();
                                Intent intent = new Intent(PreparingPlayerView.this, PlayerView.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                QuestionAnswerOptionRequest questionAnswerOptionRequest = new QuestionAnswerOptionRequest(questionIDArray[i], responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(PreparingPlayerView.this);
                requestQueue.add(questionAnswerOptionRequest);

                i++;
            }
        }

        //To create an individual box that contain details of a particular question
        private LinearLayout createQuestionBox(int i, String[] questionIDArray){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 50);

            //Inner container
            LinearLayout innerLL = new LinearLayout(PreparingPlayerView.this);
            innerLL.setOrientation(LinearLayout.VERTICAL);
            innerLL.setBackgroundResource(R.drawable.white_border_transparent_background);
            innerLL.setPadding(15, 15, 15, 50);
            innerLL.setId(i);
            innerLL.setLayoutParams(lp);


            //Question - Heading_TextView
            TextView tvQuestionHeading = new TextView(PreparingPlayerView.this);
            tvQuestionHeading.setText("Question");
            tvQuestionHeading.setTextSize(20);
            tvQuestionHeading.setTextColor(Color.WHITE);
            tvQuestionHeading.setPadding(15, 5, 2, 2);

            //Question & Answer_Option - LinearLayout
            LinearLayout questionLL = new LinearLayout(PreparingPlayerView.this);
            questionLL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            qlp.setMargins(30,0,30,20);
            questionLL.setLayoutParams(qlp);

            //Question - Actual Question_TextView
            TextView tvQuestion = new TextView(PreparingPlayerView.this);
            tvQuestion.setText("TEMP");
            tvQuestionHeading.setTextSize(20);

            //Answer Option - TextView
            TextView tvAnswerOption = new TextView(PreparingPlayerView.this);
            tvAnswerOption.setText("change");
            tvAnswerOption.setTextSize(20);

            questionLL.addView(tvQuestion);
            questionLL.addView(tvAnswerOption);

            //LinearLayout for MCQoptions
            LinearLayout mcqOptionsLL = new LinearLayout(PreparingPlayerView.this);
            mcqOptionsLL.setOrientation(LinearLayout.HORIZONTAL);
            mcqOptionsLL.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams mcqOlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mcqOlp.setMargins(0, 0, 15, 0);

            //Option A - Button
            Button bOptionA = new Button(PreparingPlayerView.this);
            bOptionA.setBackgroundResource(R.drawable.white_border);
            bOptionA.setText("A");
            bOptionA.setTextSize(20);
            bOptionA.setTextColor(Color.WHITE);
            bOptionA.setLayoutParams(mcqOlp);

            //Option B - Button
            Button bOptionB = new Button(PreparingPlayerView.this);
            bOptionB.setBackgroundResource(R.drawable.white_border);
            bOptionB.setText("B");
            bOptionB.setTextSize(20);
            bOptionB.setTextColor(Color.WHITE);
            bOptionB.setLayoutParams(mcqOlp);

            //Option C - Button
            Button bOptionC = new Button(PreparingPlayerView.this);
            bOptionC.setBackgroundResource(R.drawable.white_border);
            bOptionC.setText("C");
            bOptionC.setTextSize(20);
            bOptionC.setTextColor(Color.WHITE);
            bOptionC.setLayoutParams(mcqOlp);

            //Option D - Button
            Button bOptionD = new Button(PreparingPlayerView.this);
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
            TextView tvTimeLeftTitle = new TextView(PreparingPlayerView.this);
            tvTimeLeftTitle.setText("Time Left");
            tvTimeLeftTitle.setTextSize(20);
            tvTimeLeftTitle.setTextColor(Color.WHITE);
            tvTimeLeftTitle.setPadding(15, 5, 2, 2);

            //Time Left (Display countdown) - TextView
            LinearLayout.LayoutParams tvTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            tvTimeLeftLL.setMargins(30,0,30,35);
            final TextView tvTimeLeft = new TextView(PreparingPlayerView.this);
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
            LinearLayout defusePassLL = new LinearLayout(PreparingPlayerView.this);
            defusePassLL.setOrientation(LinearLayout.HORIZONTAL);
            defusePassLL.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams dplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dplp.setMargins(0, 0, 40, 0);

            //To be completed
            //Defuse - Button
            //Attempt to defuse: Check answer with server
            //If answer is correct, bomb is defused and timer cancelled
            //Else timer continues to countdown
            Button bDefuse = new Button(PreparingPlayerView.this);
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
            Button bPass = new Button(PreparingPlayerView.this);
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

            return innerLL;
        }
    }
}
