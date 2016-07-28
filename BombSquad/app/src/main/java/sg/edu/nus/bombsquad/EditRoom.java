package sg.edu.nus.bombsquad;


import android.os.Bundle;
 import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class EditRoom extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        display();
    }

    private void display() {
        Global global = Global.getInstance();
        final boolean[] selected = new boolean[100000];
        LinearLayout ll = (LinearLayout)findViewById(R.id.editRoomScroll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int i = 0;
        while (i < global.getQuestionName().length) {
            CheckBox checkbox = (CheckBox) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.checkbox, null);
            checkbox.setText(global.getQuestionName()[i]);
            checkbox.setId(Integer.parseInt(global.getQuestion_id()[i]));
            checkbox.setTextSize(25);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected[v.getId()]) {
                        selected[v.getId()] = false;
                    } else {
                        selected[v.getId()] = true;
                    }
                }
            });
            assert ll != null;
            ll.addView(checkbox, lp);
            i++;
        }
    }
}
