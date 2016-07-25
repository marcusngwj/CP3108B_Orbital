<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $bomb_name = mysqli_real_escape_string($con, $_POST["bomb_name"]);
	$question_type = mysqli_real_escape_string($con, $_POST["question_type"]);
	$question = mysqli_real_escape_string($con, $_POST["question"]);
	$option_one = mysqli_real_escape_string($con, $_POST["option_one"]);
	$option_two = mysqli_real_escape_string($con, $_POST["option_two"]);
	$option_three = mysqli_real_escape_string($con, $_POST["option_three"]);
	$option_four = mysqli_real_escape_string($con, $_POST["option_four"]);
	$answer = mysqli_real_escape_string($con, $_POST["answer"]);
	$time_limit = mysqli_real_escape_string($con, $_POST["time_limit"]);
	$points_awarded = mysqli_real_escape_string($con, $_POST["points_awarded"]);
	$points_deducted = mysqli_real_escape_string($con, $_POST["points_deducted"]);
	$num_pass = mysqli_real_escape_string($con, $_POST["num_pass"]);
	$user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
	
	$response = array();
    $response["success"] = false;  
    
	$result = mysqli_query($con,"INSERT INTO Bomb_Depository (bomb_name, question_type, question, option_one, option_two, option_three, option_four, answer, time_limit, points_awarded, points_deducted, num_pass, user_id) 
      VALUES ('$bomb_name', '$question_type', '$question', '$option_one', '$option_two', '$option_three', '$option_four', '$answer', '$time_limit', '$points_awarded', '$points_deducted', '$num_pass', '$user_id')");
		  
	if($result == true){
		echo '{"query_result":"SUCCESS"}';
		$response["success"] = true;
	}
	else{
		echo '{"query_result":"FAILURE!!!"}';
	}
	
	mysqli_close($con);
	
	echo json_encode($response);

?>