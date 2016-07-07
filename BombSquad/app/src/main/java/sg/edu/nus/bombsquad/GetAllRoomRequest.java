package sg.edu.nus.bombsquad;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class GetAllRoomRequest extends StringRequest{
    private static final String UNIQUE_CODE_REQUEST_URL = "http://orbitalbombsquad.x10host.com/getAllRoom.php";
    private Map<String, String> params;

    public GetAllRoomRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, UNIQUE_CODE_REQUEST_URL, listener, null);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
