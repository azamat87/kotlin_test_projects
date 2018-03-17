<?php
require("DBInfo.inc");

//http://localhost:/TwitterWebServer/TweetAdd.php?user_id=2&tweet_text=hello&tweet_picture=path/123.png

$query = "insert into tweets (user_id, tweet_text, tweet_picture) VALUES ('". $_GET['user_id']."', '". $_GET['tweet_text']."', '". $_GET['tweet_picture']."')";

$result = mysqli_query($connect, $query);

if (!$result) {
    $output = "{'msg':'fail'}";
}else{
    $output = "{'msg':'tweet is added'}";
}

print ($output);
mysqli_close($connect);
