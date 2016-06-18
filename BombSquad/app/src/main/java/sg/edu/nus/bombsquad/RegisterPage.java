package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


/*import android.support.v7.app.AlertDialog;
import org.json.JSONException;
import org.json.JSONObject;*/

public class RegisterPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final EditText editFirstName = (EditText) findViewById(R.id.editTextFirstName);
        final EditText editLastName = (EditText) findViewById(R.id.editTextLastName);
        final EditText editEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editMobileNo = (EditText) findViewById(R.id.editTextMobileNo);
        final EditText editUser = (EditText) findViewById(R.id.editTextUserRegister);
        final EditText editPass = (EditText) findViewById(R.id.editTextPassRegister);
        final EditText editPassConfirm = (EditText) findViewById(R.id.editTextPassRegisterConfirm);
        final Button bRegister = (Button) findViewById(R.id.buttonRegisterPage);

        assert bRegister != null;
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = editFirstName.getText().toString();
                String last_name = editLastName.getText().toString();
                final String email = editEmail.getText().toString();
                final String mobile_no = editMobileNo.getText().toString();
                final String username = editUser.getText().toString();
                final String password = editPass.getText().toString();
                final String passConfirm = editPassConfirm.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                RegisterPage.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */

                        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                        RegisterPage.this.startActivity(intent);
                    }
                };

                boolean[] success = {false, false, true, true, true};

                //To capitalise first character in the first name
                Character firstChar = first_name.charAt(0);
                if(Character.isLetter(firstChar)){
                    first_name = Character.toUpperCase(firstChar) + first_name.substring(1);
                    success[0] = true;
                }
                else{
                    editFirstName.setError("Name has to begin with an alphabet");
                }

                //To capitalise first character in the last name
                firstChar = last_name.charAt(0);
                if(Character.isLetter(firstChar)){
                    last_name = Character.toUpperCase(firstChar) + last_name.substring(1);
                    success[1] = true;
                }
                else{
                    editLastName.setError("Name has to begin with an alphabet");
                }

                //Simple Email verification
                if(email.startsWith("@") || email.endsWith("@") || !email.contains("@")){
                    editEmail.setError("Missing local part or domain");
                    success[2] = false;
                }

                //Simple Mobile verfication
                if(mobile_no.startsWith("-") || mobile_no.substring(1).contains("+")){
                    editMobileNo.setError("Incorrect mobile number");
                    success[3] = false;
                }

                //To ensure password more than 8 characters
                if(password.length()<8){
                    editPass.setError("Password must be more than 8 characters");
                    editPass.setText("");
                    editPassConfirm.setText("");
                    success[4] = false;
                }

                //To check if password matches password confirmation
                if(!password.equals(passConfirm)){
                    editPassConfirm.setError("Password does not match");
                    editPass.setText("");
                    editPassConfirm.setText("");
                    success[4] = false;
                }

                if(success[0] && success[1] && success[2] && success[3] && success[4]) {
                    RegisterRequest registerRequest = new RegisterRequest(first_name, last_name, email, mobile_no, username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
                    queue.add(registerRequest);
                }

            }
        });

    }

}
