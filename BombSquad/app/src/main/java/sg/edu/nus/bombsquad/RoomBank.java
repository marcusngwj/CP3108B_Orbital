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
    ArrayList<RoomDetail> roomDetailList;
//    ArrayList<QuestionDetail> questionDetailList;
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
    public void setRoomDetailList(ArrayList<RoomDetail> rmDetailList) { this.roomDetailList = rmDetailList; }
//    public void setQuestionDetailList(ArrayList<QuestionDetail> qnDetailList) { this.questionDetailList = qnDetailList; }
    public void setQuestionIDList(ArrayList<String> questionIDList) { this.questionIDList = questionIDList; }
    public void setQuestionHashMap(HashMap<String, QuestionDetail> map) { this.questionHashMap = map; }
    public void setRoomDetailHashMap(HashMap<String, RoomDetail> map) { this.roomDetailHashMap = map; }
    public void setCurrentQuestion(String question_id) { this.currentQuestion = question_id; }

    /*---------- Getter ----------*/
    public String getRoomName() { return room_name; }
    public String getRoomCode() { return room_code; }
    public int getNumQuestion() { return numQuestion; }
    public ArrayList<RoomDetail> getRoomDetailList() { return roomDetailList; }
//    public ArrayList<QuestionDetail> getQuestionDetailList() { return questionDetailList; }
    public ArrayList<String> getQuestionIDList() { return questionIDList; }
    public HashMap<String, QuestionDetail> getQuestionHashMap() { return questionHashMap; }
    public HashMap<String, RoomDetail> getRoomDetailHashMap() { return roomDetailHashMap; }
    public String getCurrentQuestion() { return currentQuestion; }

}
