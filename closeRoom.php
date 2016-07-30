<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
    
	$statement = mysqli_prepare($con, "UPDATE Room SET player_id = 0, time_left = 0, question_status = 0 WHERE room_code = ?");
	mysqli_stmt_bind_param($statement, "s", $room_code);
	mysqli_stmt_execute($statement);
	
	$statement1 = mysqli_prepare($con, "DELETE FROM Game WHERE room_code = ?");
	mysqli_stmt_bind_param($statement1, "s", $room_code);
	mysqli_stmt_execute($statement1);
	
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);	

?>