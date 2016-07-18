package sg.edu.nus.bombsquad;


public class Global {
    private static Global instance;
    private String[] stringArr;
    private boolean[] booleanArr;
    private String room_status;
    private String[] question_id = new String[100000];
    private String room_code;



    //Modify
    public void setData(String[] arr) {
        stringArr = arr;
    }

    public void setData(boolean[] arr) {
        booleanArr = arr;
    }

    public void setRoomStatus(String string) {
        room_status = string;
    }

    public void setQuestion_id(String[] arr) { question_id = arr; }

    public void setRoom_code(String string) { room_code = string; }



    //Retrieve
    public String[] getStringArray() {
        return stringArr;
    }

    public boolean[] getBooleanArray() {
        return booleanArr;
    }

    public String getRoomStatus() {
        return room_status;
    }

    public String[] getQuestion_id() { return question_id; }

    public String getRoom_code() { return room_code; }



    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
