<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $user_id = $_POST["player_id"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE user_id = ?");
    mysqli_stmt_bind_param($statement, "s", $user_id);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $user_id, $first_name, $last_name, $email, $mobile_no, $username, $password);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["first_name"] = $first_name;
		$response["last_name"] = $last_name;
    }
    mysqli_close($con);
	echo json_encode($response);
?>