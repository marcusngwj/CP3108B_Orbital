package sg.edu.nus.bombsquad;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GameRequest extends StringRequest {

    private static final String GAME_REQUEST_URL = "http://orbitalbombsquad.x10host.com/gameRequest.php";
    private Map<String, String> params;

    public GameRequest(String user_id, String room_status, String room_id, Response.Listener<String> listener) {
        super(Method.POST, GAME_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("room_status", room_status);
        params.put("room_id", room_id);
    }

    @Override
    public Map<String, String> getParams() {return params;}
}
