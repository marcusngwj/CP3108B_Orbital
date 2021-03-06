package sg.edu.nus.bombsquad;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoreBoard extends AppCompatActivity{
    Global global = Global.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        display();
        exit();
    }

    @Override
    public void onBackPressed() {
    }

    private void display() {
        Intent intent = getIntent();
        String[] playerListScore = global.getPlayerListScore();
        TableLayout nameTable = (TableLayout)findViewById(R.id.nameTable);
        TableLayout scoreTable = (TableLayout)findViewById(R.id.scoreTable);
        for (int i = 0; i < Integer.parseInt(intent.getStringExtra("num_player")); i++) {
            TableRow nameRow = new TableRow(ScoreBoard.this);
            TableRow scoreRow = new TableRow(ScoreBoard.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            nameRow.setLayoutParams(lp);
            scoreRow.setLayoutParams(lp);
            TextView tv = new TextView(ScoreBoard.this);
            TextView tv1 = new TextView(ScoreBoard.this);
            tv.setText((playerListScore[i]));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(20);
            tv1.setText(global.getPlayerScore(playerListScore[i]));
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(20);
            nameRow.addView(tv);
            scoreRow.addView(tv1);
            nameTable.addView(nameRow, i);
            scoreTable.addView(scoreRow, i);
        }
    }
    private void exit() {
        Button endGame = (Button)findViewById(R.id.buttonEndGame);
        assert endGame != null;
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent endOfGameIntent = new Intent(ScoreBoard.this, RoomType.class);
                endOfGameIntent.putExtra("user_id", intent.getStringExtra("user_id"));
                System.out.println("USER ID: ");
                System.out.println(intent.getStringExtra("user_id"));
                startActivity(endOfGameIntent);
            }
        });

    }
}
