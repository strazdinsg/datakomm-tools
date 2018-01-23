<?php

// To set up your application, copy this file to "dbconn.php" and fill in values for your database configuration


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
