<?php

// Saves a (key, value) pair into the database. If a value for that key already exists, it is overwritten

require_once("functions.php");

if (!include("dbconn.php")) {
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

if (!isset($_REQUEST["value"])) {
    error("Value not specified");
}
$value = $_REQUEST["value"];

// Handle non-allowed characters properly
$value = mysqli_real_escape_string($conn, $value);

$stmt = $conn->prepare("REPLACE INTO storage (storekey, value) VALUES (?, ?)");
if (!$stmt) {
    error("Error in preparing query");
}
$stmt->bind_param("ss", $key, $value);

if (!$stmt->execute()) {
    error("Could not insert data");
}

success("Data stored");