<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$question_status = mysqli_real_escape_string($con, $_POST["question_status"]);
    
	$statement = mysqli_prepare($con, "UPDATE Room SET question_status = '$question_status' WHERE room_code = '$room_code' AND question_id = '$question_id'");
	mysqli_stmt_execute($statement);
	
	if ($question_status == 2 || $question_status == 3) {
		$statement1 = mysqli_prepare($con, "UPDATE Room SET deploy_status = 0 WHERE room_code = '$room_code' AND question_id = '$question_id'");
		mysqli_stmt_execute($statement1);
	}
	
	mysqli_close($con);
?>