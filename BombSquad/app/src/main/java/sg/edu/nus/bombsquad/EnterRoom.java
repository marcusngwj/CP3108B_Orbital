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

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                /*String first_name = jsonResponse.getString("first_name");
                                String last_name = jsonResponse.getString("last_name");
                                String userID = jsonResponse.getString("userID");

                                Intent intent = new Intent(EnterRoom.this, RoomType.class);
                                intent.putExtra("first_name", first_name);
                                intent.putExtra("last_name", last_name);
                                intent.putExtra("userID", userID);*/

                                //EnterRoom.this.startActivity(intent);

                                AlertDialog.Builder builder = new AlertDialog.Builder(EnterRoom.this);
                                builder.setMessage("PASS")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EnterRoom.this);
                                builder.setMessage("Code Failed")
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
        });
    }
}
