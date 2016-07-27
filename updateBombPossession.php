<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$player_id = mysqli_real_escape_string($con, $_POST["player_id"]);
	
	$query = "SELECT num_pass FROM Room WHERE room_code = '$room_code' AND question_id = '$question_id'";
	$statement_one = mysqli_prepare($con, $query);
    mysqli_stmt_execute($statement_one);
    mysqli_stmt_store_result($statement_one);
    mysqli_stmt_bind_result($statement_one, $num_pass);
	
	$response = array();
	if(mysqli_stmt_fetch($statement_one)){
		$response["num_pass"] = $num_pass;
	}

	$statement_two = mysqli_prepare($con, "UPDATE Room SET player_id = ? WHERE room_code = ? AND question_id = ?");
	mysqli_stmt_bind_param($statement_two, "sss", $player_id, $room_code, $question_id);
	mysqli_stmt_execute($statement_two);
	
	$num_pass = $num_pass - 1;
	$statement = mysqli_prepare($con, "UPDATE Room SET num_pass = '$num_pass' WHERE room_code = '$room_code' AND question_id = '$question_id'");
	mysqli_stmt_execute($statement);
		
	mysqli_close($con);
	echo json_encode($response);
?>