package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HostView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view);
        display();
    }

    private void display(){
        final Intent intent = getIntent();
        final String room_id = intent.getStringExtra("room_id");

        TextView tvHostViewBattlefieldRoomName = (TextView) findViewById(R.id.textViewHostViewBattlefieldRoomName);
        tvHostViewBattlefieldRoomName.setText(room_id+"");

        //Outer Container
        LinearLayout outerLL = (LinearLayout) findViewById(R.id.linearLayoutHostView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,50);

        int i = 0;
        while (i < 2) {
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
            LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            qlp.setMargins(30,0,30,20);
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
            LinearLayout.LayoutParams etIPOLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            etIPOLL.setMargins(30,0,30,20);
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
            LinearLayout.LayoutParams etTimeLeftLL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            etTimeLeftLL.setMargins(30,0,30,35);
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
}