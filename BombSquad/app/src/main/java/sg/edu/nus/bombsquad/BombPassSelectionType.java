package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class BombPassSelectionType extends AppCompatActivity {
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_pass_selection_type);

        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: BombPassSelectionType");

        passToRandom();
        passToSelected();
    }

    //Pass bomb to random player
    private void passToRandom(){
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayerBPST);
    }


    //Pass bomb to selected player
    private void passToSelected(){
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayerBPST);
        bSelectPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPTS = new Intent(BombPassSelectionType.this, BombPassPlayerSelection.class);
                startActivity(intentPTS);
            }
        });

    }
}
