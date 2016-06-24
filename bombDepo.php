<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $bomb_name = $_POST["bomb_name"];
	$question_type = $_POST["question_type"];
	$question = $_POST["question"];
	$answer = $_POST["answer"];
	$time_limit = $_POST["time_limit"];
	$points_awarded = $_POST["points_awarded"];
	$points_deducted = $_POST["points_deducted"];
	$num_pass = $_POST["num_pass"];
	$user_id = $_POST["user_id"];
	
	$response = array();
	$response["success"] = false;
    
	$result = mysqli_query($con,"INSERT INTO Bomb_Depository (bomb_name, question_type, question, answer, time_limit, points_awarded, points_deducted, num_pass, user_id); 
      VALUES ('$bomb_name', '$question_type', '$question', '$answer', '$time_limit', '$points_awarded', '$points_deducted', '$num_pass', '$user_id')");
		  
	if($result == true){
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE!!!"}';
	}
	
	mysqli_close($con);

?>