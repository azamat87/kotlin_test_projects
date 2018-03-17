<?php

require("DBInfo.inc");


if ($_GET['op'] == 1) {
    //case 1: search op=1, query =?, startFrom=20
    //http://localhost:/TwitterWebServer/TweetList.php?op=1&user_id=2$StartFrom=0
    $query = "select * from user_tweets where user_id IN (SELECT following_user_id FROM following WHERE user_id=". $_GET['user_id'] . ") 
    or user_id" . $_GET['user_id'] . " order by tweet_date DESC" . "LIMIT 20 OFFSET ".$_GET['StartFrom'];

} elseif ($_GET['op'] == 2){

    //case 2: search op=3, query =?, startFrom=20
    //http://localhost:/TwitterWebServer/TweetList.php?op=2&user_id=2$StartFrom=0
    $query = "select * from user_tweets where user_id=". $_GET['user_id'] . " order by tweet_date DESC" . "LIMIT 20 OFFSET ".$_GET['StartFrom'];

}else{
    //case 3: search op=3, query =?, startFrom=20
//http://localhost:/TwitterWebServer/TweetList.php?op=3&query=new&StartFrom=0

    $query = "select * from user_tweets where tweet_text LIKE '%". $_GET['query']."%' LIMIT 20 OFFSET ".$_GET['StartFrom'];
}


$result = mysqli_query($connect, $query);

if (!$result) {
    $output = "{'msg':'fail'}";
    die('Error cannot ru query');
}

$userTweets = array();
while ($row = mysqli_fetch_assoc($result)) {
    $userTweets[] = $row;
}

if ($userTweets) {
    print ("{'msg':'has tweet', 'info':'" . json_encode($userTweets) . "'}");
}else{
    print ("{'msg':'no tweet'}");
}

mssql_free_result($result);

print ($output);
mysqli_close($connect);