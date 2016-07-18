package sg.edu.nus.bombsquad;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateRoomRequest extends StringRequest {

    private static final String ROOM_CREATE_REQUEST_URL = "http://orbitalbombsquad.x10host.com/createRoom.php";
    private Map<String, String> params;

    public CreateRoomRequest(String userID, String room_name, String generatedCode, int question_id, int deploy_status, int time_left,
                             int player_id, Response.Listener<String> listener){
        super(Method.POST, ROOM_CREATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", userID);
        params.put("room_name", room_name);
        params.put("room_code", generatedCode);
        params.put("question_id", question_id+"");
        params.put("deploy_status", deploy_status+"");
        params.put("time_left", time_left+"");
        params.put("player_id", player_id+"");
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}