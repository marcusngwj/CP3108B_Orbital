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

public class LoginPage extends AppCompatActivity {
    Button bLogin,bRegister;
    EditText editUser,editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        onLogin();
        onRegister();
    }

    public void onLogin(){
        bLogin=(Button)findViewById(R.id.buttonLogin);
        editUser=(EditText)findViewById(R.id.editTextUser);
        editPass=(EditText)findViewById(R.id.editTextPass);

        /*//Login Button (Dummy Login)
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                if(editUser.getText().toString().equals("admin") && editPass.getText().toString().equals("admin"))  //To check if credential exists
                {
                    Intent intent = new Intent(v.getContext(), RoomType.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        //Login Request
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final String username = editUser.getText().toString();
                final String password = editPass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String first_name = jsonResponse.getString("first_name");
                                String last_name = jsonResponse.getString("last_name");
                                String userID = jsonResponse.getString("userID");

                                Intent intent = new Intent(LoginPage.this, RoomType.class);
                                intent.putExtra("first_name", first_name);
                                intent.putExtra("last_name", last_name);
                                intent.putExtra("userID", userID);

                                LoginPage.this.startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                         .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                queue.add(loginRequest);
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
}
