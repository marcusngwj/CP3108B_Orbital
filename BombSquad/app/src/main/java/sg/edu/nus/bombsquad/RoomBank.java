package sg.edu.nus.bombsquad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//This class serves as a container to contain information from a same room_code
//  and the questions tied to that room_code
//In short, another way of calling Room Bank, is Game
public class RoomBank {
    String room_name;
    String room_code;
    int numQuestion;
    ArrayList<String> questionIDList;
    HashMap<String, QuestionDetail> questionHashMap;
    HashMap<String, RoomDetail> roomDetailHashMap;
    String currentQuestion;

    /*---------- Constructor ----------*/
    public RoomBank(String room_name, String room_code) {
        this.room_name = room_name;
        this.room_code = room_code;
    }

    /*---------- Setter ----------*/
    public void setNumQuestion(int numQn) { this.numQuestion = numQn; }
    public void setQuestionIDList(ArrayList<String> questionIDList) { this.questionIDList = questionIDList; }
    public void setQuestionHashMap(HashMap<String, QuestionDetail> map) { this.questionHashMap = map; }
    public void setRoomDetailHashMap(HashMap<String, RoomDetail> map) { this.roomDetailHashMap = map; }
    public void setCurrentQuestion(String question_id) { this.currentQuestion = question_id; }

    /*---------- Getter ----------*/
    public String getRoomName() { return room_name; }
    public String getRoomCode() { return room_code; }
    public int getNumQuestion() { return numQuestion; }
    public ArrayList<String> getQuestionIDList() { return questionIDList; }
    public HashMap<String, QuestionDetail> getQuestionHashMap() { return questionHashMap; }
    public HashMap<String, RoomDetail> getRoomDetailHashMap() { return roomDetailHashMap; }
    public String getCurrentQuestion() { return currentQuestion; }

    /*---------- Others ----------*/
    public static void addPlayerIntoGame(String room_code, String user_id){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_status", "1")
                .add("room_code", room_code)
                .add("player", user_id)
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/insertUserIntoGame.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        response.body().close();
                    }
                });
    }

    public static void removePlayerFromGame(String room_code, String user_id){
        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", room_code)
                .add("player", user_id)
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/removePlayerFromGame.php").post(postData).build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("FAIL");
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        response.body().close();
                    }
                });
    }
}
