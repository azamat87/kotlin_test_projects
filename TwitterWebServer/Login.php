<?php

require("DBInfo.inc");

//http://localhost:/TwitterWebServer/Login.php?email=aza@mail.ru&password=123456

$query = "select * from login where email='". $_GET['email']."' and password='". $_GET['password']."'";

$result = mysqli_query($connect, $query);

if (!$result) {
    $output = "{'msg':'fail'}";
    die('Error cannot ru query');
}

$userInfo = array();
while ($row = mysqli_fetch_assoc($result)) {
    $userInfo[] = $row;
    break;
}

if ($userInfo) {
    print ("{'msg':'pass login', 'info':'" . json_encode($userInfo) . "'}");
}else{
    print ("{'msg':'cannot login'}");
}

mssql_free_result($result);

print ($output);
mysqli_close($connect);