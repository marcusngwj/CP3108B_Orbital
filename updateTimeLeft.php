	<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$time_left = mysqli_real_escape_string($con, $_POST["time_left"]);
    
	$statement = mysqli_prepare($con, "UPDATE Room SET time_left = ? WHERE room_code = ? AND question_id = ?");
	mysqli_stmt_bind_param($statement, "sss", $time_left, $room_code, $question_id);
	mysqli_stmt_execute($statement);
	
	$statement1 = mysqli_prepare($con, "SELECT * FROM Room WHERE room_code = ? AND question_id = ?");
	mysqli_stmt_bind_param($statement1, "ss", $room_code, $question_id);
	mysqli_stmt_execute($statement1);
	mysqli_stmt_store_result($statement1);
    mysqli_stmt_bind_result($statement1, $room_id, $room_name, $user_id, $room_code, $question_id, $deploy_status, $time_left, $player_id, $num_pass, $question_status);
    mysqli_stmt_fetch($statement1);
	$response = array();
	$response["success"] = false;  
	while(mysqli_stmt_fetch($statement1)){
		$response["success"] = true;
		$response["deploy_status"] = $deploy_status;
	}
	
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>