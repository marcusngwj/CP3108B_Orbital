<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = $_POST["room_code"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ?");
    mysqli_stmt_bind_param($statement, "s", $room_code);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
	
	$num_rows = mysqli_stmt_num_rows($statement);
    
    $response = array(array(), array());
    $response["success"] = false;  
    
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["game_id"] = $game_id;
		$response[$i]["host"] = $host;
		$response[$i]["room_status"] = $room_status;
		$response[$i]["room_code"] = $room_code;
		$response[$i]["player"] = $player;
		$response[$i]["numRow"] = $num_rows;
		$i++;
    }
    mysqli_close($con);
	echo json_encode($response);
?>