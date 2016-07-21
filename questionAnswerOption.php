<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$question_id = $_POST["question_id"];
	
    $statement = mysqli_prepare($con, "SELECT * FROM Bomb_Depository WHERE question_id = ?");
    mysqli_stmt_bind_param($statement, "s", $question_id);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $question_id, $bomb_name, $question_type, $question, 
	$option_one, $option_two, $option_three, $option_four, $answer, $time_limit, $points_awarded,
	$points_deducted, $num_pass, $user_id);
    
    $response = array(array(), array());
    $response["success"] = false;
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["question_id"] = $question_id;
		$response[$i]["bomb_name"] = $bomb_name;
		$response[$i]["question_type"] = $question_type;
		$response[$i]["question"] = $question;
		$response[$i]["option_one"] = $option_one;
		$response[$i]["option_two"] = $option_two;
		$response[$i]["option_three"] = $option_three;
		$response[$i]["option_four"] = $option_four;
		$response[$i]["answer"] = $answer;
		$response[$i]["time_limit"] = $time_limit;
		$response[$i]["points_awarded"] = $points_awarded;
		$response[$i]["points_deducted"] = $points_deducted;
		$response[$i]["num_pass"] = $num_pass;
		$response[$i]["user_id"] = $user_id;
		$i++;
    }
	mysqli_close($con);
    echo json_encode($response);
?>