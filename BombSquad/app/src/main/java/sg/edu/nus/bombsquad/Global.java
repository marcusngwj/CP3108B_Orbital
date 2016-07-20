package sg.edu.nus.bombsquad;


import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Global {
    private static Global instance;

    //Variables for temporary use
    private String[] stringArr;
    private String[][] string2DArr;
    private boolean[] booleanArr;
    private int[] intArr;
    private int number;
    private int counter;
    private boolean booleanVar;
    private String string;

    
    //Variables with defined purpose
    private String user_id;
    private String room_name;
    private String room_code;
    private String room_status;
    private int num_question;
    private String[] question_id = new String[100000];
    private String player_name;
    private String[] player_id = new String[100000];
    private String[] player_list;
    private boolean booleanAccess;
    private LinearLayout[] questionLayoutArray;





    //Modify
    public void setData(String[] arr) {
        stringArr = arr;
    }

    public void setData(String[][] arr) { string2DArr = arr; }

    public void setData(boolean[] arr) {
        booleanArr = arr;
    }

    public void setData(int[] arr) { intArr = arr; }

    public void setNumber(int integer) { number = integer; }

    public void setCounter(int newCount) { counter = newCount; }

    public void setBooleanVar(boolean var) { booleanVar = var; }

    public void setString(String line) { string = line; }

    public void setUserId(String id) { user_id = id; }

    public void setRoomName(String name) {
        room_name = name;
    }

    public void setRoomCode(String string) { room_code = string; }

    public void setRoomStatus(String string) {
        room_status = string;
    }

    public void setNumQuestion(int num) {
        num_question = num;
    }

    public void setQuestion_id(String[] arr) { question_id = arr; }

    public void setPlayerName (String name) {
        player_name = name;
    }

    public void setPlayerId (String[] list) {
        player_id = list;
    }

    public void setPlayerList (String[] list) {
        player_list = list;
    }

    public void setBooleanAccess(boolean bool) {
        booleanAccess = bool;
    }

    public void setQuestionLayoutArray(LinearLayout[] arr) { questionLayoutArray = arr; }











    //Retrieve
    public String[] getStringArray() {
        return stringArr;
    }

    public String[][] getString2DArr() { return  string2DArr; }

    public boolean[] getBooleanArray() {
        return booleanArr;
    }

    public int[] getIntArr() { return intArr; }

    public int getNumber() { return number; }

    public int getCounter() { return counter; }

    public boolean getBooleanVar() { return booleanVar; }

    public String getString() { return string; }

    public String getUserId() { return user_id; }

    public String getRoomName() {
        return room_name;
    }

    public String getRoomCode() { return room_code; }

    public String getRoomStatus() {
        return room_status;
    }

    public int getNumQuestion() {
        return num_question;
    }

    public String[] getQuestion_id() { return question_id; }

    public String getPlayerName() {
        return player_name;
    }

    public String[] getPlayerId() {
        return player_id;
    }

    public String[] getPlayerList() {
        return player_list;
    }

    public boolean getBooleanAccess() {
        return booleanAccess;
    }

    public LinearLayout[] getQuestionLayoutArray() { return questionLayoutArray; }















    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
