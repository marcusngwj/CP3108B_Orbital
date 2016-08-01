<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$player = mysqli_real_escape_string($con, $_POST["player"]);
    
    $statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ? AND player = ?");
    mysqli_stmt_bind_param($statement, "ss", $room_code, $player);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["room_status"] = $room_status;
    }
    mysqli_close($con);
	echo json_encode($response);
?>