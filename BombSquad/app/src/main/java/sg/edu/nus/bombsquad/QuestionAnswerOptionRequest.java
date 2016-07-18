package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcus on 18/7/2016.
 */
public class QuestionAnswerOptionRequest extends StringRequest {
    private static final String QUESTION_ANSWEROPTION_REQUEST_URL = "http://orbitalbombsquad.x10host.com/questionAnswerOption.php";
    private Map<String, String> params;

    public QuestionAnswerOptionRequest(String question_id, Response.Listener<String> listener) {
        super(Request.Method.POST, QUESTION_ANSWEROPTION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("question_id", question_id);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
