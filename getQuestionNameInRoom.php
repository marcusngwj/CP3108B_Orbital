<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
    
    $statement = mysqli_prepare($con, "SELECT * FROM Room WHERE room_code = ?");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $deploy_status, $time_left, $player_id, $num_pass);
    
    $response = array(array(), array());
    $response["success"] = false;  
    
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$question_id;
		$statement1 = mysqli_prepare($con, "SELECT * FROM Bomb_Depository WHERE question_id = $question_id");
		mysqli_stmt_execute($statement1);
		mysqli_stmt_store_result($statement1);
		mysqli_stmt_bind_result($statement1, $question_id, $bomb_name, $question_type, $question, 
		$option_one, $option_two, $option_three, $option_four, $answer, $time_limit, $points_awarded,
		$points_deducted, $num_pass, $user_id);
		while(mysqli_stmt_fetch($statement1)) {
			$response[$i]["bomb_name"] = $bomb_name;
		}
		$i++;
    }
    mysqli_close($con);
	echo json_encode($response);
?>