var express = require('express');
var router = express.Router();
var url = require('url');
var mysql = require('mysql')

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*'); //security
    var querydata = url.parse(req.url, true).query;
    var config = {
        host: '127.0.0.1',
        port: '3306',
        user: 'root',
        password: '',
        database: 'demo'
    };

    var connection = mysql.createConnection(config);
    connection.connect();

    // var myQuery = "insert into login(user_name, password) values('" + querydata.username + "','" + querydata.passwordusers + "')";
    // connection.query(myQuery,
    //     function (err, rows, fields) {
    //         if (err){
    //             // res.send('error cannot add');
    //             res.send(err);
    //         }else {
    //             res.send('data is added');
    //         }
    //     });

    var myQuery = "select * from login";
    connection.query(myQuery,
        function (err, rows, fields) {
            if (err){
                res.send('error cannot add');
            }else {
                res.send(rows);
            }
        });
    connection.end();
  //res.send('welcome to node');
});

module.exports = router;
