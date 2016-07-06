<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $user_id, $first_name, $last_name, $email, $mobile_no, $username, $password);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["user_id"] = $user_id;
		$response["first_name"] = $first_name;
		$response["last_name"] = $last_name;
		$response["email"] = $email;
		$response["mobile_no"] = $mobile_no;
		$response["username"] = $username;
		$response["password"] = $password;
    }
    echo json_encode($response);
?>