<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $first_name = $_POST["first_name"];
    $last_name = $_POST["last_name"];
	$email = $_POST["email"];
	$mobile_no = $_POST["mobile_no"];
    $username = $_POST["username"];
    $password = $_POST["password"];
    
	$result = mysqli_query($con,"INSERT INTO user (first_name, last_name, email, mobile_no, username, password) 
          VALUES ('$first_name', '$last_name', '$email', '$mobile_no', '$username', '$password')");
 
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>