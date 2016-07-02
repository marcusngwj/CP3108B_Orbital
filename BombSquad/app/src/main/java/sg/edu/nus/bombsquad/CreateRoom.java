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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

public class CreateRoom extends AppCompatActivity {
    EditText editRoomName;
    String roomName;
    Button bGenerate, bSetUp;
    TextView txGCode;
    int roomCode;
    boolean bGenerateClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        setUpRoom();
    }

    private void setUpRoom(){
        bSetUp=(Button)findViewById(R.id.buttonSetUp);
        bSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomCode = (int) Math.floor(Math.random()*1000000);
                editRoomName = (EditText) findViewById(R.id.editTextRoomName);
                roomName = editRoomName.getText().toString();
                final String roomCodeString = roomCode + "";
                Intent intent = getIntent();
                final String user_id = intent.getStringExtra("user_id");
                if (TextUtils.isEmpty(roomName)) {
                    editRoomName.setError("Input Room Name");
                } else if (roomName.length() > 20) {
                    editRoomName.setError("Name must be less than 20 characters");
                } else {
                    intent = new Intent(CreateRoom.this, ExistOrNew.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("room_code", roomCodeString);
                    intent.putExtra("room_name", roomName);
                    startActivity(intent);
                }
            }
        });
    }
}
