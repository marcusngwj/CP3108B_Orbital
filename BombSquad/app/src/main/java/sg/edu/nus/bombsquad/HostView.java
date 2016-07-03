package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        LinearLayout outerLL = (LinearLayout) findViewById(R.id.linearLayoutHostView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int i = 0;
        while (i < 2) {
            LinearLayout innerLL = new LinearLayout(this);
            innerLL.setBackgroundResource(R.drawable.white_border_transparent_background);
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            innerLL.setId(i);

            //Question
            TextView tvQuestion = new TextView(this);
            tvQuestion.setText("Question");
            tvQuestion.setPadding(15, 5, 2, 2);

            //In possession of
            TextView tvInPossessionOf = new TextView(this);
            tvInPossessionOf.setText("In possession of");
            tvInPossessionOf.setPadding(15, 5, 2, 2);

            //Time Left
            TextView tvTimeLeft = new TextView(this);
            tvTimeLeft.setText("Time Left");
            tvTimeLeft.setPadding(15, 5, 2, 2);

            EditText etTimeLeft = new EditText(this);
            etTimeLeft.setPadding(15, 5, 2, 2);

            innerLL.addView(tvQuestion);
            innerLL.addView(tvInPossessionOf);
            innerLL.addView(tvTimeLeft);
            innerLL.addView(etTimeLeft);

            outerLL.addView(innerLL, lp);

            i++;

        }

    }
}