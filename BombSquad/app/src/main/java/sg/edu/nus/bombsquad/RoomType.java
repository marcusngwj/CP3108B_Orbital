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

        //Create Room button
        bCreateRoom=(Button)findViewById(R.id.buttonCreateRoom);
        if (bCreateRoom != null) {
            bCreateRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCreate = new Intent(v.getContext(), CreateRoom.class);
                    startActivity(intentCreate);
                }
            });
        }

        //Enter Room button
        bEnterRoom=(Button)findViewById(R.id.buttonEnterRoom);
        if (bEnterRoom != null) {
            bEnterRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEnter = new Intent(v.getContext(), EnterRoom.class);
                    startActivity(intentEnter);
                }
            });
        }
    }
}
