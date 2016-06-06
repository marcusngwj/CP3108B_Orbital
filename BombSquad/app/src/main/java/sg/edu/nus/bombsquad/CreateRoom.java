package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateRoom extends AppCompatActivity {
    EditText editRoomName;
    String roomName;
    Button bGenerate, bSetUp;
    TextView txGCode;
    int generatedCode;
    boolean bGenerateClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        txGCode=(TextView)findViewById(R.id.textViewGCode);
        txGCode.setVisibility(View.GONE);

        generateCode();
        setUpRoom();
    }

    private void generateCode(){
        bGenerate=(Button)findViewById(R.id.buttonGenerate);
        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedCode = (int) Math.floor(Math.random()*1000000);
                txGCode.setVisibility(View.VISIBLE);
                txGCode.setBackgroundColor(Color.WHITE);
                txGCode.setText(Integer.toString(generatedCode));
                bGenerateClicked = true;
            }
        });
    }

    private void setUpRoom(){
        bSetUp=(Button)findViewById(R.id.buttonSetUp);
        bSetUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editRoomName = (EditText)findViewById(R.id.editTextRoomName);
                roomName = editRoomName.getText().toString();
                if(TextUtils.isEmpty(roomName)){
                    editRoomName.setError("Input Room Name");
                }
                else if(roomName.length()>20){
                    editRoomName.setError("Name must be less than 20 characters");
                }
                else if(bGenerateClicked==false){
                    Toast.makeText(getApplicationContext(), "Please Generate Code",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(v.getContext(), ExistOrNew.class);
                    startActivity(intent);
                }
            }
        });
    }

    public String getRoomName(){
        return roomName;
    }

    public int getRoomCode(){
        return generatedCode;
    }
}
