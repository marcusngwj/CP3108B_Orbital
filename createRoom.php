	<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_name = mysqli_real_escape_string($con, $_POST["room_name"]);
	$user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$deplay_status = mysqli_real_escape_string($con, $_POST["deplay_status"]);
	$time_left = mysqli_real_escape_string($con, $_POST["time_left"]);
	$player_id = mysqli_real_escape_string($con, $_POST["player_id"]);
    
	$result = mysqli_query($con,"INSERT INTO Room (room_name, user_id, room_code, question_id, deploy_status, time_left, player_id) 
          VALUES ('$room_name', '$user_id', '$room_code', '$question_id', '$deploy_status', '$time_left', '$player_id')");
 
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>