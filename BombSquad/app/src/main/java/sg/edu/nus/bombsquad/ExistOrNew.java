package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExistOrNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_or_new);

        final Button bExistingBomb = (Button) findViewById(R.id.buttonExistingBomb);
        final Button bNewBomb = (Button) findViewById(R.id.buttonNewBomb);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String roomCode = intent.getStringExtra("roomCode");

        //Button: New Bomb
        bNewBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistOrNew.this, NewBomb.class);
                intent.putExtra("userID", userID);
                intent.putExtra("roomCode", roomCode);
                ExistOrNew.this.startActivity(intent);
            }
        });
    }
}
