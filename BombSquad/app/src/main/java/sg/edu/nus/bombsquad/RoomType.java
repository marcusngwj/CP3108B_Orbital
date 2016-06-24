package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomType extends AppCompatActivity {
    Button bCreateRoom, bEnterRoom, bHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type);

        final TextView etTestFirstName = (TextView)findViewById(R.id.etTestFirstName);
        final TextView etTestLastName = (TextView)findViewById(R.id.etTestLastName);

        Intent intent = getIntent();
        final String first_name = intent.getStringExtra("first_name");
        final String last_name = intent.getStringExtra("last_name");
        final String userID = intent.getStringExtra("userID");
        final TextView eTestId = (TextView)findViewById(R.id.eTestId); //test
        eTestId.setText(userID); //test
        etTestFirstName.setText(first_name);
        etTestLastName.setText(last_name);

        createRoom(userID);
        enterRoom();
        history();
    }

    //Create Room button
    private void createRoom(String userID){
        final String _userID = userID;
        bCreateRoom=(Button)findViewById(R.id.buttonCreateRoom);
        bCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreate = new Intent(v.getContext(), CreateRoom.class);
                intentCreate.putExtra("userID", _userID);
                startActivity(intentCreate);
            }
        });
    }

    //Enter Room button
    private void enterRoom(){
        bEnterRoom=(Button)findViewById(R.id.buttonEnterRoom);
        bEnterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEnter = new Intent(v.getContext(), EnterRoom.class);
                startActivity(intentEnter);
            }
        });
    }

    //Enter History
    private void history() {
        bHistory = (Button)findViewById(R.id.buttonHistory);
        bHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHistory = new Intent(v.getContext(), History.class);
                startActivity(intentHistory);
            }
        });
    }
}
