<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);

    $statement = mysqli_prepare($con, "DELETE FROM Room WHERE user_id = ? AND room_code = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $room_code);
    mysqli_stmt_execute($statement);
	mysqli_close($con);
?>