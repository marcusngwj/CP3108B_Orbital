<?php
    $con = mysqli_connect("mysql12.000webhost.com", "a6020307_squad", "orbital123", "a6020307_squad");
    
	$user_id = $_POST["user_id"];
	$room_id = $_POST["room_id"];

    $statement = mysqli_prepare($con, "DELETE FROM Room WHERE user_id = ? AND room_id = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $room_id);
    mysqli_stmt_execute($statement);
	mysqli_close($con);
?>