package sg.edu.nus.bombsquad;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

public class QuestionDetail {
    //Constants
    public static final int ID_ETANSWEROPTION_CONSTANT = 1000000;
    public static final int ID_BOPTIONA_CONSTANT = 65000000;
    public static final int ID_BOPTIONB_CONSTANT = 66000000;
    public static final int ID_BOPTIONC_CONSTANT = 67000000;
    public static final int ID_BOPTIOND_CONSTANT = 68000000;
    public static final int ID_TVTIMELEFT_CONSTANT = 2000000;
    public static final int ID_BDEFUSE_CONSTANT = 3000000;
    public static final int ID_BPASS_CONSTANT = 4000000;

    //Variables
    Context context;
    LinearLayout layout;
    TextView tvQuestionHeading;
    TextView tvQuestion;
    Button bOptionA;
    Button bOptionB;
    Button bOptionC;
    Button bOptionD;
    TextView tvTimeLeftTitle;
    TextView tvTimeLeft;
    Button bDefuse;
    Button bPass;
    int i;  //Question number
    String question_id;
    String bomb_name;
    String question_type;
    String question;
    String[] option = new String[5];
    String correctAnswer;
    String time_limit;
    String points_awarded;
    String points_deducted;
    String num_pass;
    boolean answerIsCorrect;
    CountDownTimer timer;

    /*---------- Constructor ----------*/
    public QuestionDetail(Context context, int i, String question_id, String bomb_name, String question_type, String question,
                          String option_one, String option_two, String option_three, String option_four, String correctAnswer,
                          String time_limit, String points_awarded, String points_deducted, String num_pass) {
        this.context = context;
        this.i = i;
        this.question_id = question_id;
        this.bomb_name = bomb_name;
        this.question_type = question_type;
        this.question = question;
        this.option[0] = option_one;
        this.option[1] = option_two;
        this.option[2] = option_three;
        this.option[3] = option_four;
        this.correctAnswer = correctAnswer;
        this.time_limit = time_limit;
        this.points_awarded = points_awarded;
        this.points_deducted = points_deducted;
        this.num_pass = num_pass;

        answerIsCorrect = false;
        setLayout(i);
//        setTimer();
    }


    /*---------- Setter ----------*/
    public void setLayout(int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 50);

        //Creating Id
        final int idETAnswerOption = i + ID_ETANSWEROPTION_CONSTANT;   //id for etAnswerOption
        final int idBOptionA = i + ID_BOPTIONA_CONSTANT;    //id for bOptionA
        final int idBOptionB = i + ID_BOPTIONB_CONSTANT;    //id for bOptionB
        final int idBOptionC = i + ID_BOPTIONC_CONSTANT;    //id for bOptionC
        final int idBOptionD = i + ID_BOPTIOND_CONSTANT;    //id for bOptionD
        final int idTVTimeLeft = i + ID_TVTIMELEFT_CONSTANT;   //id for tvTimeLeft
        final int idBDefuse = i + ID_BDEFUSE_CONSTANT;  //id for bDefuse
        final int idBPass = i + ID_BPASS_CONSTANT;    //id for bPass

        //Container
        LinearLayout innerLL = new LinearLayout(context);
        innerLL.setOrientation(LinearLayout.VERTICAL);
        innerLL.setBackgroundResource(R.drawable.white_border_transparent_background);
        innerLL.setPadding(15, 15, 15, 50);
        innerLL.setId(i);
        innerLL.setLayoutParams(lp);


        //Question - Heading_TextView
        TextView tvQuestionHeading = new TextView(context);
        tvQuestionHeading.setText("Question " + (i+1));
        tvQuestionHeading.setTextSize(20);
        tvQuestionHeading.setTextColor(Color.WHITE);
        tvQuestionHeading.setPadding(15, 5, 2, 2);
        this.tvQuestionHeading = tvQuestionHeading;

        //Question & Answer_Option - LinearLayout
        LinearLayout questionLL = new LinearLayout(context);
        questionLL.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        qlp.setMargins(30,0,30,20);
        questionLL.setLayoutParams(qlp);

        //Question - Actual Question_TextView
        final TextView tvQuestion = new TextView(context);
        tvQuestion.setText(question);
        tvQuestion.setTextSize(20);
        questionLL.addView(tvQuestion);
        this.tvQuestion = tvQuestion;

