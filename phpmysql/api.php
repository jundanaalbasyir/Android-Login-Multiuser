<?php
 require_once 'Connection.php';
 $response = array();
 if(isset($_GET['action'])) {
    switch($_GET['action']){
        case 'signup':
        if(isValid(array('username','email','password'))) {
            $username = $_POST['username']; 
            $email = $_POST['email']; 
            $password = md5($_POST['password']);
            $status = $_POST['status'];
            
            $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
            $stmt->bind_param("ss", $username, $email);
            $stmt->execute();
            $stmt->store_result();
            if($stmt->num_rows == 0) {
                 //if user is new creating an insert
                $stmt = $conn->prepare("INSERT INTO users (username, email, password, status) VALUES (?, ?, ?, ?)");
                $stmt->bind_param("ssss", $username, $email, $password, $status);      
                //if the user is successfully added to the database 
                if($stmt->execute()){
                    $stmt = $conn->prepare("SELECT id, username, email FROM users WHERE username = ?"); 
                    $stmt->bind_param("s",$username);
                    $stmt->execute();
                    $stmt->bind_result($id, $username, $email);
                    $stmt->fetch();

                    $user = array(
                        'id'=>$id, 
                        'username'=>$username, 
                        'email'=>$email,
                        'status'=>$status
                    );
                    //adding the user data in response 
                    $response['error'] = false; 
                    $response['message'] = 'User registered successfully.'; 
                    $response['user'] = $user; 
                } else {
                    $response['error'] = true; 
                    $response['message'] = 'Unable to create user.'; 
                }
            } else {
                $response['error'] = true;
                $response['message'] = 'User already registered.';
            }
            $stmt->close();
        } else {
            $response['error'] = true; 
            $response['message'] = 'Incomplete data.';
        }
        break; 
        case 'login':
        if(isValid(array('username', 'password'))){
            //getting values 
            $username = $_POST['username'];
            $password = md5($_POST['password']); 
            
            //creating the check query 
            $stmt = $conn->prepare("SELECT id, username, email, status FROM users WHERE username = ? AND password = ?");
            $stmt->bind_param("ss",$username, $password);
            $stmt->execute();
            $stmt->store_result();
            
            if($stmt->num_rows > 0) {
                $stmt->bind_result($id, $username, $email, $status);
                $stmt->fetch();
                $user = array(
                    'id'=>$id, 
                    'username'=>$username, 
                    'email'=>$email,
                    'status'=>$status
                );
                $response['error'] = false; 
                $response['message'] = 'Login successfull'; 
                $response['user'] = $user; 
            }else{
                //if the user not found 
                $response['error'] = true; 
                $response['message'] = 'Invalid username or password';
            }
        } else {
            $response['error'] = true; 
            $response['message'] = 'Invalid data.';
        }
        break;
        default;
            $response['error'] = true; 
            $response['message'] = 'Invalid Action.';
        break;
    }
 } else {
    $response['error'] = true; 
    $response['message'] = 'Invalid Request.';
 }
 function isValid($params){
    foreach($params as $param) {
        //if the paramter is not available or empty
        if(isset($_POST[$param])) {
            if(empty($_POST[$param])){
                return false;
            }
        } else {
            return false;
        }
    }
    //return true if every param is available and not empty 
    return true; 
}
echo json_encode($response);
?>