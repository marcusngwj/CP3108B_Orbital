package sg.edu.nus.bombsquad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PlayerList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        display();
    }

    private void display() {
        Intent intent = getIntent();
        final Global global = Global.getInstance();
        final Queue player_list = global.getPlayerList();
        System.out.println(player_list.peek());
        /*if(global.getPlayerName() != null) {
            String name = global.getPlayerName();
            global.setPlayerName(null);
            return name;
        }
        else {
            return getPlayerName(id);
        }*/
        //getPlayerName(currPlayer);
            /*player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody postData = new FormBody.Builder().add("player", currPlayer).build();
                    Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/setPlayerQuestion.php").post(postData).build();

                    client.newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    System.out.println("FAIL");
                                }
                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    //Do something...
                                }
                            });
                }
            });*/
    }
}