        //Answer Option - TextView
        if(question_type.equals("Multiple Choice")){
            TextView tvAnswerOption = new TextView(context);
            String result = "";
            char optionLetter = 65;
            for(int j=0; j<4; j++){
                result += optionLetter + ". " + option[j] + "\n";
                optionLetter++;
            }
            tvAnswerOption.setText(result);
            tvAnswerOption.setTextSize(20);
            questionLL.addView(tvAnswerOption);
        }
        else{
            EditText etAnswerOption = new EditText(context);
            etAnswerOption.setId(idETAnswerOption);
            questionLL.addView(etAnswerOption);
        }

        //LinearLayout for MCQoptions
        LinearLayout mcqOptionsLL = new LinearLayout(context);
        mcqOptionsLL.setOrientation(LinearLayout.HORIZONTAL);
        mcqOptionsLL.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams mcqOlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mcqOlp.setMargins(0, 0, 15, 0);


        //Option A - Button
        final Button bOptionA = new Button(context);
        bOptionA.setBackgroundResource(R.drawable.white_border);
        bOptionA.setText("A");
        bOptionA.setTag(option[0]);
        bOptionA.setTextSize(20);
        bOptionA.setTextColor(Color.WHITE);
        bOptionA.setLayoutParams(mcqOlp);
        this.bOptionA = bOptionA;

        //Option B - Button
        final Button bOptionB = new Button(context);
        bOptionB.setBackgroundResource(R.drawable.white_border);
        bOptionB.setText("B");
        bOptionB.setTag(option[1]);
        bOptionB.setTextSize(20);
        bOptionB.setTextColor(Color.WHITE);
        bOptionB.setLayoutParams(mcqOlp);
        this.bOptionB = bOptionB;


        //Option C - Button
        final Button bOptionC = new Button(context);
        bOptionC.setBackgroundResource(R.drawable.white_border);
        bOptionC.setText("C");
        bOptionC.setTag(option[2]);
        bOptionC.setTextSize(20);
        bOptionC.setTextColor(Color.WHITE);
        bOptionC.setLayoutParams(mcqOlp);
        this.bOptionC = bOptionC;


        //Option D - Button
        final Button bOptionD = new Button(context);
        bOptionD.setBackgroundResource(R.drawable.white_border);
        bOptionD.setText("D");
        bOptionD.setTag(option[3]);
        bOptionD.setTextSize(20);
        bOptionD.setTextColor(Color.WHITE);
        bOptionD.setLayoutParams(mcqOlp);
        this.bOptionD = bOptionD;


