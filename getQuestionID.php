<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = $_POST["room_code"];
    $statement = mysqli_prepare($con, "SELECT * FROM Room WHERE room_code = ?");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $deploy_status, $time_left, $player_id);
    
    $response = array(array(), array());
    $response["success"] = false;
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["room_id"] = $room_id;
		$response[$i]["room_name"] = $room_name;
		$response[$i]["user_id"] = $user_id;
		$response[$i]["room_code"] = $room_code;
		$response[$i]["question_id"] = $question_id;
	$response[$i]["deploy_status"] = $deploy_status;
		$response[$i]["time_left"] = $time_left;
		$response[$i]["player_id"] = $player_id;
		$i++;
    }
	mysqli_close($con);
    echo json_encode($response);
?>