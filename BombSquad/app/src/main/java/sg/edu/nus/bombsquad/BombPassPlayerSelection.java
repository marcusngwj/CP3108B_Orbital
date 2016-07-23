package sg.edu.nus.bombsquad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BombPassPlayerSelection extends AppCompatActivity {
    Global global = Global.getInstance();
    RoomBank roomBank = global.getRoomBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_pass_player_selection);

        //To show on Android Monitor onCreate
        System.out.println("Activity Name: BombPassPlayerSelection");

//        display();
    }

    public void display(){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", roomBank.getRoomCode())
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/removePlayerFromGame.php").post(postData).build();

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
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                    }
                });
    }
}
