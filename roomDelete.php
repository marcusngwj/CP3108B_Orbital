<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$user_id = $_POST["user_id"];
	$room_id = $_POST["room_id"];

    $statement = mysqli_prepare($con, "DELETE FROM Room WHERE user_id = ? AND room_id = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $room_id);
    mysqli_stmt_execute($statement);
	mysqli_close($con);
?>