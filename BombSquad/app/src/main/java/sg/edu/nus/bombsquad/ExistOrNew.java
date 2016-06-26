package sg.edu.nus.bombsquad;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ExistOrNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_or_new);

        existingBomb();
        newBomb();

    }

    //Button: Existing Bomb
    private void existingBomb() {
        final Button bExistingBomb = (Button) findViewById(R.id.buttonExistingBomb);
        assert bExistingBomb != null;
        bExistingBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getJSONObject("0").getBoolean("success");

                            if(success){
                                Intent intentDepo = new Intent(ExistOrNew.this, History.class);
                                intentDepo.putExtra("depo", jsonResponse.toString());
                                ExistOrNew.this.startActivity(intentDepo);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ExistOrNew.this);
                                builder.setMessage("FAILLLLLLLLLLLLLLLL")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExistOrNew.this);
                            builder.setMessage("No bomb found")
                                    .create()
                                    .show();
                        }
                    }
                };
                Intent intent = getIntent();
                BombDepoRequest bombDepoRequest = new BombDepoRequest(intent.getStringExtra("userID"), responseListener);
                RequestQueue queue = Volley.newRequestQueue(ExistOrNew.this);
                queue.add(bombDepoRequest);
            }
        });
    }

    //Button: New Bomb
    private void newBomb() {
        final Button bNewBomb = (Button) findViewById(R.id.buttonNewBomb);
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        final String roomCode = intent.getStringExtra("roomCode");
        assert bNewBomb != null;
        bNewBomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistOrNew.this, NewBomb.class);
                intent.putExtra("userID", userID);
                intent.putExtra("roomCode", roomCode);
                ExistOrNew.this.startActivity(intent);
            }
        });
    }
}
