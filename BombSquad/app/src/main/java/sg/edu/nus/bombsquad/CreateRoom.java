package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateRoom extends AppCompatActivity {
    EditText editRoomName;
    String roomName;
    Button bGenerate, bSetUp;
    TextView txGCode;
    int generatedCode;

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
            }
        });
    }

    private void setUpRoom(){
        bSetUp=(Button)findViewById(R.id.buttonSetUp);
        bSetUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                roomName = editRoomName.getText().toString();
                Intent intent = new Intent(v.getContext(), ExistOrNew.class);
                startActivity(intent);
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
