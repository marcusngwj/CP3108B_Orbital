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
                final String roomCode = etEnterRoomCode.getText().toString();

                if (roomCode.isEmpty()) {
                    etEnterRoomCode.setError("Enter a room code");
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    String room_name = jsonResponse.getString("room_name");
                                    global.setRoomName(room_name);
                                    final String room_code = jsonResponse.getString("room_code");
                                    global.setRoomCode(room_code);

                                    final RoomBank roomBank = new RoomBank(room_name, room_code);
                                    global.setRoomBank(roomBank);

                                    Response.Listener<String> responseCatcher = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                ArrayList<String> questionIDList = new ArrayList<String>();

                                                System.out.println(jsonResponse);

                                                RoomBank tempRoomBank = global.getRoomBank();
                                                HashMap<String, RoomDetail> roomDetailHashMap = new HashMap<String, RoomDetail>();
                                                int count = 0;  //Count total number of questions in a room

                                                int i = 0;
                                                while (i < jsonResponse.length()) {
                                                    if (jsonResponse.getJSONObject(i + "").getBoolean("success")) {
                                                        String room_id = jsonResponse.getJSONObject(i + "").getString("room_id");
                                                        String deploy_status = jsonResponse.getJSONObject(i + "").getString("deploy_status");
                                                        String time_left = jsonResponse.getJSONObject(i + "").getString("time_left");
                                                        String player_id = jsonResponse.getJSONObject(i + "").getString("player_id");
                                                        String question_id = jsonResponse.getJSONObject(i + "").getString("question_id");
                                                        count++;
                                                        questionIDList.add(question_id);
                                                        roomBank.setQuestionIDList(questionIDList);

                                                        tempRoomBank.setNumQuestion(count);

                                                        roomDetailHashMap.put(question_id, new RoomDetail(roomBank.getRoomCode(), room_id, question_id, deploy_status, time_left, player_id));

                                                        tempRoomBank.setRoomDetailHashMap(roomDetailHashMap);
                                                        global.setRoomBank(tempRoomBank);
                                                    }
                                                    i++;
                                                }


                                            } catch (JSONException e) {
                                                /*e.printStackTrace();*/
                                            }

                                            //Add user into "GAME" table in the database
                                            RoomBank.addPlayerIntoGame(room_code, global.getUserId());



                                            Intent playerIntent = new Intent(EnterRoom.this, PreparingPlayerView.class);
                                            startActivity(playerIntent);

                                        }
                                    };
                                    GetRoomDetailRequest getRoomDetailRequest = new GetRoomDetailRequest(roomCode + "", responseCatcher);
                                    RequestQueue requestQueue = Volley.newRequestQueue(EnterRoom.this);
                                    requestQueue.add(getRoomDetailRequest);

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
                    EnterRoomRequest enterRoomRequest = new EnterRoomRequest(roomCode, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EnterRoom.this);
                    queue.add(enterRoomRequest);
                }
            }
        });
    }
}
