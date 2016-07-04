package sg.edu.nus.bombsquad;


public class Global {
    private static Global instance;
    private String[] stringArr;
    private boolean[] booleanArr;
    private String room_status;

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

    //Others
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
