<?php
// set.php - Update command from Dashboard
header('Content-Type: application/json');
require 'db.php';

$key = isset($_GET['key']) ? $_GET['key'] : '';
$cmd = isset($_GET['cmd']) ? $_GET['cmd'] : '';

if ($key !== "1234") {
    echo json_encode(["status" => "error", "message" => "Unauthorized access"]);
    exit;
}

if (!empty($cmd)) {
    $stmt = $conn->prepare("UPDATE control SET command=? WHERE id=1");
    $stmt->bind_param("s", $cmd);
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Command updated to $cmd"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Update failed"]);
    }
    $stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "No command provided"]);
}

$conn->close();
?>
