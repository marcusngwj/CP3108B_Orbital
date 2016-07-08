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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomType extends AppCompatActivity {
    Button bCreateRoom, bEnterRoom, bHistory, bManageRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_type);
        manageRoom();
        createRoom();
        enterRoom();
        history();
    }

    //Manage Room Button
    private void manageRoom() {
        bManageRoom = (Button)findViewById(R.id.buttonManageRoom);
        bManageRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getJSONObject("0").getBoolean("success");
                            System.out.println("TEST TEST TEST TEST TEST TEST");
                            System.out.println("test = " + jsonResponse);

                            if(success){
                                Intent intentManage = new Intent(RoomType.this, ManageRoom.class);
                                intentManage.putExtra("room", jsonResponse.toString());
                                intentManage.putExtra("user_id", jsonResponse.getJSONObject("0").getString("user_id"));
                                RoomType.this.startActivity(intentManage);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RoomType.this);
                                builder.setMessage("FAILLLLLLLLLLLLLLLL")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RoomType.this);
                            builder.setMessage("No rooms found")
                                    .create()
                                    .show();
                        }
                    }
                };
                Intent intent = getIntent();
                RoomRequest roomRequest = new RoomRequest(intent.getStringExtra("user_id"), responseListener);
                RequestQueue queue = Volley.newRequestQueue(RoomType.this);
                queue.add(roomRequest);
            }
        });
    }

    //Create Room button
    private void createRoom(){
        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        bCreateRoom=(Button)findViewById(R.id.buttonCreateRoom);
        bCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getJSONObject("0").getBoolean("success");
                            System.out.println(jsonResponse);

                            if (success) {
                                Intent intentCreate = new Intent(RoomType.this, CreateRoom.class);
                                intentCreate.putExtra("room", jsonResponse.toString());
                                intentCreate.putExtra("user_id", user_id);
                                startActivity(intentCreate);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                GetAllRoomRequest unique = new GetAllRoomRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(RoomType.this);
                queue.add(unique);
            }
        });
    }

    //Enter Room button
    private void enterRoom(){
        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        bEnterRoom=(Button)findViewById(R.id.buttonEnterRoom);
        bEnterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEnter = new Intent(v.getContext(), EnterRoom.class);
                intentEnter.putExtra("user_id", user_id);
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
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getJSONObject("0").getBoolean("success");

                            if(success){
                                Intent intentHistory = new Intent(RoomType.this, History.class);
                                intentHistory.putExtra("room", jsonResponse.toString());
                                RoomType.this.startActivity(intentHistory);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RoomType.this);
                                builder.setMessage("FAILLLLLLLLLLLLLLLL")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RoomType.this);
                            builder.setMessage("No history found")
                                    .create()
                                    .show();
                        }
                    }
                };
                Intent intent = getIntent();
                RoomRequest roomRequest = new RoomRequest(intent.getStringExtra("user_id"), responseListener);
                RequestQueue queue = Volley.newRequestQueue(RoomType.this);
                queue.add(roomRequest);
            }
        });
    }
}
