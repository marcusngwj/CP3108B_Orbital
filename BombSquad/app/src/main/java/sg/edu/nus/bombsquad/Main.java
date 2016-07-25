package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: Main");

        //To be removed and replaced with splash screen
        Button buttonToNext=(Button)findViewById(R.id.buttonMain);
        if (buttonToNext != null) {
            buttonToNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newPage = new Intent(v.getContext(), LoginPage.class);
                    startActivity(newPage);
                }
            });
        }
    }

    public void onBackPressed() {
    }

}
