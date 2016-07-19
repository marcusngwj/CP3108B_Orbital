	<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = $_POST["room_code"];
	$question_id = $_POST["question_id"];
	$player_id = $_POST["player_id"];
    
	$statement = mysqli_prepare($con, "UPDATE Room SET player_id = ? WHERE room_code = ? AND question_id = ?");
	mysqli_stmt_bind_param($statement, "sss", $player_id, $room_code, $question_id);
	mysqli_stmt_execute($statement);
	
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>