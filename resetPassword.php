<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$username = mysqli_real_escape_string($con, $_POST["username"]);
	$password = mysqli_real_escape_string($con, $_POST["password"]);
	
	$query = "SELECT username FROM user WHERE username='$username'";
	$res = mysqli_query($con, $query);
	$anything_found = mysqli_num_rows($res);
	
	$response = array();
	$response["success"] = false;
	
	if($anything_found >0) {
		$response["success"] = true;
		
		$timeTarget = 0.05; // 50 milliseconds 
		$cost = 8;
		do {
			$cost++;
			$start = microtime(true);
			$hash = password_hash($password, PASSWORD_BCRYPT, ["cost" => $cost]);
			$end = microtime(true);
		}while (($end - $start) < $timeTarget);
    
		$statement = mysqli_prepare($con, "UPDATE user SET password = '$hash' WHERE username = '$username'");
	
		$result = mysqli_query($con, "UPDATE user SET password = '$hash' WHERE username = '$username'");
	
		mysqli_stmt_execute($statement);
	}
	else {
		$response["success"] = false;
	}
	
	mysqli_close($con);
	echo json_encode($response);
?>