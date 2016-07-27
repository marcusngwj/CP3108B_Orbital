<?php
//Find all players in game, randomly select a player (excluding myself and the host)
//Change possession of the bomb to this player, then decrease the number of passes by 1

    $con = mysqli_connect("orbitalbombsquad.x10host.com", "orbital2", "h3llo world", "orbital2_bombsquad");
    
	$room_code = mysqli_real_escape_string($con, $_POST["room_code"]);
	$question_id = mysqli_real_escape_string($con, $_POST["question_id"]);
	$my_id = mysqli_real_escape_string($con, $_POST["my_id"]);
    
	//Selecting any available players in the game
    $statement = mysqli_prepare($con, "SELECT * FROM Game, user WHERE Game.room_code = '$room_code' AND Game.player = user.user_id");
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $game_id, $host, $room_status, $room_code, $player, $user_id, $first_name, $last_name, $email, $mobile_no, $username, $password);
	
	$num_rows = mysqli_stmt_num_rows($statement);	//Number of available players
    
    $response = array(array(), array());
    $response["success"] = false;
	$response["numRow"] = $num_rows;	
	
	$host_arr = array();
	$host_arr[0] = "-1";
    
    $i = 0;
    while(mysqli_stmt_fetch($statement)){
        $response[$i]["success"] = true;
		$response[$i]["game_id"] = $game_id;
		$response[$i]["host"] = $host;
		$response[$i]["room_status"] = $room_status;
		$response[$i]["room_code"] = $room_code;
		$response[$i]["player"] = $player;				//id of players who are in the game
		
		$response[$i]["user_id"] = $user_id;			//id of players who are in the game
		$response[$i]["first_name"] = $first_name;
		$response[$i]["last_name"] = $last_name;
		$response[$i]["email"] = $email;
		$response[$i]["mobile_no"] = $mobile_no;
		$response[$i]["username"] = $username;
		$response[$i]["password"] = $password;
		
		if($host != $host_arr[0]){
			$host_arr[1] = $host;
		}
		
		$i++;
    }
	
	//If player_id_to_receive_bomb is same as my_id or host's id, repeat
	do {
		$index = rand(0, $num_rows-1);	//Choosing a random number from the indices of the array
		$player_id_to_receive_bomb = $response[$index]["player"];
		$player_first_name_to_receive_bomb = $response[$index]["first_name"];
		$player_last_name_to_receive_bomb = $response[$index]["last_name"];
	}while($player_id_to_receive_bomb==$my_id || $player_id_to_receive_bomb==$host_arr[1]);
	
	/*For checking only*/
	$response["my_id"] = $my_id;
	$response["host_zero"] = $host_arr[0];
	$response["host_id"] = $host_arr[1];
	$response["player_id_to_receive_bomb"] = $player_id_to_receive_bomb;
	$response["player_first_name_to_receive_bomb"] = $player_first_name_to_receive_bomb;
	$response["player_last_name_to_receive_bomb"] = $player_last_name_to_receive_bomb;
	
	//Extract the number of passes from Room with the given room_code and question_id
	$query = "SELECT num_pass FROM Room WHERE room_code = '$room_code' AND question_id = '$question_id'";
	$statement_one = mysqli_prepare($con, $query);
    mysqli_stmt_execute($statement_one);
    mysqli_stmt_store_result($statement_one);
    mysqli_stmt_bind_result($statement_one, $num_pass);
	
	if(mysqli_stmt_fetch($statement_one)){
		$response["num_pass"] = $num_pass;
	}
	
	//Update player_id in Room to the new id of the player who possesses the bomb
	$statement_two = mysqli_prepare($con, "UPDATE Room SET player_id = '$player_id_to_receive_bomb' WHERE room_code = '$room_code' AND question_id = '$question_id'");
	mysqli_stmt_execute($statement_two);
	
	//Decrease the number of passes by 1
	$num_pass = $num_pass - 1;
	$statement_three = mysqli_prepare($con, "UPDATE Room SET num_pass = '$num_pass' WHERE room_code = '$room_code' AND question_id = '$question_id'");
	mysqli_stmt_execute($statement_three);
		
	mysqli_close($con);
	echo json_encode($response);
?>