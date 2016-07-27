<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$my_id = mysqli_real_escape_string($con, $_POST["my_id"]);
    
	$statement = mysqli_prepare($con, "SELECT * FROM Room, Bomb_Depository WHERE Room.room_code = '$room_code' AND Room.question_id = Bomb_Depository.question_id");
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code, $question_id, $deploy_status, $time_left, $player_id, $num_pass, $question_id, $bomb_name, $question_type, $question, $option_one, $option_two, $option_three, $option_four, $answer, $time_limit, $points_awarded, $points_deducted, $num_pass, $user_id);
	
    $response = array(array(), array());
    $response["success"] = false;
	$response["my_id"] = $my_id;	//id of the user
	$response["room_name"] = $room_name;
	$response["room_code"] = $room_code;
	
		
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["room_id"] = $room_id;
		$response[$i]["user_id"] = $user_id;		//user_id of host
		$response[$i]["question_id"] = $question_id;
		$response[$i]["deploy_status"] = $deploy_status;
		$response[$i]["time_left"] = $time_left;
		$response[$i]["player_id"] = $player_id;	//player_id who possesses the bomb
		$response[$i]["num_pass"] = $num_pass;
		$response[$i]["question_id"] = $question_id;
		
		$response[$i]["bomb_name"] = $bomb_name;
		$response[$i]["question_type"] = $question_type;
		$response[$i]["question"] = $question;
		$response[$i]["option_one"] = $option_one;
		$response[$i]["option_two"] = $option_two;
		$response[$i]["option_three"] = $option_three;
		$response[$i]["option_four"] = $option_four;
		$response[$i]["answer"] = $answer;
		$response[$i]["time_limit"] = $time_limit;
		$response[$i]["points_awarded"] = $points_awarded;
		$response[$i]["points deducted"] = $points_deducted;
		$response[$i]["num_pass"] = $num_pass;
		$response[$i]["user_id"] = $user_id;
		
		$i++;
    }
	
	$statement_two = mysqli_prepare($con, "SELECT total_score FROM Score WHERE user_id = '$my_id' AND room_code = '$room_code'");
	mysqli_stmt_execute($statement_two);
	mysqli_stmt_store_result($statement_two);
	mysqli_stmt_bind_result($statement_two, $total_score);
	
	if(mysqli_stmt_fetch($statement_two)){
		$response["total_score"] = $total_score;
	}
		
	
	mysqli_close($con);
    echo json_encode($response);
?>