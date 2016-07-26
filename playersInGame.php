<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
    
    $statement = mysqli_prepare($con, "SELECT * FROM Game, user WHERE Game.room_code = '$room_code' AND Game.player = user.user_id");
    
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player, $user_id, $first_name, $last_name, $email, $mobile_no, $username, $password);
	
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
		
		$response[$i]["user_id"] = $user_id;
		$response[$i]["first_name"] = $first_name;
		$response[$i]["last_name"] = $last_name;
		$response[$i]["email"] = $email;
		$response[$i]["mobile_no"] = $mobile_no;
		$response[$i]["username"] = $username;
		$response[$i]["password"] = $password;
		
		$response["numRow"] = $num_rows;
		
		$i++;
    }
    mysqli_close($con);
	echo json_encode($response);
?>