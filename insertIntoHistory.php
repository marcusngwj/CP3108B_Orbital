<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");

	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$player_id = mysqli_real_escape_string($con, $_POST["player_id"]);
	$player_answer = mysqli_real_escape_string($con, $_POST["player_answer"]);
	
	$statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ?");
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
	
	$result = mysqli_query($con,"INSERT INTO History (game_id, host_id, bomb_id, bomb_name, question_type, 
	question, option_one, option_two, option_three, option_four, answer, player_id, player_answer) 
	VALUES ('$game_id', '$host', '$question_id', '$bomb_name', '$question_type', '$question', 
	'$option_one', '$option_two', '$option_three', '$option_four', '$answer', '$player_id', '$player_answer')");
	
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
   	mysqli_close($con);


?>