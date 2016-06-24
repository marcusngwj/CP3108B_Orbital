package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 24/6/2016.
 */
public class HistoryRequest extends StringRequest {
    private static final String HISTORY_REQUEST_URL = "http://orbitalbombsquad.comlu.com/history.php";
    private Map<String, String> params;

    public HistoryRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, HISTORY_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
