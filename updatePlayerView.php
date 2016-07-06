<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = $_POST["room_code"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ?");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
		$response["game_id"] = $game_id;
		$response["host"] = $host;
		$response["room_status"] = $room_status;
		$response["room_code"] = $room_code;
		$response["player"] = $player;
    }
    mysqli_close($con);
    echo json_encode($response);
?>