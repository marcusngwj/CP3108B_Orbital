<?php
    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
    $user_id = mysqli_real_escape_string($con, $_POST["user_id"]);
    $room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$total_score = mysqli_real_escape_string($con, $_POST["total_score"]);
		
	$query = "SELECT * FROM Score WHERE user_id='$user_id' AND room_code='$room_code'";
	$res = mysqli_query($con, $query);
	$anything_found = mysqli_num_rows($res);
	
	//If not found, add new row
	if($anything_found <=0) {  
		$result = mysqli_query($con,"INSERT INTO Score (user_id, room_code, total_score) 
			VALUES ('$user_id', '$room_code', '$total_score')");
	}
	
	mysqli_close($con);
	
?>