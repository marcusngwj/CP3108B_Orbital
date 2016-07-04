package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class PlayerView extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);
        display();
    }

    private void display() {
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        TextView room_name = (TextView)findViewById(R.id.textViewPlayerViewBattlefieldRoomName);
        assert room_name != null;
        room_name.setText(intent.getStringExtra("room_name"));
        LinearLayout ll = (LinearLayout)findViewById(R.id.playerViewLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView waiting = (TextView)findViewById(R.id.textViewPlayerMessage);
        waiting.setText("Waiting for host to start game...");
        System.out.println("-----------------------");
        System.out.println("test test test test");
        System.out.println("-----------------------");
        new Background().execute(intent.getStringExtra("room_code"));
    }

    class Background extends AsyncTask<String, Void, Void> {
        TextView uiUpdate = (TextView)findViewById(R.id.textViewPlayerMessage);

        protected Void doInBackground(String... codes) {
            final Global global = Global.getInstance();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseListener = new JSONObject(response);
                        boolean success = responseListener.getBoolean("success");
                        System.out.println(responseListener);
                        if (success){
                            global.setRoomStatus(responseListener.getString("room_status"));
                            }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            UpdatePlayerViewRequest updatePlayer = new UpdatePlayerViewRequest(codes[0], responseListener);
            RequestQueue queue = Volley.newRequestQueue(PlayerView.this);
            queue.add(updatePlayer);
            return null;
        }

        protected void onPostExecute(Void update) {
            Global global = Global.getInstance();
            System.out.println("global room status = " + global.getRoomStatus());
            if (global.getRoomStatus() != null && global.getRoomStatus().equals("1")) {
                uiUpdate.setText("In room");
            }
            else {
                System.out.println("No update");
            }
        }
    }
}
