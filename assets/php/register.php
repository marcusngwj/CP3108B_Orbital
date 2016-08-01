<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $first_name = mysqli_real_escape_string($con, $_POST["first_name"]);
    $last_name = mysqli_real_escape_string($con, $_POST["last_name"]);
	$email = mysqli_real_escape_string($con, $_POST["email"]);
	$mobile_no = mysqli_real_escape_string($con, $_POST["mobile_no"]);
    $username = mysqli_real_escape_string($con, $_POST["username"]);
    $password = mysqli_real_escape_string($con, $_POST["password"]);
	
	$timeTarget = 0.05; // 50 milliseconds 
	$cost = 8;
	do {
		$cost++;
		$start = microtime(true);
		$hash = password_hash($password, PASSWORD_BCRYPT, ["cost" => $cost]);
		$end = microtime(true);
	}while (($end - $start) < $timeTarget);
	
	$query = "SELECT username FROM user WHERE username='$username'";
	$res = mysqli_query($con, $query);
	$anything_found = mysqli_num_rows($res);
	
	$response = array();
	$response["userexist"] = false;
    
	if($anything_found >0) {
		// echo 'Username already exist!';
		$response["userexist"] = true;
	}
	else{
		$result = mysqli_query($con,"INSERT INTO user (first_name, last_name, email, mobile_no, username, password) 
          VALUES ('$first_name', '$last_name', '$email', '$mobile_no', '$username', '$hash')");
		  
		if($result == true){
			echo '{"query_result":"SUCCESS"}';
		}
		else{
			echo '{"query_result":"FAILURE!!!"}';
		}
	}
	
	mysqli_close($con);
	
	echo json_encode($response);

?>