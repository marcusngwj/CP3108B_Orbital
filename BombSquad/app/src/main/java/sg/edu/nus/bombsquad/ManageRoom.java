package sg.edu.nus.bombsquad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ManageRoom extends AppCompatActivity {
    boolean[] selected = new boolean[100000];
    final Global global = Global.getInstance();
    HashMap<String, String> selectedRoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_room);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: ManageRoom");

        global.setData(selected);
        global.setQuestionName(new String[100000]);
        selectedRoomName = new HashMap<String, String>();
        display();
        deleteRoom();
        startRoom();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); so that user cannot press the back button on android! YAY!
        Intent intent = getIntent();
        Intent back = new Intent(ManageRoom.this, RoomType.class);
        back.putExtra("user_id", intent.getStringExtra("user_id"));
        startActivity(back);
    }

    private void display() {
        try {
            Intent intent = getIntent();
            JSONObject room = new JSONObject(intent.getStringExtra("room"));
            LinearLayout ll = (LinearLayout) findViewById(R.id.manageRoomScroll);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            selected = global.getBooleanArray();

            int i = 0;
            while (i < room.length()) {
                final CheckBox checkbox = (CheckBox)((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox,null);
                checkbox.setText(room.getJSONObject(i+"").getString("room_name"));
                checkbox.setId(Integer.parseInt(room.getJSONObject(i+"").getString("room_code")));
                checkbox.setTextSize(25);
                selectedRoomName.put(checkbox.getId()+"", checkbox.getText()+"");
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected[v.getId()]) {
                            selected[v.getId()] = false;
                        }
                        else {
                            selected[v.getId()] = true;
                            String name = (String) checkbox.getText();
                        }
                    }
                });
                assert ll != null;
                ll.addView(checkbox, lp);
                i++;
            }
        }
        catch (JSONException e) {
           // e.printStackTrace();
        }

    }

    //Red-Cross Button
    private void deleteRoom() {
        final Intent intent = getIntent();
        ImageButton redCross = (ImageButton)findViewById(R.id.RMredCross);
        assert redCross != null;
        redCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageRoom.this);
                builder.setMessage("Are you sure you wish to delete this room? Deleted room cannot be retrieved anymore.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int j = 0;
                                while (j < 100000) {
                                    if (selected[j]) {
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Intent backIntent = new Intent(ManageRoom.this, RoomType.class);
                                                backIntent.putExtra("room", intent.getStringExtra("room"));
                                                backIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                                startActivity(backIntent);
                                            }
                                        };
                                        RoomDeleteRequest roomDelete = new RoomDeleteRequest(intent.getStringExtra("user_id"), j+"", responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(ManageRoom.this);
                                        queue.add(roomDelete);
                                    }
                                    j++;
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        });
    }

    //Green-Tick Button
    private void startRoom() {
        final Intent intent = getIntent();
        //final String user_id = intent.getStringExtra("user_id");
        ImageButton greenTick = (ImageButton)findViewById(R.id.RMgreenTick);
        assert greenTick != null;
        greenTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageRoom.this);
                builder.setMessage("Do you wish to start the room or edit the room?")
                        .setPositiveButton("Start Room", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManageRoom.this);
                                builder.setMessage("Please wait while we load the room :)")
                                        .create()
                                        .show();
                                int k = 0;
                                int numTrues = 0;
                                int chosenK=-1;

                                //Proceed to next activity iff 1 option is selected
                                while(k < 100000){
                                    if(numTrues>1){
                                        Toast.makeText(getApplicationContext(), "Select only one",Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    else if(selected[k]){
                                        numTrues++;
                                        chosenK = k;
                                    }
                                    k++;
                                }


                                final int codeOfRoomChosen = chosenK;

                                if(numTrues < 1){
                                    Toast.makeText(getApplicationContext(), "Select a room",Toast.LENGTH_SHORT).show();
                                }

                                else if(numTrues == 1){
                                    //Nested Response to make sure counting is done before new intent
                                    final OkHttpClient client = new OkHttpClient();
                                    RequestBody postData = new FormBody.Builder()
                                            .add("user_id", intent.getStringExtra("user_id"))
                                            .add("room_status", "1")
                                            .add("room_code", chosenK+"")
                                            .add("player", intent.getStringExtra("user_id"))
                                            .build();
                                    Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/gameRequest.php").post(postData).build();

                                    client.newCall(request)
                                            .enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    System.out.println("FAIL");
                                                }
                                                @Override
                                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                    RequestBody postData = new FormBody.Builder()
                                                            .add("room_code", codeOfRoomChosen+"")
                                                            .build();
                                                    Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getQuestionID.php").post(postData).build();
                                                    client.newCall(request)
                                                            .enqueue(new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {
                                                                    System.out.println("FAIL");
                                                                }
                                                                @Override
                                                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                                    try {
                                                                        JSONObject jsonResponse = new JSONObject(response.body().string());
                                                                        String[] question_id = new String[jsonResponse.length()];
                                                                        int count = 0;  //Count total number of questions in a room

                                                                        int i = 0;
                                                                        while (i < jsonResponse.length()) {
                                                                            if (jsonResponse.getJSONObject(i + "").getBoolean("success")) {
                                                                                question_id[i] = (jsonResponse.getJSONObject(i + "").getString("question_id"));
                                                                                count++;
                                                                                global.setNumber(count);
                                                                                global.setQuestion_id(question_id);

                                                                            }
                                                                            i++;
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        //e.printStackTrace();
                                                                    }

                                                                    final int numQuestion = global.getNumber();
                                                                    final String[][] tempArr = new String[numQuestion][8];
                                                                    int i = 0;
                                                                    global.setCounter(0);
                                                                    while (i < numQuestion) {
                                                                        final int idx = i;

                                                                        RequestBody postData = new FormBody.Builder()
                                                                                .add("question_id", global.getQuestion_id()[idx])
                                                                                .build();
                                                                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/questionAnswerOption.php").post(postData).build();
                                                                        client.newCall(request)
                                                                                .enqueue(new Callback() {
                                                                                    @Override
                                                                                    public void onFailure(Call call, IOException e) {
                                                                                        System.out.println("FAIL");
                                                                                    }
                                                                                    @Override
                                                                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                                                        try {
                                                                                            JSONObject jsonResponse = new JSONObject(response.body().string());
                                                                                            if (jsonResponse.getBoolean("success")) {
                                                                                                tempArr[idx][0] = jsonResponse.getString("question_type");
                                                                                                tempArr[idx][1] = jsonResponse.getString("question");
                                                                                                tempArr[idx][2] = jsonResponse.getString("option_one");
                                                                                                tempArr[idx][3] = jsonResponse.getString("option_two");
                                                                                                tempArr[idx][4] = jsonResponse.getString("option_three");
                                                                                                tempArr[idx][5] = jsonResponse.getString("option_four");
                                                                                                tempArr[idx][6] = jsonResponse.getString("answer");
                                                                                                global.putTimeLimit(jsonResponse.getString("question_id"), jsonResponse.getString("time_limit"));
                                                                                                if (global.getCounter() == numQuestion - 1) {
                                                                                                    global.setData(tempArr);
                                                                                                    Intent intent = getIntent();
                                                                                                    Intent hostIntent = new Intent(ManageRoom.this, HostView.class);
                                                                                                    hostIntent.putExtra("room", intent.getStringExtra("room"));
                                                                                                    hostIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                                                                                    global.setRoomCode(codeOfRoomChosen+"");
                                                                                                    global.setRoomName(selectedRoomName.get(codeOfRoomChosen+""));
                                                                                                    global.setNumQuestion(global.getNumber());
                                                                                                    global.setCounter(0);
                                                                                                    startActivity(hostIntent);
                                                                                                }
                                                                                                global.setCounter(global.getCounter()+1);
                                                                                            }
                                                                                        } catch (JSONException e) {
                                                                                            // e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                });
                                                                        i++;
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }
                        })
                        .setNeutralButton("Edit Room", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int k = 0;
                                OkHttpClient client = new OkHttpClient();
                                System.out.println("EDIT ROOM");
                                while (k < 100000) {
                                    if (selected[k]) {
                                        System.out.println("room_code" + k);
                                        RequestBody postData = new FormBody.Builder()
                                                .add("room_code", k+"")
                                                .build();
                                        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getQuestionNameInRoom.php").post(postData).build();
                                        client.newCall(request)
                                                .enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        System.out.println("FAIL");
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                                        try {
                                                            JSONObject result = new JSONObject(response.body().string());
                                                            System.out.println("Result: " + result);
                                                            if (result.getJSONObject(0+"").getBoolean("success")) {
                                                                System.out.println("HERE!");
                                                                int m = 0;
                                                                while (m < result.length()) {
                                                                    System.out.println("TEST");
                                                                    global.getQuestionName()[m] = result.getJSONObject(m + "").getString("bomb_name");
                                                                    System.out.println("TEST1");
                                                                    m++;
                                                                    System.out.println("TEST2");
                                                                }
                                                                System.out.println("HERE1!");
                                                                Intent intent = getIntent();
                                                                Intent editRoomIntent = new Intent(ManageRoom.this, EditRoom.class);
                                                                editRoomIntent.putExtra("room", intent.getStringExtra("room"));
                                                                editRoomIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                                                                System.out.println("HERE2!");
                                                                startActivity(editRoomIntent);
                                                                System.out.println("HERE3!");
                                                            }
                                                            System.out.println("FIRST ELEMENT: " + global.getQuestion_id()[0]);
                                                        } catch (JSONException e) {
                                                            //e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                    k++;
                                }

                            }
                        })
                        .create()
                        .show();
            }
        });
    }


}