<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_status = mysqli_real_escape_string($con, $_POST["room_status"]);
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$player = mysqli_real_escape_string($con, $_POST["player"]);
	
	$statement = mysqli_prepare($con, "SELECT * FROM Game WHERE room_code = ? AND player = ?");
    mysqli_stmt_bind_param($statement, "ss", $room_code, $player);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player);
	
	if (!mysqli_stmt_fetch($statement)) {
    
		$result = mysqli_query($con,"INSERT INTO Game (room_status, room_code, player) 
			VALUES ('$room_status', '$room_code', '$player')");
 
	}
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);
?>