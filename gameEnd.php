<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$player = mysqli_real_escape_string($con, $_POST["player"]);
    
    $statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ? AND player = ?");
    mysqli_stmt_bind_param($statement, "ss", $room_code, $player);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
	
	$statement1 = mysqli_prepare($con, "SELECT * FROM Score WHERE room_code = ?");
    mysqli_stmt_bind_param($statement1, "s", $room_code);
    mysqli_stmt_execute($statement1);
    
    mysqli_stmt_store_result($statement1);
    mysqli_stmt_bind_result($statement1, $score_id, $user_id, $room_code, $total_score);
    
    $response = array(array(), array());
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["room_status"] = $room_status;
		$i = 0;
		while (mysqli_stmt_fetch($statement1)) {
			$response[$i]["player"] = $player;
			$response[$i]["user_id"] = $user_id;
			$statement2 = mysqli_prepare($con, "SELECT * FROM user WHERE user_id = $user_id");
			mysqli_stmt_execute($statement2);
			mysqli_stmt_store_result($statement2);
			mysqli_stmt_bind_result($statement2, $user_id, $first_name, $last_name, $email, $mobile_no, $username, $password);
			mysqli_stmt_fetch($statement2);
			$response[$i]["first_name"] = $first_name;
			$response[$i]["last_name"] = $last_name;
			$response[$i]["total_score"] = $total_score;
			$i++;
		}
    }
    mysqli_close($con);
	echo json_encode($response);
?>