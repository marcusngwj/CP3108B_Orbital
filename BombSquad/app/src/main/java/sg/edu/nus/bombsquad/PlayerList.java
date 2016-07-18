package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class PlayerList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        display();
    }

    private void display() {
        Intent intent = getIntent();
        try {
            JSONObject player_list = new JSONObject(intent.getStringExtra("player_list"));
            int i = 1;
            while (!player_list.getJSONObject(i+"").getString("player").equals("0")) {
                final String player_id = player_list.getJSONObject(i+"").getString("player");
                Button player = new Button(this);
                player.setText(getPlayerName(player_id));
                player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody postData = new FormBody.Builder().add("player", player_id).build();
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
                });
                i++;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public String getPlayerName(String id) {
        final Global global = Global.getInstance();
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder().add("user_id", id).build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getPlayerName.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        String name = response.body().string();
                        global.setPlayerName(name);
                    }
                });

        if(global.getPlayerName() != null) {
            String name = global.getPlayerName();
            global.setPlayerName(null);
            return name;
        }
        else {
            return getPlayerName(id);
        }
    }
}

