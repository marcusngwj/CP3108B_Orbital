<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $room_name = $_POST["room_name"];
	$user_id = $_POST["user_id"];
	$room_code = $_POST["room_code"];
    
	$result = mysqli_query($con,"INSERT INTO Room (room_name, user_id, room_code) 
          VALUES ('$room_name', '$user_id', '$room_code')");
 
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>