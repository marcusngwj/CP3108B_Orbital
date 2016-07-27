package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BombPassSelectionType extends AppCompatActivity {
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_pass_selection_type);

        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: BombPassSelectionType");

        passToRandom();
        passToSelected();
    }

    //Pass bomb to random player
    private void passToRandom(){
        Button bRandomPlayer = (Button)findViewById(R.id.buttonRandomPlayerBPST);
        bRandomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passBombToRandomPlayer(global.getUserId(), roomBank.getRoomCode(), roomBank.getCurrentQuestion());
                /*Intent intentPTS = new Intent(BombPassSelectionType.this, PlayerView.class);
                startActivity(intentPTS);*/
            }
        });
    }

    //Pass bomb to selected player
    private void passToSelected(){
        Button bSelectPlayer = (Button)findViewById(R.id.buttonSelectPlayerBPST);
        bSelectPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPTS = new Intent(BombPassSelectionType.this, BombPassPlayerSelection.class);
                startActivity(intentPTS);
            }
        });
    }

    //Static method to be used by other class
    public static void passBombToRandomPlayer(String user_id, String room_code, String question_id){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", room_code)
                .add("question_id", question_id)
                .add("my_id", user_id)
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/passBombToRandom.php").post(postData).build();

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
                            System.out.println(result);

                            String player_id_to_receive_bomb = result.getString("player_id_to_receive_bomb");
                            String player_first_name_to_receive_bomb = result.getString("player_first_name_to_receive_bomb");
                            String player_last_name_to_receive_bomb = result.getString("player_last_name_to_receive_bomb");

                            System.out.println("id of new player who possesses the bomb: " + player_id_to_receive_bomb);
                            System.out.println("Name: " + player_first_name_to_receive_bomb + " " + player_last_name_to_receive_bomb);


                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }


                    }
                });
    }
}
