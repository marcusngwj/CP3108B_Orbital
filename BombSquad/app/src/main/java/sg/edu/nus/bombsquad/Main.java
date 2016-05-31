package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonToNext=(Button)findViewById(R.id.buttonMain);
    }

//    To edit this part
    public void buttonToNext(){
        Intent newPage = new Intent(this, RoomType.class);
        startActivity(newPage);
    }
}
