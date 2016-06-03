package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        //Login Button
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
