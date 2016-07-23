package sg.edu.nus.bombsquad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RoomDetail {
    /*---------- Variables ----------*/
    String room_code;
    String room_id;
    String question_id;
    String deploy_status;
    String time_left;
    String player_id;   //Player holding onto the bomb

    /*---------- Constructor ----------*/
    public RoomDetail(String room_code, String room_id, String question_id, String deploy_status, String time_left, String player_id) {
        this.room_code = room_code;
        this.room_id = room_id;
        this.question_id = question_id;
        this.deploy_status = deploy_status;
        this.time_left = time_left;
        this.player_id = player_id;
    }



    /*---------- Setter ----------*/
    public void setRoomID(String room_id) { this.room_id = room_id; }

    public void setQuestionID(String question_id) { this.question_id = question_id; }

    public void setDeployStatus(String deploy_status) { this.deploy_status = deploy_status; }

    public void setTimeLeft(String time_left) { this.time_left = time_left; }

    public void setPlayerID(String player_id) { this.player_id = player_id; }






    /*---------- Getter ----------*/
    public String getRoomCode() { return room_code; }

    public String getRoomID() { return room_id; }

    public String getQuestionID() { return question_id; }

    public String getDeployStatus(final int i) {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder().add("room_code", room_code).build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getRoomDetail.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            deploy_status = result.getJSONObject(i + "").getString("deploy_status");
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                    }
                });

        return deploy_status;
    }

    //Get the timer from server
    public String getTimeLeft(final int i) {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder().add("room_code", room_code).build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getRoomDetail.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            time_left = result.getJSONObject(i + "").getString("time_left");
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                    }
                });

        return time_left;
    }

    //Get the id of the player who possesses the bomb
    public String getPlayerID(final int i) {
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder().add("room_code", room_code).build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/getRoomDetail.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            player_id = result.getJSONObject(i + "").getString("player_id");
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                    }
                });
        return player_id;
    }
}
