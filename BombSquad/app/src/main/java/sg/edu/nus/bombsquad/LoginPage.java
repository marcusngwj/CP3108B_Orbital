package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class LoginPage extends AppCompatActivity {
    Global global = Global.getInstance();
    Button bLogin,bRegister;
    EditText editUser,editPass;
    TextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: LoginPage");

        onLogin();
        onRegister();
        onForgetPassword();
    }

    public void onBackPressed() {
        /*Intent intent = new Intent(LoginPage.this, Main.class);
        startActivity(intent);*/
    }

    public void onLogin(){
        bLogin=(Button)findViewById(R.id.buttonLogin);
        editUser=(EditText)findViewById(R.id.editTextUser);
        editPass=(EditText)findViewById(R.id.editTextPass);

        //Login Request
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final String username = editUser.getText().toString();
                final String password = editPass.getText().toString();

                if(username.isEmpty()){
                    editUser.setError("Enter username");
                }
                else if(password.isEmpty()){
                    editPass.setError("Enter password");
                }
                else {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    String first_name = jsonResponse.getString("first_name");
                                    String last_name = jsonResponse.getString("last_name");
                                    String user_id = jsonResponse.getString("user_id");

                                    Intent intent = new Intent(LoginPage.this, RoomType.class);
                                    intent.putExtra("first_name", first_name);
                                    intent.putExtra("last_name", last_name);
                                    intent.putExtra("user_id", user_id);

                                    global.setUserId(user_id);

                                    LoginPage.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                    builder.setMessage("Incorrect username or password :(")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                            /*e.printStackTrace();*/
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    public void onRegister(){
        bRegister=(Button)findViewById(R.id.buttonRegister);

        //Register Button
        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    public void onForgetPassword(){
        tvForgetPassword = (TextView)findViewById(R.id.textViewForgetPassword);
        tvForgetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginPage.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }
}
