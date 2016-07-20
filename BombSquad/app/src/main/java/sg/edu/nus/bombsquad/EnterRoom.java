package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EnterRoom extends AppCompatActivity {
    Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);

        final EditText etEnterRoomCode = (EditText) findViewById(R.id.editTextEnterRoomCode);
        final Button bReadyForBattle = (Button) findViewById(R.id.buttonReadyForBattle);

        assert bReadyForBattle != null;
        bReadyForBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final String roomCode = etEnterRoomCode.getText().toString();

                if(roomCode.isEmpty()){
                    etEnterRoomCode.setError("Enter a room code");
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                System.out.println(jsonResponse);
                                System.out.println(success);

                                if (success) {
                                    Intent intent = getIntent();
                                    Intent playerIntent = new Intent(EnterRoom.this, PlayerView.class);
                                    playerIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                    playerIntent.putExtra("room_name", jsonResponse.getString("room_name"));
                                    playerIntent.putExtra("room_code", jsonResponse.getString("room_code"));

                                    global.setRoomName(jsonResponse.getString("room_name"));
                                    global.setRoomCode(jsonResponse.getString("room_code"));
                                    startActivity(playerIntent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterRoom.this);
                                    builder.setMessage("Room Code does not exist")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    EnterRoomRequest enterRoomRequest = new EnterRoomRequest(roomCode, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EnterRoom.this);
                    queue.add(enterRoomRequest);
                }
            }
        });
    }
}
