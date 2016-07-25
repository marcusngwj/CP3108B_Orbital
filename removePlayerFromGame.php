<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$player = mysqli_real_escape_string($con, $_POST["player"]);
	
    $statement = mysqli_prepare($con, "DELETE FROM Game WHERE room_code = ? AND player = ?");
    mysqli_stmt_bind_param($statement, "ss", $room_code, $player);
    mysqli_stmt_execute($statement);
	mysqli_close($con);
?>