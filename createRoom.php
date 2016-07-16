	<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $room_name = $_POST["room_name"];
	$user_id = $_POST["user_id"];
	$room_code = $_POST["room_code"];
	$question_id = $_POST["question_id"];
	$deplay_status = $_POST["deplay_status"];
	$time_left = $_POST["time_left"];
	$player_id = $_POST["player_id"];
    
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