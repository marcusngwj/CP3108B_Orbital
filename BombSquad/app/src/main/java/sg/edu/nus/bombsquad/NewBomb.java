package sg.edu.nus.bombsquad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class NewBomb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bomb);

        final EditText etBombName = (EditText) findViewById(R.id.editTextBombName);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerQuestionType);

        final EditText etQuestion = (EditText) findViewById(R.id.editTextQuestion);

        final RadioGroup rgMCQ = (RadioGroup) findViewById(R.id.radioGroupMCQ);
        final EditText etShortLongAnswer = (EditText) findViewById(R.id.editTextShortLongAnswer);

        final EditText etTimeLimit = (EditText) findViewById(R.id.editTextTimeLimit);
        final EditText etPointsAwarded = (EditText) findViewById(R.id.editTextPointsAwarded);
        final EditText etPointsDeducted = (EditText) findViewById(R.id.editTextPointsDeducted);
        final EditText etNumPass = (EditText) findViewById(R.id.editTextNumPass);
        final Button bCreateBomb = (Button) findViewById(R.id.buttonCreateBomb);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.question_type, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerString = parent.getItemAtPosition(position).toString();
                if (spinnerString.equals("Multiple Choice")) {
                    rgMCQ.setVisibility(View.VISIBLE);
                    etShortLongAnswer.setVisibility(View.GONE);
                } else {
                    etShortLongAnswer.setVisibility(View.VISIBLE);
                    rgMCQ.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        assert bCreateBomb != null;
        bCreateBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bombName = etBombName.getText().toString();
                String question = etQuestion.getText().toString();
                String timeLimit = etTimeLimit.getText().toString();
                String pointsAwarded = etPointsAwarded.getText().toString();
                String pointsDeducted = etPointsDeducted.getText().toString();
                String numPass = etNumPass.getText().toString();
                String choice = spinner.getSelectedItem().toString();
            }

        });
    }
}