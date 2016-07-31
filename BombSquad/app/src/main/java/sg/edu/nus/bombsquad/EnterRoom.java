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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EnterRoom extends AppCompatActivity {
    Global global = Global.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: EnterRoom");

        final EditText etEnterRoomCode = (EditText) findViewById(R.id.editTextEnterRoomCode);
        final Button bReadyForBattle = (Button) findViewById(R.id.buttonReadyForBattle);

        assert bReadyForBattle != null;
        bReadyForBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String room_code = etEnterRoomCode.getText().toString();

                if (room_code.isEmpty()) {
                    etEnterRoomCode.setError("Enter a room code");
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<String> questionIDList = new ArrayList<String>();
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                System.out.println(jsonResponse);

                                if (success) {
                                    final String room_name = jsonResponse.getString("room_name");
                                    System.out.println("roomNAME: " + room_name);
                                    final String num_rows = jsonResponse.getString("num_rows");
                                    final int num_rows_IntegerValue = Integer.valueOf(num_rows);

                                    final RoomBank roomBank = new RoomBank(room_name, room_code);
                                    roomBank.setNumQuestion(num_rows_IntegerValue);
                                    global.setRoomBank(roomBank);
                                    RoomBank tempRoomBank = global.getRoomBank();
                                    HashMap<String, RoomDetail> roomDetailHashMap = new HashMap<String, RoomDetail>();

                                    for(int i=0; i<num_rows_IntegerValue; i++){
                                        if (jsonResponse.getJSONObject(i + "").getBoolean("success")) {
                                            String room_id = jsonResponse.getJSONObject(i + "").getString("room_id");
                                            String deploy_status = jsonResponse.getJSONObject(i + "").getString("deploy_status");
                                            String time_left = jsonResponse.getJSONObject(i + "").getString("time_left");
                                            String player_id = jsonResponse.getJSONObject(i + "").getString("player_id");
                                            String question_id = jsonResponse.getJSONObject(i + "").getString("question_id");
                                            questionIDList.add(question_id);
                                            roomBank.setQuestionIDList(questionIDList);

                                            roomDetailHashMap.put(question_id, new RoomDetail(roomBank.getRoomCode(), room_id, question_id, deploy_status, time_left, player_id));

                                            tempRoomBank.setRoomDetailHashMap(roomDetailHashMap);
                                            global.setRoomBank(tempRoomBank);
                                        }

                                    }

                                    //Add user into "GAME" table in the database
                                    RoomBank.addPlayerIntoGame(room_code, global.getUserId());

                                    Intent playerIntent = new Intent(EnterRoom.this, PreparingPlayerView.class);
                                    startActivity(playerIntent);


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EnterRoom.this);
                                    builder.setMessage("Room Code does not exist")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                /*e.printStackTrace();*/
                            }
                        }
                    };
                    EnterRoomRequest enterRoomRequest = new EnterRoomRequest(room_code, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EnterRoom.this);
                    queue.add(enterRoomRequest);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();    //user cannot press the back button on android
        Intent intent = getIntent();
        Intent back = new Intent(EnterRoom.this, RoomType.class);
        back.putExtra("user_id", global.getUserId());
        startActivity(back);
    }
}
