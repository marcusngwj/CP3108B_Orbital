package sg.edu.nus.bombsquad;

public class RoomDetail {
    /*---------- Variables ----------*/
    String room_id;
    String room_name;
    String user_id;
    String room_code;
    String question_id;
    String deploy_status;
    String time_left;
    String player_id;   //Player holding onto the bomb

    /*---------- Constructor ----------*/
    public RoomDetail(String room_id, String room_name, String user_id, String room_code, String question_id, String deploy_status,
                      String time_left, String player_id) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.user_id = user_id;
        this.room_code = room_code;
        this.question_id = question_id;
        this.deploy_status = deploy_status;
        this.time_left = time_left;
        this.player_id = player_id;
    }










    /*---------- Setter ----------*/
    public void setRoom_id(String room_id) { this.room_id = room_id; }

    public void setRoom_name(String room_name) { this.room_name = room_name; }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public void setRoom_code(String room_code) { this.room_code = room_code; }

    public void setQuestion_id(String question_id) { this.question_id = question_id; }

    public void setDeploy_status(String deploy_status) { this.deploy_status = deploy_status; }

    public void setTime_left(String time_left) { this.time_left = time_left; }

    public void setPlayer_id(String player_id) { this.player_id = player_id; }











    /*---------- Getter ----------*/

    public String getRoom_id() { return room_id; }

    public String getRoom_name() { return room_name; }

    public String getUser_id() { return user_id; }

    public String getRoom_code() { return room_code; }

    public String getQuestion_id() { return question_id; }

    public String getDeploy_status() { return deploy_status; }

    public String getTime_left() { return time_left; }

    public String getPlayer_id() { return player_id; }
}
