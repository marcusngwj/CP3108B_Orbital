package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RoomRequest extends StringRequest {
    private static final String ROOM_REQUEST_URL = "http://orbitalbombsquad.x10host.com/room.php";
    private Map<String, String> params;

    public RoomRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, ROOM_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
