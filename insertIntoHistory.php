<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");

	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$player_id = mysqli_real_escape_string($con, $_POST["player_id"]);
	$player_answer = mysqli_real_escape_string($con, $_POST["player_answer"]);
	$correctness = mysqli_real_escape_string($con, $_POST["correctness"]);
	
	$statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ? LIMIT 1");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
	mysqli_stmt_fetch($statement);
	
	$statement1 = mysqli_prepare($con, "SELECT * FROM Bomb_Depository WHERE question_id = ?");
    mysqli_stmt_bind_param($statement1, "s", $question_id);
    mysqli_stmt_execute($statement1);
    mysqli_stmt_store_result($statement1);
    mysqli_stmt_bind_result($statement1, $question_id, $bomb_name, $question_type, $question, 
	$option_one, $option_two, $option_three, $option_four, $answer, $time_limit, $points_awarded,
	$points_deducted, $num_pass, $user_id);
	mysqli_stmt_fetch($statement1);
	
	$game_id = mysqli_real_escape_string($con, $game_id);
	$host = mysqli_real_escape_string($con, $host);
	$question_id = mysqli_real_escape_string($con, $question_id);
	$bomb_name = mysqli_real_escape_string($con, $bomb_name);
	$question_id = mysqli_real_escape_string($con, $question_id);
	$question = mysqli_real_escape_string($con, $question);
	$option_one = mysqli_real_escape_string($con, $option_one);
	$option_two = mysqli_real_escape_string($con, $option_two);
	$option_three = mysqli_real_escape_string($con, $option_three);
	$option_four = mysqli_real_escape_string($con, $option_four);
	$answer = mysqli_real_escape_string($con, $answer);
	$player_id = mysqli_real_escape_string($con, $player_id);
	$player_answer = mysqli_real_escape_string($con, $player_answer);
	
	
	$response = array();
	$response["game_id"] = $game_id;
	$response["host"] = $host;
	$response["question_id"] = $question_id;
	$response["bomb_name"] = $bomb_name;
	$response["question_type"] = $question_type;
	$response["question"] = $question;
	$response["option_one"] = $option_one;
	$response["option_two"] = $option_two;
	$response["option_three"] = $option_three;
	$response["option_four"] = $option_four;
	$response["answer"] = $answer;
	$response["player_id"] = $player_id;
	$response["player_answer"] = $player_answer;
	
	echo json_encode($response);
	
	$result = mysqli_query($con,"INSERT INTO History (game_id, host_id, room_code, bomb_id, bomb_name, question_type, question, option_one, option_two, option_three, option_four, answer, player_id, player_answer, correctness) 
	VALUES ('$game_id', '$host', '$room_code', '$question_id', '$bomb_name', '$question_type', '$question', '$option_one', '$option_two', '$option_three', '$option_four', '$answer', '$player_id', '$player_answer', '$correctness')");
	
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
   	mysqli_close($con);


?>