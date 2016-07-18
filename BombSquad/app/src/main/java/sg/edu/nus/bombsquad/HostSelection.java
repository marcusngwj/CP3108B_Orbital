package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HostSelection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_selection);
        Intent intent = getIntent();
        String question_id = intent.getStringExtra("question_id");
        String room_code = intent.getStringExtra("room_code");

        deployToRandom(room_code, question_id);
        deployToSelected(room_code, question_id);
    }

    private void deployToRandom(String room_code, String question_id) {
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayer);
        assert bRandomPlayer != null;
        bRandomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void deployToSelected(String room_code, String question_id) {
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayer);
    }
}