        //Button A onClickListener
        bOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionA = (String)bOptionA.getTag();
                if(optionA.equals(correctAnswer)){
                    answerIsCorrect = true;
                }
                else{
                    answerIsCorrect = false;
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
                    answerIsCorrect = true;
                }
                else{
                    answerIsCorrect = false;
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
                    answerIsCorrect = true;
                }
                else{
                    answerIsCorrect = false;
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
                    answerIsCorrect = true;
                }
                else{
                    answerIsCorrect = false;
                }
                bOptionA.setBackgroundResource(R.drawable.white_border);
                bOptionB.setBackgroundResource(R.drawable.white_border);
                bOptionC.setBackgroundResource(R.drawable.white_border);
                bOptionD.setBackgroundResource(R.drawable.green_border);
            }
        });

        mcqOptionsLL.addView(bOptionA);
        mcqOptionsLL.addView(bOptionB);
        mcqOptionsLL.addView(bOptionC);
        mcqOptionsLL.addView(bOptionD);


        //Time Left (Title) - TextView
        TextView tvTimeLeftTitle = new TextView(context);
        tvTimeLeftTitle.setText("Time Left");
        tvTimeLeftTitle.setTextSize(20);
        tvTimeLeftTitle.setTextColor(Color.WHITE);
        tvTimeLeftTitle.setPadding(15, 5, 2, 2);
        this.tvTimeLeftTitle = tvTimeLeftTitle;

        //Time Left (Display countdown) - TextView
        LinearLayout.LayoutParams tvTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        tvTimeLeftLL.setMargins(30,0,30,35);
        final TextView tvTimeLeft = new TextView(context);
        tvTimeLeft.setId(i + ID_TVTIMELEFT_CONSTANT);
        tvTimeLeft.setPadding(15, 15, 12, 12);
        tvTimeLeft.setWidth(30);
        tvTimeLeft.setBackgroundResource(R.drawable.white_bg_black_border);
        tvTimeLeft.setLayoutParams(tvTimeLeftLL);
        this.tvTimeLeft = tvTimeLeft;


        //LinearLayout for defuse and pass
        LinearLayout defusePassLL = new LinearLayout(context);
        defusePassLL.setOrientation(LinearLayout.HORIZONTAL);
        defusePassLL.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams dplp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dplp.setMargins(0, 0, 40, 0);

        //Defuse - Button
        Button bDefuse = new Button(context);
        bDefuse.setId(idBDefuse);
        bDefuse.setBackgroundResource(R.drawable.green_bg_black_border);
        bDefuse.setText("Defuse");
        bDefuse.setLayoutParams(dplp);
        this.bDefuse = bDefuse;

        //Pass - Button
        Button bPass = new Button(context);
        bPass.setBackgroundResource(R.drawable.white_bg_black_border);
        bPass.setText("Pass");
        this.bPass = bPass;

        defusePassLL.addView(bDefuse);
        defusePassLL.addView(bPass);


        //Format of front-end in order
        innerLL.addView(tvQuestionHeading);
        innerLL.addView(questionLL);
        if(question_type.equals("Multiple Choice")){
            innerLL.addView(mcqOptionsLL);
        }
        innerLL.addView(tvTimeLeftTitle);
        innerLL.addView(tvTimeLeft);
        innerLL.addView(defusePassLL);

        layout = innerLL;
    }

    public void setTvQuestionHeading(TextView tvQuestionHeading) { this.tvQuestionHeading = tvQuestionHeading; }

    public void setTvQuestion(TextView tvQuestion) {
        this.tvQuestion = tvQuestion;
    }

    public void setbOptionA(Button bOptionA) {
        this.bOptionA = bOptionA;
    }

    public void setbOptionB(Button bOptionB) {
        this.bOptionB = bOptionB;
    }

    public void setbOptionC(Button bOptionC) {
        this.bOptionC = bOptionC;
    }

    public void setbOptionD(Button bOptionD) {
        this.bOptionD = bOptionD;
    }

    public void setTvTimeLeftTitle(TextView tvTimeLeftTitle) { this.tvTimeLeftTitle = tvTimeLeftTitle; }

    public void setTvTimeLeft(TextView tvTimeLeft) {
        this.tvTimeLeft = tvTimeLeft;
    }

    public void setbDefuse(Button bDefuse) {
        this.bDefuse = bDefuse;
    }

    public void setbPass(Button bPass) {
        this.bPass = bPass;
    }

    public void setQuestionNum(int i) {
        this.i = i;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public void setBomb_name(String bomb_name) {
        this.bomb_name = bomb_name;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOption(String[] option) {
        this.option = option;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public void setPoints_awarded(String points_awarded) {
        this.points_awarded = points_awarded;
    }

    public void setPoints_deducted(String points_deducted) { this.points_deducted = points_deducted; }

    public void setNum_pass(String num_pass) {
        this.num_pass = num_pass;
    }

    public void setAnswerIsCorrect(boolean answerIsCorrect) { this.answerIsCorrect = answerIsCorrect; }

    public void setTimer() {
        long timeInitial = Long.valueOf(time_limit)*1000;
        final CountDownTimer timer = new CountDownTimer(timeInitial, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimeLeft.setText(millisUntilFinished / 1000 + "");
                tvTimeLeft.setTag("RUNNING");
                System.out.println("Timer for qn " + (i + 1) + " is " + tvTimeLeft.getTag());
            }

            @Override
            public void onFinish() {
                tvTimeLeft.setText("BOOM");
                tvTimeLeft.setTag("FINISHED");
                System.out.println("Timer for qn " + (i + 1) + " has " + tvTimeLeft.getTag() + " counting down");
            }

        };
        this.timer = timer;
    }






    /*---------- Getter ----------*/
    public LinearLayout getLayout() { return layout; }
    public TextView getTvQuestionHeading() { return tvQuestionHeading; }
    public TextView getTvQuestion() { return tvQuestion; }
    public Button getbOptionA() { return bOptionA; }
    public Button getbOptionB() { return bOptionB; }
    public Button getbOptionC() { return bOptionC; }
    public Button getbOptionD() { return bOptionD; }
    public TextView getTvTimeLeftTitle() { return tvTimeLeftTitle; }
    public TextView getTvTimeLeft() { return tvTimeLeft; }
    public Button getbDefuse() { return bDefuse; }
    public Button getbPass() { return bPass; }
    public int getQuestionNum() { return i; }
    public String getQuestion_id() { return question_id; }
    public String getBomb_name() { return bomb_name; }
    public String getQuestion_type() { return question_type; }
    public String getQuestion() { return question; }
    public String[] getOption() { return option; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getTime_limit() { return time_limit; }
    public String getPoints_awarded() { return points_awarded; }
    public String getPoints_deducted() { return points_deducted; }
    public String getNum_pass() { return num_pass; }
    public boolean getAnswerIsCorrect() { return answerIsCorrect; }
    public CountDownTimer getTimer() { return timer; }
}
