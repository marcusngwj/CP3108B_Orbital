<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $first_name = $_POST["first_name"];
    $last_name = $_POST["last_name"];
	$email = $_POST["email"];
	$mobile_no = $_POST["mobile_no"];
    $username = $_POST["username"];
    $password = $_POST["password"];
	
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
          VALUES ('$first_name', '$last_name', '$email', '$mobile_no', '$username', '$password')");
		  
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