<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $room_code = $_POST["room_code"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Room WHERE room_code = ?");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $time_left, $player_id);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["room_id"] = $room_id;
		$response["room_name"] = $room_name;
		$response["user_id"] = $user_id;
		$response["room_code"] = $room_code;
		$response["question_id"] = $question_id;
		$response["time_left"] = $time_left;
		$response["player_id"] = $player_id;
    }
    mysqli_close($con);
    echo json_encode($response);
?>