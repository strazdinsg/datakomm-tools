<?php

// Gets a value by given key from the database.

require_once("functions.php");

if (!@include("dbconn.php")) {
    error("The system is not set up. Please, follow instructions in file dbconn.dist.php");
}


$conn = mysqli_connect($dbhost, $dbuser, $dbpwd, $dbname, $dbport);

if (!$conn) {
    error("Could not connect to database");
}

if (!isset($_REQUEST["key"])) {
    error("Key not specified");
}
$key = $_REQUEST["key"];
if (!$key) {
    error("Key not specified");
}

// Handle non-allowed characters properly
$key = mysqli_real_escape_string($conn, $key);


$stmt = $conn->prepare("SELECT value from storage WHERE storekey = ?");
if (!$stmt) {
    error("Error in preparing query");
}
$stmt->bind_param("s", $key);

$value = "";
if (!$stmt->execute() || !$stmt->bind_result($value)) {
    error("Could not execute select statement");
}

if (!$stmt->fetch()) {
    error("No value stored for given key");
}

$stmt->close();

success($value);