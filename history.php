<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
	$user_id = $_POST["user_id"];

    $statement = mysqli_prepare($con, "SELECT * FROM Room WHERE user_id = ?");
    mysqli_stmt_bind_param($statement, "s", $user_id);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $room_id, $room_name, $user_id, $room_code);
    
    $response = array(array(), array());
    $response["success"] = false;
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["room_id"] = $room_id;
		$response[$i]["room_name"] = $room_name;
		$response[$i]["user_id"] = $user_id;
		$response[$i]["room_code"] = $room_code;
		$i++;
    }
    
    echo json_encode($response);
?>