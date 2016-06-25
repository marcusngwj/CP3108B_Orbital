package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class NewBomb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bomb);

        Intent intent = getIntent();
        final String roomCode = intent.getStringExtra("roomCode");

        //Bomb Name
        final EditText etBombName = (EditText) findViewById(R.id.editTextBombName);

        //Question Type
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerQuestionType);

        final EditText etQuestion = (EditText) findViewById(R.id.editTextQuestion);

        //MCQ
        final RadioGroup rgMCQ = (RadioGroup) findViewById(R.id.radioGroupMCQ);
        final RadioButton rbMCQOption1 = (RadioButton) findViewById(R.id.radioButtonMCQOption1);
        final RadioButton rbMCQOption2 = (RadioButton) findViewById(R.id.radioButtonMCQOption2);
        final RadioButton rbMCQOption3 = (RadioButton) findViewById(R.id.radioButtonMCQOption3);
        final RadioButton rbMCQOption4 = (RadioButton) findViewById(R.id.radioButtonMCQOption4);

        //Short-or-Long Answer Question
        final EditText etShortLongAnswerQuestion = (EditText) findViewById(R.id.editTextShortLongAnswerQuestion);

        //Answer
        final EditText etAnswer = (EditText) findViewById(R.id.editTextAnswer);

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
                    etQuestion.setVisibility(View.VISIBLE);
                } else {
                    rgMCQ.setVisibility(View.GONE);
                    etQuestion.setVisibility(View.VISIBLE);
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
                String questionType = spinner.getSelectedItem().toString();

                String question = etQuestion.getText().toString();
                //Need to get text from radio button options

                String answer = etAnswer.getText().toString();
                String timeLimit = etTimeLimit.getText().toString();
                String pointsAwarded = etPointsAwarded.getText().toString();
                String pointsDeducted = etPointsDeducted.getText().toString();
                String numPass = etNumPass.getText().toString();
                String choice = spinner.getSelectedItem().toString();

                boolean[] success = {false, false, false, false, false, false, false};

                //Bomb Name verification
                if(bombName.isEmpty()){
                    etBombName.setError("Field cannot be empty");
                    success[0] = false;
                }
                else{
                    success[0] = true;
                }

                //Question verification
                if(questionType.equals("Multiple Choice")){
                    if(question.isEmpty()){
                        etQuestion.setError("Field cannot be empty");
                        success[1] = false;
                    }
                    else{
                        success[1] = true;
                    }
                }

                //Answer Verification
                if(answer.isEmpty()){
                    etAnswer.setError("Field cannot be empty");
                    success[2] = false;
                }
                else{
                    success[2] = true;
                }

                //Time Limit Verification
                if(timeLimit.isEmpty()){
                    etTimeLimit.setError("Field cannot be empty");
                    success[3] = false;
                }
                else{
                    success[3] = true;
                }

                //Points Awarded Verification
                if(pointsAwarded.isEmpty()){
                    etPointsAwarded.setError("Field cannot be empty");
                    success[4] = false;
                }
                else{
                    success[4] = true;
                }

                //Points Deducted Verification
                if(pointsDeducted.isEmpty()){
                    etPointsDeducted.setError("Field cannot be empty");
                    success[5] = false;
                }
                else{
                    success[5] = true;
                }

                //Number of Passes Verification
                if(numPass.isEmpty()){
                    etNumPass.setError("Field cannot be empty");
                    success[6] = false;
                }
                else{
                    success[6] = true;
                }

                if(success[0] && success[1] &&success[2] && success[3] && success[4] && success[5] && success[6]) {
                    Intent intent = new Intent(NewBomb.this, ExistOrNew.class);
                    NewBomb.this.startActivity(intent);
                }
            }

        });
    }
}
