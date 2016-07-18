package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RoomDeleteRequest extends StringRequest {
    private static final String ROOM_DELETE_REQUEST_URL = "http://orbitalbombsquad.x10host.com/roomDelete.php";
    private Map<String, String> params;

    public RoomDeleteRequest(String user_id, String room_code, Response.Listener<String> listener) {
        super(Request.Method.POST, ROOM_DELETE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("room_id", room_code);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
