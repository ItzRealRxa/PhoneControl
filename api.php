<?php
// api.php - Fetch current command for Android App
header('Content-Type: application/json');
require 'db.php';

$key = isset($_GET['key']) ? $_GET['key'] : '';

if ($key !== "1234") {
    echo json_encode(["status" => "error", "message" => "Unauthorized access"]);
    exit;
}

$sql = "SELECT command FROM control WHERE id=1";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $command = $row['command'];
    
    echo json_encode(["command" => $command, "status" => "success"]);

    // Auto-reset command after execution (as requested)
    if ($command !== "none") {
        $conn->query("UPDATE control SET command='none' WHERE id=1");
    }
} else {
    echo json_encode(["command" => "none", "status" => "empty"]);
}

$conn->close();
?>
