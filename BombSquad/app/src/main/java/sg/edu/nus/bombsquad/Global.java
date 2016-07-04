package sg.edu.nus.bombsquad;


public class Global {
    private static Global instance;
    private String[] stringArr;
    private boolean[] booleanArr;
    private String room_status;
    private String question_id;
    private String room_id;

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

    public void setQuestion_id(String string) { question_id = string; }

    public void setRoom_id(String string) { room_id = string; }


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

    public String getQuestion_id() { return question_id; }

    public String getRoom_id() { return room_id; }


    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
