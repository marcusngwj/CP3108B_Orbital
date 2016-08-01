<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);

    $statement = mysqli_prepare($con, "DELETE FROM Bomb_Depository WHERE user_id = ? AND question_id = ?");
    mysqli_stmt_bind_param($statement, "ss", $user_id, $question_id);
    mysqli_stmt_execute($statement);
	mysqli_close($con);
?>