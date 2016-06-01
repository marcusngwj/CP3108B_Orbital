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

//    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        onLogin();
    }

    public void onLogin(){
        bLogin=(Button)findViewById(R.id.buttonLogin);
        editUser=(EditText)findViewById(R.id.editTextUser);
        editPass=(EditText)findViewById(R.id.editTextPass);

//        tx1=(TextView)findViewById(R.id.textView3);
//        tx1.setVisibility(View.GONE);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                if(editUser.getText().toString().equals("admin") && editPass.getText().toString().equals("admin"))
                {
                    Intent intent = new Intent(v.getContext(), RoomType.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

//                    tx1.setVisibility(View.VISIBLE);
//                    tx1.setBackgroundColor(Color.RED);
//                    counter--;
//                    tx1.setText(Integer.toString(counter));
//
//                    if (counter == 0) {
//                        b1.setEnabled(false);
                }
            }
        });
    }
}
