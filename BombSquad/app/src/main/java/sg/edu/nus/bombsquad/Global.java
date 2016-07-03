package sg.edu.nus.bombsquad;


public class Global {
    private static Global instance;
    private String[] stringArr;
    private boolean[] booleanArr;

    public void setData(String[] arr) {
        stringArr = arr;
    }

    public void setData(boolean[] arr) {
        booleanArr = arr;
    }

    public String[] getStringArray() {
        return stringArr;
    }

    public boolean[] getBooleanArray() {
        return booleanArr;
    }

    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }
}
