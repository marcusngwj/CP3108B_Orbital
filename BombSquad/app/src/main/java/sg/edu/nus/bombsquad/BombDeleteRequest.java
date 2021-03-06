package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BombDeleteRequest extends StringRequest {
    private static final String BOMB_DELETE_REQUEST_URL = "http://orbitalbombsquad.x10host.com/bombDelete.php";
    private Map<String, String> params;

    public BombDeleteRequest(String user_id, String question_id, Response.Listener<String> listener) {
        super(Request.Method.POST, BOMB_DELETE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("question_id", question_id);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}