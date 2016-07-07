<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
    $host = $_POST["user_id"];
	$room_status = $_POST["room_status"];
	$room_id = $_POST["room_id"];

	
	$response = array();
    $response["success"] = false;  
    
	$result = mysqli_query($con,"INSERT INTO Game (host, room_status, room_id, player) 
      VALUES ('$host', '$room_status', '$room_id', null)");
		  
	if($result == true){
		echo '{"query_result":"SUCCESS"}';
		$response["success"] = true;
	}
	else{
		echo '{"query_result": fail"}';
	}
	
	mysqli_close($con);
	
	echo json_encode($response);

?>