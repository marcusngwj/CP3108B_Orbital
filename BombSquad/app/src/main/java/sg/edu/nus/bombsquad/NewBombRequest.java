package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewBombRequest extends StringRequest {
    private static final String NEW_BOMB_REQUEST_URL = "http://orbitalbombsquad.comlu.com/bombDepo.php";
    private Map<String, String> params;

    public NewBombRequest(String bomb_name, String question_type, String question, String option_one, String option_two, String option_three, String option_four, String answer, String time_limit, String points_awarded, String points_deducted, String num_pass, String user_id, Response.Listener<String> listener){
        super(Request.Method.POST, NEW_BOMB_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("bomb_name", bomb_name);
        params.put("question_type", question_type);
        params.put("question", question);
        params.put("option_one", option_one);
        params.put("option_two", option_two);
        params.put("option_three", option_three);
        params.put("option_four", option_four);
        params.put("answer", answer);
        params.put("time_limit", time_limit);
        params.put("points_awarded",points_awarded);
        params.put("points_deducted", points_deducted);
        params.put("num_pass", num_pass);
        params.put("user_id", user_id);

    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
