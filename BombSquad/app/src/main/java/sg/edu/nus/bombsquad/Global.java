package sg.edu.nus.bombsquad;


import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Global {
    private static Global instance;

    /*---------- Variables for temporary use ----------*/
    private String[] stringArr;
    private String[][] string2DArr;
    private boolean[] booleanArr;
    private int[] intArr;
    private int number;
    private int counter;
    private int counter1;
    private boolean booleanVar;
    private String string;

    /*---------- Variables with defined purpose ----------*/
    private String user_id; //DO NOT EDIT (Should remain constant throughout everything)
    private String room_name;
    private String room_code;
    private int num_question;
    private String[] question_id = new String[100000];
    private String player_name;
    private String[] player_id = new String[100000];
    private String[] player_list = new String[100000];
    private boolean booleanAccess;
    private boolean playerStatus;
    private int timeLeft;
    private String currQuestionId;
    private TextView[] hostViewPossession;
    private int[] playerPossessBomb;
    private int[] timeLefts;
    private TextView[] tvTimeLefts;
    private TextView[] tvQnStat;
    private boolean updateHostViewBoolean;
    private boolean runScheduler;
    private RoomBank roomBank;
    private boolean create;
    private String[] questionName;
    private boolean editRoomBoolean;
    private boolean error;


    /*---------- okgttp ----------*/
    private OkHttpClient client = new OkHttpClient();
    private Request.Builder requestBuilder = new Request.Builder();



    /*-------Qn Deployed ---------*/
    private HashMap<String, Boolean> deployedQ = new HashMap<>();

    /*------Player in room -------*/
    private HashMap<String, String> playerInRoom = new HashMap<>();

    /*------Player in room -------*/
    private HashMap<String, String> timeLimit = new HashMap<>();

    /*------ While loading -------*/
    private AlertDialog.Builder builder;



    /*-------- Qn Statuses -------*/
    private String[] qnStatus = {"Question is not deployed",
    "Question being deployed",
    "Bomb has been successfully defused",
    "Bomb has exploded",
    "Player has failed this question",
    "Upper limit of passes reached",
    "TO BE REPLACE BY ACTUAL"};
    private int[] qnStatusCode;



    /*---------- Modify ----------*/
    public void setData(String[] arr) { stringArr = arr; }

    public void setData(String[][] arr) { string2DArr = arr; }

    public void setData(boolean[] arr) { booleanArr = arr; }

    public void setData(int[] arr) { intArr = arr; }

    public void setNumber(int integer) { number = integer; }

    public void setCounter(int newCount) { counter = newCount; }

    public void setCounter1(int count) { counter1 = count;}

    public void setBooleanVar(boolean var) { booleanVar = var; }

    public void setString(String line) { string = line; }

    public void setUserId(String id) { user_id = id; }

    public void setRoomName(String name) { room_name = name; }

    public void setRoomCode(String string) { room_code = string; }

    public void setNumQuestion(int num) { num_question = num; }

    public void setQuestion_id(String[] arr) { question_id = arr; }

    public void setPlayerName (String name) { player_name = name; }

    public void setPlayerId (String[] list) { player_id = list; }

    public void setPlayerList (String[] list) { player_list = list; }

    public void setBooleanAccess(boolean bool) { booleanAccess = bool; }

    public void setPlayerExist(boolean bool) { playerStatus = bool; }

    public void setTimeLeft(int time) { timeLeft = time; }

    public void setCurrQuestionId(String id) { currQuestionId = id; }

    public void setHostViewPossession(TextView[] tv) { hostViewPossession = tv; }

    public void setPlayerPossessBomb(int[] arr) { playerPossessBomb = arr; }

    public void setTimeLefts(int[] arr) { timeLefts = arr; }

    public void setTvTimeLefts(TextView[] arr) { tvTimeLefts = arr; }

    public void setTvQnStat(TextView[] arr) {
        tvQnStat = arr;
    }

    public void setUpdateHostViewBoolean(Boolean bool) { updateHostViewBoolean = bool; }

    public void setRunScheduler(Boolean bool) { runScheduler = bool; }

    public void setRoomBank(RoomBank roomBank) { this.roomBank = roomBank; }

    public void setDeployedQ(String key, Boolean value) {
        deployedQ.put(key, value);
    }

    public void undeployQ(String key) {
        deployedQ.remove(key);
    }

    public void clearDeployed() {
        deployedQ.clear();
    }

    public void pushPlayerInRoom(String id, String name) {
        playerInRoom.put(id, name);
    }

    public void removePlayerInRoom(String id) {
        playerInRoom.remove(id);
    }

    public void putTimeLimit(String key, String value) {
        timeLimit.put(key, value);
    }

    public void setCreate(boolean bool) {
        create = bool;
    }

    public void setAlert(AlertDialog.Builder build) {
        builder = build;
    }

    public void setQuestionName(String[] arr) {
        questionName = arr;
    }

    public void setEditRoomBoolean(boolean bool) {
        editRoomBoolean = bool;
    }

    public void setError(boolean bool) {
        error = bool;
    }

    public void setQnStatusCode(int[] arr){
        qnStatusCode = arr;
    }




    /*---------- Retrieve ----------*/
    public String[] getStringArray() { return stringArr; }

    public String[][] getString2DArr() { return  string2DArr; }

    public boolean[] getBooleanArray() { return booleanArr; }

    public int[] getIntArr() { return intArr; }

    public int getNumber() { return number; }

    public int getCounter() { return counter; }

    public int getCounter1() { return counter1; }

    public boolean getBooleanVar() { return booleanVar; }

    public String getString() { return string; }

    public String getUserId() { return user_id; }

    public String getRoomName() { return room_name; }

    public String getRoomCode() { return room_code; }

    public int getNumQuestion() { return num_question; }

    public String[] getQuestion_id() { return question_id; }

    public String getPlayerName() { return player_name; }

    public String[] getPlayerId() { return player_id; }

    public String[] getPlayerList() { return player_list; }

    public boolean getBooleanAccess() { return booleanAccess; }

    public boolean playerExist() { return playerStatus; }

    public int getTimeLeft() { return timeLeft; }

    public String getCurrQuestionId() { return currQuestionId; }

    public TextView[] getHostViewPossession() { return hostViewPossession; }

    public int[] getPlayerPossessBomb() { return playerPossessBomb; }

    public int[] getTimeLefts() { return timeLefts; }

    public TextView[] getTvTimeLefts() { return tvTimeLefts; }

    public TextView[] getTvQnStat() {
        return tvQnStat;
    }

    public boolean getUpdateHostViewBoolean() { return updateHostViewBoolean; }

    public boolean getRunScheduler() { return runScheduler; }

    public OkHttpClient getClient() { return client; }

    public Request.Builder getRBuilder() { return requestBuilder; }

    public RoomBank getRoomBank() { return roomBank; }

    public Boolean isDeployedQ(String key) {
        return deployedQ.containsKey(key);
    }

    public String getPlayerInRoom(String id) {
        return playerInRoom.get(id);
    }

    public Boolean existDeployedQ() {
        return !deployedQ.isEmpty();
    }

    public String getTimeLimit(String id) {
        return timeLimit.get(id);
    }

    public boolean getCreate() {
        return create;
    }

    public AlertDialog.Builder getAlert() {
        return builder;
    }

    public String[] getQuestionName() {
        return questionName;
    }

    public boolean getEditRoomBoolean() {
        return editRoomBoolean;
    }

    public boolean getError() {
        return error;
    }

    public String[] getQnStatus() {
        return qnStatus;
    }

    public int[] getQnStatusCode() {
        return qnStatusCode;
    }



    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}