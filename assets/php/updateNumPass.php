<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$num_pass = mysqli_real_escape_string($con, $_POST["num_pass"]);
    
	$statement = mysqli_prepare($con, "UPDATE Room SET num_pass = '$num_pass' WHERE room_code = '$room_code' AND question_id = '$question_id'");
	mysqli_stmt_execute($statement);
	
	mysqli_close($con);
?>