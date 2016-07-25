package sg.edu.nus.bombsquad;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

public class EditRoom extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        Global global = Global.getInstance();
        System.out.println(global.getQuestionName()[0]);
    }
}
