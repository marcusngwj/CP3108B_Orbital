package sg.edu.nus.bombsquad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
    ArrayList<RoomDetail> roomDetailList;
    ArrayList<QuestionDetail> questionDetailList;
    ArrayList<String> questionIDList;
    ArrayList<String> playersInGameList;

    /*---------- Constructor ----------*/
    public RoomBank(String room_name, String room_code) {
        this.room_name = room_name;
        this.room_code = room_code;
    }

    /*---------- Setter ----------*/
    public void setNumQuestion(int numQn) { this.numQuestion = numQn; }
    public void setRoomDetailList(ArrayList<RoomDetail> rmDetailList) { this.roomDetailList = rmDetailList; }
    public void setQuestionDetailList(ArrayList<QuestionDetail> qnDetailList) { this.questionDetailList = qnDetailList; }
    public void setQuestionIDList(ArrayList<String> questionIDList) { this.questionIDList = questionIDList; }

    /*---------- Getter ----------*/
    public String getRoomName() { return room_name; }
    public String getRoomCode() { return room_code; }
    public int getNumQuestion() { return numQuestion; }
    public ArrayList<RoomDetail> getRoomDetailList() { return roomDetailList; }
    public ArrayList<QuestionDetail> getQuestionDetailList() { return questionDetailList; }
    public ArrayList<String> getQuestionIDList() { return questionIDList; }

    public ArrayList<String> getPlayersInGameList() {
        playersInGameList = new ArrayList<String>();

        OkHttpClient client = new OkHttpClient();
        RequestBody postData = new FormBody.Builder()
                .add("room_code", room_code)
                .build();
        Request request = new Request.Builder().url("http://orbitalbombsquad.x10host.com/playersInGame.php").post(postData).build();

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

                            int numPlayers = Integer.valueOf(result.getJSONObject(0+"").getString("numRow"));
                            System.out.println("numPlayers: " + numPlayers);

                            for(int i=0; i<numPlayers; i++){
                                String player_id = result.getJSONObject(i+"").getString("player");
                                System.out.println("player_id: " + player_id);
                                playersInGameList.add(player_id);
                            }
                        } catch (JSONException e) {
                            /*e.printStackTrace();*/
                        }

                    }
                });

        return playersInGameList;
    }
}
