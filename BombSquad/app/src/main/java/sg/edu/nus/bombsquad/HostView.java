package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
            TextView question = new TextView(this);
            question.setText("Question");
            innerLL.addView(question);
            outerLL.addView(innerLL, lp);
            i++;
        }

    }
}