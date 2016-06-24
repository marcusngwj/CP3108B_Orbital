package sg.edu.nus.bombsquad;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by user on 24/6/2016.
 */
public class HistoryRequest extends StringRequest {
    private static final String HISTORY_REQUEST_URL = "http://orbitalbombsquad.comlu.com/history.php";

    public HistoryRequest(String user_id, Response.Listener<String> listener) {
        super(Method.POST, HISTORY_REQUEST_URL, listener, null);
    }
}
