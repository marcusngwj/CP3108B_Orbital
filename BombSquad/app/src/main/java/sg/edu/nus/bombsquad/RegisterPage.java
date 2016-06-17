package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
                final String first_name = editFirstName.getText().toString();
                final String last_name = editLastName.getText().toString();
                final String email = editEmail.getText().toString();
                final String mobile_no = editMobileNo.getText().toString();
                final String username = editUser.getText().toString();
                final String password = editPass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(first_name, last_name, email, mobile_no, username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
                queue.add(registerRequest);
            }
        });

    }


}
