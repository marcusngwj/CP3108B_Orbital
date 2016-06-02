package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final EditText editFirstName = (EditText)findViewById(R.id.editTextFirstName);
        final EditText editLastName = (EditText)findViewById(R.id.editTextLastName);
        final EditText editEmail = (EditText)findViewById(R.id.editTextEmail);
        final EditText editMobileNo = (EditText)findViewById(R.id.editTextMobileNo);
        final EditText editUser = (EditText)findViewById(R.id.editTextUserRegister);
        final EditText editPass = (EditText)findViewById(R.id.editTextPassRegister);
        final EditText editPassConfirm = (EditText)findViewById(R.id.editTextPassRegisterConfirm);
        final Button bRegister=(Button)findViewById(R.id.buttonRegisterPage);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editUser.getText().toString().equals("admin") && editPass.getText().toString().equals("admin"))  //To check if credential exists
                {
                    Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
