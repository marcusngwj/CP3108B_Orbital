package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoomType extends AppCompatActivity {
    Button bCreate,bEnter, bHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type);

        bCreate=(Button)findViewById(R.id.buttonCreate);
        if (bCreate != null) {
            bCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCreate = new Intent(v.getContext(), CreateRoom.class);
                    startActivity(intentCreate);
                }
            });
        }

        bEnter=(Button)findViewById(R.id.buttonEnter);
        if (bEnter != null) {
            bEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEnter = new Intent(v.getContext(), EnterRoom.class);
                    startActivity(intentEnter);
                }
            });
        }
    }
}
