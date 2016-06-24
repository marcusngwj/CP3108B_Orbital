package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EnterRoomRequest extends StringRequest{
    private static final String ENTER_ROOM_REQUEST_URL = "http://orbitalbombsquad.comlu.com/enterRoom.php";
    private Map<String, String> params;

    public EnterRoomRequest(String roomCode, Response.Listener<String> listener){
        super(Request.Method.POST, ENTER_ROOM_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("room_code", roomCode);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
