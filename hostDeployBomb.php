	<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = $_POST["room_code"];
	$question_id = $_POST["question_id"];
	$player_id = $_POST["player_id"];
    
	$result = mysqli_query($con,"UPDATE Room SET player_id = ? WHERE room_code = ? AND question_id = ?");
 
	if($result == true) {
		echo '{"query_result":"SUCCESS"}';
	}
	else{
		echo '{"query_result":"FAILURE"}';
	}
	
	mysqli_close($con);

?>