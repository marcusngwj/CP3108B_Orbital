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
    }

    @Override
    public void onBackPressed() {
    }

    private void display() {
        Intent intent = getIntent();
        String[] playerListScore = global.getPlayerListScore();
        TableLayout nameTable = (TableLayout)findViewById(R.id.nameTable);
        TableLayout scoreTable = (TableLayout)findViewById(R.id.scoreTable);
        for (int i = 0; i < Integer.parseInt(intent.getStringExtra("num_player")) - 1; i++) {
            TableRow nameRow = new TableRow(ScoreBoard.this);
            TableRow scoreRow = new TableRow(ScoreBoard.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            nameRow.setLayoutParams(lp);
            scoreRow.setLayoutParams(lp);
            TextView tv = new TextView(ScoreBoard.this);
            TextView tv1 = new TextView(ScoreBoard.this);
            tv.setText(global.getPlayerInRoom(playerListScore[i]));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(30);
            tv1.setText(global.getPlayerScore(playerListScore[i]));
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(30);
            nameRow.addView(tv);
            scoreRow.addView(tv1);
            nameTable.addView(nameRow, i);
            scoreTable.addView(scoreRow, i);
        }
    }

}
