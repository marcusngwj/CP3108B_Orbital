<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $statement = mysqli_prepare($con, "SELECT * FROM Room");
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $time_left, $player_id);
    
    $response = array(array(),array());
    $response["success"] = false; 
    
	$i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true; 
		$response[$i]["room_code"] = $room_code;
		$response[$i]["time_left"] = $time_left;
		$response[$i]["player_id"] = $player_id;
    }
    mysqli_close($con);
    echo json_encode($response);
?>