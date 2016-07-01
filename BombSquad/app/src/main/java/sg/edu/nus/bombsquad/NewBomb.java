package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewBomb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bomb);

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
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
        final EditText etMCQOption1 = (EditText) findViewById(R.id.editTextMCQOption1);
        final EditText etMCQOption2 = (EditText) findViewById(R.id.editTextMCQOption2);
        final EditText etMCQOption3 = (EditText) findViewById(R.id.editTextMCQOption3);
        final EditText etMCQOption4 = (EditText) findViewById(R.id.editTextMCQOption4);

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
                    etQuestion.setVisibility(View.VISIBLE);
                    rgMCQ.setVisibility(View.VISIBLE);
                } else {
                    etQuestion.setVisibility(View.VISIBLE);
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
                assert etBombName != null;
                String bombName = etBombName.getText().toString();
                String questionType = spinner.getSelectedItem().toString();

                assert etQuestion != null;
                String question = etQuestion.getText().toString();
                assert etMCQOption1 != null;
                String option1 = etMCQOption1.getText().toString();
                assert etMCQOption2 != null;
                String option2 = etMCQOption2.getText().toString();
                assert etMCQOption3 != null;
                String option3 = etMCQOption3.getText().toString();
                assert etMCQOption4 != null;
                String option4 = etMCQOption4.getText().toString();

                assert etAnswer != null;
                String answer = etAnswer.getText().toString();
                assert etTimeLimit != null;
                String timeLimit = etTimeLimit.getText().toString();
                assert etPointsAwarded != null;
                String pointsAwarded = etPointsAwarded.getText().toString();
                assert etPointsDeducted != null;
                String pointsDeducted = etPointsDeducted.getText().toString();
                assert etNumPass != null;
                String numPass = etNumPass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(NewBomb.this, ExistOrNew.class);
                        intent.putExtra("user_id", user_id);
                        
                        NewBomb.this.startActivity(intent);
                    }
                };

                boolean[] success = {true, true, true, true, true, true, true};

                //Bomb Name verification
                if(bombName.isEmpty()){
                    etBombName.setError("Field cannot be empty");
                    success[0] = false;
                }

                //Question verification
                if(question.isEmpty()){
                    etQuestion.setError("Field cannot be empty");
                    success[1] = false;
                }

                if(questionType.equals("Multiple Choice")){
                    //Option1 Verification
                    if(option1.isEmpty()){
                        etMCQOption1.setError("Field cannot be empty");
                        success[1] = false;
                    }

                    //Option2 Verification
                    if(option2.isEmpty()){
                        etMCQOption2.setError("Field cannot be empty");
                        success[1] = false;
                    }

                    //Option3 Verification
                    if(option3.isEmpty()){
                        etMCQOption3.setError("Field cannot be empty");
                        success[1] = false;
                    }

                    //Option4 Verification
                    if(option4.isEmpty()){
                        etMCQOption4.setError("Field cannot be empty");
                        success[1] = false;
                    }
                }

                //Answer Verification
                if(answer.isEmpty()){
                    etAnswer.setError("Field cannot be empty");
                    success[2] = false;
                }
                else{
                    if(questionType.equals("Multiple Choice") && !answer.equals(option1) && !answer.equals(option2) && !answer.equals(option3) && !answer.equals(option4))
                    {
                        etAnswer.setError("Answer muut be in one of the options");
                        etAnswer.setText("");
                        success[2] = false;
                    }
                }
                
                //Time Limit Verification
                if(timeLimit.isEmpty()){
                    etTimeLimit.setError("Field cannot be empty");
                    success[3] = false;
                }

                //Points Awarded Verification
                if(pointsAwarded.isEmpty()){
                    etPointsAwarded.setError("Field cannot be empty");
                    success[4] = false;
                }

                //Points Deducted Verification
                if(pointsDeducted.isEmpty()){
                    etPointsDeducted.setError("Field cannot be empty");
                    success[5] = false;
                }

                //Number of Passes Verification
                if(numPass.isEmpty()){
                    etNumPass.setError("Field cannot be empty");
                    success[6] = false;
                }

                if(success[0] && success[1] &&success[2] && success[3] && success[4] && success[5] && success[6]) {
                    NewBombRequest newBombRequest = new NewBombRequest(bombName, questionType, question, option1, option2, option3, option4, answer, timeLimit, pointsAwarded, pointsDeducted, numPass, user_id, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(NewBomb.this);
                    queue.add(newBombRequest);
                }
            }

        });
    }
}
