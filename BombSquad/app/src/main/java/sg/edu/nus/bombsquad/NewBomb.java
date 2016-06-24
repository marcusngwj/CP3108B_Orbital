package sg.edu.nus.bombsquad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class NewBomb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bomb);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerQuestionType);
    }
}
