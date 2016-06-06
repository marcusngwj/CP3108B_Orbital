package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoomType extends AppCompatActivity {
    Button bCreateRoom, bEnterRoom, bHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type);

        createRoom();
        enterRoom();
    }

    //Create Room button
    private void createRoom(){
        bCreateRoom=(Button)findViewById(R.id.buttonCreateRoom);
        bCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreate = new Intent(v.getContext(), CreateRoom.class);
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
}
