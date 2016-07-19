package sg.edu.nus.bombsquad;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Global {
    private static Global instance;
    private String[] stringArr;
    private String[][] string2DArr;
    private boolean[] booleanArr;
    private int[] intArr;
    private ArrayList<HashMap<String, String>> hashMapArrayList;
    private String room_status;
    private String[] question_id = new String[100000];
    private String room_code;
    private int number;
    private int counter;
    private boolean booleanVar;
    private String player_name;
    private String[] player_id = new String[100000];
    private Queue<String> player_list = new LinkedList<String>();

    //Modify
    public void setData(String[] arr) {
        stringArr = arr;
    }

    public void setData(String[][] arr) { string2DArr = arr; }

    public void setData(boolean[] arr) {
        booleanArr = arr;
    }

    public void setData(int[] arr) { intArr = arr; }

    public void setData(ArrayList<HashMap<String, String>> arr) { hashMapArrayList = arr; }

    public void setRoomStatus(String string) {
        room_status = string;
    }

    public void setQuestion_id(String[] arr) { question_id = arr; }

    public void setRoom_code(String string) { room_code = string; }

    public void setNumber(int integer) { number = integer; }

    public void setCounter(int newCount) { counter = newCount; }

    public void setBooleanVar(boolean var) { booleanVar = var; }

    public void setPlayerName (String name) {
        player_name = name;
    }

    public void setPlayerId (String[] list) {
        player_id = list;
    }


    //Retrieve
    public String[] getStringArray() {
        return stringArr;
    }

    public String[][] getString2DArr() { return  string2DArr; }

    public boolean[] getBooleanArray() {
        return booleanArr;
    }

    public int[] getIntArr() { return intArr; }

    public ArrayList<HashMap<String, String>> getHashMapArrayList() { return hashMapArrayList; }

    public String getRoomStatus() {
        return room_status;
    }

    public String[] getQuestion_id() { return question_id; }

    public String getRoom_code() { return room_code; }

    public int getNumber() { return number; }

    public int getCounter() { return counter; }

    public boolean getBooleanVar() { return booleanVar; }

    public String getPlayerName() {
        return player_name;
    }

    public String[] getPlayerId() {
        return player_id;
    }

    public Queue getPlayerList() {
        return player_list;
    }

    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
