<?php
// db.php - Database connection settings
$host = "sql211.infinityfree.com"; 
$user = "if0_41440198";      
$pass = "2DtHFUnN97DE"; 
$dbname = "if0_41440198_prax_control"; 

$conn = new mysqli($host, $user, $pass, $dbname);

if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Connection failed"]));
}
?>
