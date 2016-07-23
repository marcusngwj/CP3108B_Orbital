package sg.edu.nus.bombsquad;

import java.util.ArrayList;

//This class serves as a container to contain information from a same room_code
//  and the questions tied to that room_code
public class RoomBank {
    String room_name;
    String room_code;
    int numQuestion;
    ArrayList<RoomDetail> roomDetailList;
    ArrayList<QuestionDetail> questionDetailList;
    ArrayList<String> questionIDList;

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
}
