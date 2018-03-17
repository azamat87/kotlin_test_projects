<?php
require("DBInfo.inc");

//http://localhost:/TwitterWebServer/UserFollowing.php?op=1&user_id=1&following_user_id=2
//op = 1 following, op = 2 unsubscribe


if ($_GET['op'] == 1) {
    $query = "insert into following (user_id, following_user_id) VALUES ('" . $_GET['user_id'] . "', '" . $_GET['following_user_id'] . "')";
} elseif ($_GET['op'] == 2) {
    $query = "delete from following WHERE user_id='" . $_GET['user_id'] . " and fallowing_user_id=" . $_GET['following_user_id'];
}

$result = mysqli_query($connect, $query);
if (!$result) {
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'fallowing is updated'}";
}

print ($output);
mysqli_close($connect);