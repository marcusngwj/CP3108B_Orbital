<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
    
    $statement = mysqli_prepare($con, "SELECT * FROM Bomb_Depository WHERE question_id = ?");
    mysqli_stmt_bind_param($statement, "s", $question_id);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $question_id, $bomb_name, $question_type, $question, 
	$option_one, $option_two, $option_three, $option_four, $answer, $time_limit, $points_awarded,
	$points_deducted, $num_pass, $user_id);
	
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["bomb_name"] = $bomb_name;
		$i++;
    }
    mysqli_close($con);
	echo json_encode($response);
?>