<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = $_POST["room_code"];
    $question_id = $_POST["question_id"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Room WHERE room_code = ? AND question_id = ?");
    mysqli_stmt_bind_param($statement, "ss", $room_code, $question_id);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $deploy_status, $time_left, $player_id);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["time_left"] = $time_left;
		$response["player_id"] = $user_id;
    }
    echo json_encode($response);
?>