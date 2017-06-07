var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mysql = require('mysql');

app.use(bodyParser.urlencoded({ extended : true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;
var router = express.Router();

// create mysql connection
var connection = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: 'admin',
	database: 'potlocator'
});

connection.connect();

router.use(function(req, res, next) {
	console.log('processing request');
	next();
});

//default route

router.get('/', function(req, res) {
		res.json({message: 'Welcome to Potloicator !!!'});	
});

router.get('/login', function(req, res) {
		var isValidUser = false;
		var uname = req.param('username');
		var pwd = req.param('password');
		var sql    = 'select * from user where username = ' + connection.escape(uname) + ' and password = ' + connection.escape(pwd);
		connection.query(sql, function(err, rows, fields) { 
			if(err) throw err;
			
			var user = {};
			console.log(rows.length != 0);
			if(rows.length != 0)
			{
				
				user.id = rows[0].id;
				user.name = rows[0].first_name;
				user.is_contractor = rows[0].is_contractor == 'Y' ? true : false;
				user.isValidUser = true;
				
			}
			else if (rows.length == 0)
			{
				user.isValidUser = false;
			}
			res.json({count: rows.length, data: user});
		});
			
});

router.post('/register', function(req, res) {
	var first_name = req.param('first_name');
	var last_name = req.param('last_name');
	var email = req.param('email');
	var username = req.param('username');
	var pass = req.param('password');
	var sql = 'insert into user SET ?'
	var params  = {first_name: first_name, last_name : last_name, email: email, username: username, password:pass};

	var query = connection.query(sql, params, function(err, result) {
  		var successfull = true;
  		if(err) {
  			successfull = false;
  		}

		res.json({data: successfull});	
	});
});

router.post('/potlocation', function(req, res) {
	var latitude = req.param('latitude');
	var longitude = req.param('longitude');
	var userId = req.param('user_id');
	var sql = 'insert into potlocation SET ?'
	var params  = {latitude: latitude, longitude : longitude, reported_by: userId};

	var query = connection.query(sql, params, function(err, result) {
  		var successfull = true;
  		if(err) {
  			successfull = false;
  		}

		res.json({data: successfull});	
	});
});

router.get('/potlocation', function(req, res) {
	var uid = req.param('userid');
	var sql = 'select * from potlocation where reported_by = ' + connection.escape(uid);
	connection.query(sql, function(err, rows, fields) {
		if(err) throw err
		var potlocations = [];	

		for(var i = 0; i < rows.length; i++) {
			var potlocation = {};
			potlocation.id = rows[i].id;
			potlocation.latitude = rows[i].latitude;
			potlocation.longitude = rows[i].longitude;
			potlocation.reportedon = rows[i].reported_on;
			potlocation.isrepaired = rows[i].is_repaired == 'Y' ? true : false;
			potlocations.push(potlocation);
		}
		console.log(rows);
		res.json({count: rows.length, data: potlocations});	
	});	
})

router.get('/allpotlocation', function(req, res) {
	
	var sql = 'select * from potlocation';
	connection.query(sql, function(err, rows, fields) {
		if(err) throw err
		var potlocations = [];	

		for(var i = 0; i < rows.length; i++) {
			var potlocation = {};
			potlocation.id = rows[i].id;
			potlocation.latitude = rows[i].latitude;
			potlocation.longitude = rows[i].longitude;
			potlocation.reportedon = rows[i].reported_on;
			potlocation.isrepaired = rows[i].is_repaired == 'Y' ? true : false;
			potlocations.push(potlocation);
		}
		console.log(rows);
		res.json({count: rows.length, data: potlocations});	
	});	
})

router.put('/updatepotstatus', function(req, res) {
	var potid = req.param('potid');
	var isrepaired = req.param('isrepaired') == true || req.param('isrepaired') == 'true' ? true : false;
	var sql = 'update potlocation set is_repaired = '+ isrepaired +' where id = '+ potid;
	console.log(sql);
	var query = connection.query(sql, function(err, result) {
  		var successfull = true;
  		if(err) {
  			console.log(err);
  			successfull = false;
  		}

		res.json({data: successfull});	
	});
});

router.get('/user', function(req, res) {
	var solution;
	connection.query('select * from user', function(err, rows, fields) {
		if(err) throw err
		console.log(rows);
		res.json({data: rows});	
	});
});

// calling every api with /api
app.use('/api', router);


app.listen(port);

console.log('Server is listening on port:' + port);