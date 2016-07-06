<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $bomb_name = $_POST["bomb_name"];
	$question_type = $_POST["question_type"];
	$question = $_POST["question"];
	$option_one = $_POST["option_one"];
	$option_two = $_POST["option_two"];
	$option_three = $_POST["option_three"];
	$option_four = $_POST["option_four"];
	$answer = $_POST["answer"];
	$time_limit = $_POST["time_limit"];
	$points_awarded = $_POST["points_awarded"];
	$points_deducted = $_POST["points_deducted"];
	$num_pass = $_POST["num_pass"];
	$user_id = $_POST["user_id"];
	
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