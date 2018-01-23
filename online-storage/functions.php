<?php

// Database connection information Fill in your values here.
$dbhost = "localhost";
$dbuser = "user";
$dbpwd = "password";
$dbname = "onlinestorage";
$dbport = null;

/**
 * Only a single table is needed in the database. Initialize it with the following query:
 * CREATE TABLE `storage` ( `storekey` varchar(200) NOT NULL, `value` varchar(4000) DEFAULT NULL)
 *     ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * ALTER TABLE `storage` ADD PRIMARY KEY (`storekey`) USING HASH;
 */

/**
 * Echoes error message in JSON format, stops script
 */
function error($msg) {
    echo '{ "success": "false", "error": "' . $msg . '" }';
    exit(1);
}

/**
 * Echoes successful result message in JSON format, stops script
 */
function success($msg) {
    $res = array("success" => "true", "value" => $msg);
    echo json_encode($res);
    exit(1);
}