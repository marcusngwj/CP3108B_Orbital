<?php
//Update total score in Score table
//correct: total_score += points
//wrong: total_score -= points
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$answer_status = mysqli_real_escape_string($con, $_POST["answer_status"]);
	$points = mysqli_real_escape_string($con, $_POST["points"]);
	$user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	
	$query = "SELECT total_score FROM Score WHERE user_id = '$user_id' AND room_code = '$room_code'";
	$statement_one = mysqli_prepare($con, $query);
    mysqli_stmt_execute($statement_one);
    mysqli_stmt_store_result($statement_one);
    mysqli_stmt_bind_result($statement_one, $total_score);
	
	$response = array();
	if(mysqli_stmt_fetch($statement_one)){
		$response["total_score"] = $total_score;
	}
	
	if($answer_status=="correct"){
		$total_score += $points;
	}
	else{
		$total_score -= $points;
	}
	
	$statement_two = mysqli_prepare($con, "UPDATE Score SET total_score = '$total_score' WHERE user_id = '$user_id' AND room_code = '$room_code'");
	mysqli_stmt_execute($statement_two);
		
	mysqli_close($con);
	echo json_encode($response);
?>