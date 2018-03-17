<?php

require("DBInfo.inc");

//http://localhost:/TwitterWebServer/register.php?first_name=aza&email=aza@mail.ru&password=123456&picture_path=path/123.png

$query = "insert into login (first_name, email, password, picture_path) VALUES ('". $_GET['first_name']."', '". $_GET['email']."', '". $_GET['password']."', '". $_GET['picture_path']."')";

$result = mysqli_query($connect, $query);

if (!$result) {
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'user is added'}";
}

print ($output);
mysqli_close($connect);
