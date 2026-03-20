<?php
// version.php - Current app version for auto-update check
header('Content-Type: application/json');

$current_version = "1.0"; // Increment this when you release a new APK
$download_url = "http://prxacontrol.xo.je/app.apk";

echo json_encode([
    "version" => $current_version,
    "url" => $download_url,
    "update_required" => false // Set to true to force update logs
]);
?>
