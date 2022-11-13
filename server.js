process.env.TZ = "Asia/bangkok";
let express = require("express");
let app = express();
let bodyParser = require("body-parser");
let mysql = require("mysql");

const path = require("path");
const { error } = require("console");

let multer = require("multer");
const upload = multer({ dest: "uploads/" });

const fileUpload = require("express-fileupload");

//=================== get ipv4 addres ==========================
("use strict");
const { networkInterfaces } = require("os");
const nets = networkInterfaces();
const results_ipv4 = Object.create(null); // Or just '{}', an empty object
for (const name of Object.keys(nets)) {
  for (const net of nets[name]) {
    // Skip over non-IPv4 and internal (i.e. 127.0.0.1) addresses
    // 'IPv4' is in Node <= 17, from 18 it's a number 4 or 6
    const familyV4Value = typeof net.family === "string" ? "IPv4" : 4;
    if (net.family === familyV4Value && !net.internal) {
      if (!results_ipv4[name]) {
        results_ipv4[name] = [];
      }
      results_ipv4[name].push(net.address);
    }
  }
  // console.log(results_ipv4["Ethernet"]);
  console.log(results_ipv4);
}
//=================== get ipv4 addres ==========================

app.use(express.static("public"));

app.use(express.json());
app.use(
  express.urlencoded({
    extended: true,
  })
);
app.use(fileUpload());

app.use("/uploads", express.static("./uploads"));

app.get("/", function (req, res) {
  return res.send({ error: true, message: "Test Soccercube Web API" });
});

let dbConn = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database: "soccercube",
});

dbConn.connect();

//ประวัติการจองรวมของเเต่ละ user
app.get("/reserved/:id", function (req, res) {
  let user_id = req.params.id;
  if (!user_id) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide user id" });
  }
  dbConn.query(
    "SELECT reserves.reserve_id, reserves.user_id, reserves.reserve_date, reserves.time_start, reserves.time_end, reserves.total_price, payment.slip_img, payment.payment_status, reserves.payment_id, stadium.stadium_name, stadium.stadium_img FROM reserves INNER JOIN payment ON reserves.payment_id = payment.payment_id INNER JOIN stadium ON reserves.stadium_id = stadium.stadium_id WHERE user_id = " +
      user_id,
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "User id Not Found!!" });
      }
    }
  );
});

//ประวัติการจองรวมของ Admin
app.get("/reserved-admin/", function (req, res) {
  dbConn.query(
    "SELECT reserves.reserve_id, reserves.user_id, reserves.reserve_date, reserves.time_start, reserves.time_end, reserves.total_price, payment.slip_img, payment.payment_status, reserves.payment_id, stadium.stadium_name, stadium.stadium_img FROM reserves INNER JOIN payment ON reserves.payment_id = payment.payment_id INNER JOIN stadium ON reserves.stadium_id = stadium.stadium_id",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "User id Not Found!!" });
      }
    }
  );
});

//select reserved cancel Admin
app.get("/reserved-cancel/", function (req, res) {
  dbConn.query(
    "SELECT reserves.reserve_id, reserves.user_id, reserves.reserve_date, reserves.time_start, reserves.time_end, reserves.total_price, payment.slip_img, payment.payment_status, reserves.payment_id, stadium.stadium_name, stadium.stadium_img FROM reserves INNER JOIN payment ON reserves.payment_id = payment.payment_id INNER JOIN stadium ON reserves.stadium_id = stadium.stadium_id where payment_status = 2",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "User id Not Found!!" });
      }
    }
  );
});

//ยืนยันการจอง
app.get("/reserved-confirm/", function (req, res) {
  dbConn.query(
    "SELECT reserves.reserve_id, reserves.user_id, reserves.reserve_date, reserves.time_start, reserves.time_end, reserves.total_price, payment.slip_img, payment.payment_status, reserves.payment_id, stadium.stadium_name, stadium.stadium_img FROM reserves INNER JOIN payment ON reserves.payment_id = payment.payment_id INNER JOIN stadium ON reserves.stadium_id = stadium.stadium_id where payment_status = 4",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "User id Not Found!!" });
      }
    }
  );
});

//ยกเลิกการจองลูกค้า
app.put("/cancelReserved/:payment_id", function (req, res) {
  let payment_id = req.params.payment_id;
  let data = req.body;
  let payment_status = req.body.payment_status;
  if (!payment_id || !data) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide payment id and data" });
  }
  dbConn.query(
    "UPDATE payment SET payment_status = ? WHERE payment_id = ?",
    [payment_status, payment_id],
    function (error, results, fields) {}
  );

  dbConn.query(
    "UPDATE reserves SET delete_at = '2022-10-10 15:11:54' WHERE payment_id = ?",
    [payment_id],
    function (error, results, fields) {
      if (error) throw error;

      return res.send({
        error: false,
        message: "User has been updated seccessfully",
      });
    }
  );
});

//ดูProfile
app.get("/userDetail/:id", function (req, res) {
  let user_id = req.params.id;
  if (!user_id) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide user id" });
  }
  dbConn.query(
    "SELECT * FROM users WHERE user_id = ?",
    [user_id],
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "User id Not Found!!" });
      }
    }
  );
});

//แก้ใขโปรไฟล์
app.put("/edit_profile/:id", function (req, res) {
  let user_id = req.params.id;
  let user = req.body;
  var n = 0;
  if (!user_id || !user) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide user id and user data" });
  }
  dbConn.query(
    "SELECT * FROM users WHERE user_email = '" +
      req.body.user_email +
      "' AND user_id != " +
      user_id,
    function (error, results1, fields) {
      
      if (JSON.parse(JSON.stringify(results1)).length >= 1) {
        console.log(JSON.parse(JSON.stringify(results1)).length,"aa");
        return res
          .status(400)
          .send({ error: true, message: "Please provide user id" });
      } else {
        console.log(JSON.parse(JSON.stringify(results1)).length,"bb");
        dbConn.query(
          "UPDATE users SET ? WHERE user_id = ?",
          [user, user_id],
          function (error, results, fields) {
            if (error) throw error;

            return res.send({
              error: false,
              message: "User has been updated seccessfully",
            });
          }
        );
      }
    }
  );
});

//register
app.post("/register", function (req, res) {
  check = "SELECT * FROM users Group BY users.user_email  Having Count(*) > 1";
  console.log(check);
  let user = req.body;
  if (check == 0) {
    if (!user) {
      return res
        .status(400)
        .send({ error: true, message: "Please provide user " });
    }
    dbConn.query(
      "INSERT INTO users SET ? ",
      user,
      function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
      }
    );
  } else {
    console.log("ข้อมูลซ้ำ");
    return res.status(400).send({ error: true, message: "ข้อมูลซ้ำ" });
  }
});

//login
app.get("/login/:user/:pass", function (req, res) {
  let username = req.params.user;
  let password = req.params.pass;
  if (!username || !password) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide user id" });
  }
  dbConn.query(
    "SELECT * FROM users WHERE user_email = ? AND user_password = ? ",
    [username, password],
    function (error, results, fields) {
      if (error) throw error;
      if (results[0]) {
        return res.send(results[0]);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "eMail Not Found!!" });
      }
    }
  );
});

//Add Bank
app.post("/add", function (req, res) {
  let file = req.files.image;

  if (!req.files || Object.keys(req.files).length === 0) {
    return res.status(400).send("No files were uploaded.");
  }
  var imgName = file.name;
  if (!imgName) {
    return res
      .status(400)
      .send({ error: true, message: "sNo files were uploaded." });
  }
  console.log(imgName);

  uploadPath = __dirname + "/uploads/" + file.name;
  // console.log(uploadPath.split('/uploads')[1])

  var imgsrc = "/uploads/" + file.name;
  // var imgsrc =
  // "http://" + results_ipv4["Wi-Fi"][0] + ":3000/uploads/" + file.name;
  // var imgsrc = 'http://127.0.0.1:3000/uploads/' + uploadPath.split('/uploads')[1]

  file.mv(uploadPath, function (err) {
    if (err) return res.status(500).send(err);

    // res.send('File uploaded!');
  });

  sql =
    "INSERT INTO bank_admins (bank_name, account_number, firstName, lastName, qr_code) VALUES (?, ?, ?, ?, ?)";

  data = [
    req.body.bank_name,
    req.body.account_number,
    req.body.firstName,
    req.body.lastName,
    imgsrc,
  ];

  dbConn.query(sql, data, function (error, results, fields) {
    if (error) throw error;
    if (results) {
      return res.send(results);
    } else {
      return res
        .status(400)
        .send({ error: true, message: "bank_admins id Not Found!!" });
    }
  });
});

//Update Bank
app.put("/editbankadmin/:bank_admin_id", function (req, res) {
  let id_bank = req.params.bank_admin_id;
  // let data = req.body;
  let file = req.files.image;

  if (!req.files || Object.keys(req.files).length === 0) {
    return res.status(400).send("No files were uploaded.");
  }
  var imgName = file.name;
  if (!imgName) {
    return res
      .status(400)
      .send({ error: true, message: "sNo files were uploaded." });
  }
  console.log(imgName);

  uploadPath = __dirname + "/uploads/" + file.name;
  // console.log(uploadPath.split('/uploads')[1])

  var imgsrc = "/uploads/" + file.name;
  // var imgsrc =
  // "http://" + results_ipv4["Wi-Fi"][0] + ":3000/uploads/" + file.name;
  // var imgsrc = 'http://127.0.0.1:3000/uploads/' + uploadPath.split('/uploads')[1]

  file.mv(uploadPath, function (err) {
    if (err) return res.status(500).send(err);

    // res.send('File uploaded!');
  });

  // let bank_admins_id = req.body.bank_admins_id;
  let bank_name = req.body.bank_name;
  let account_number = req.body.account_number;
  let firstName = req.body.firstName;
  let lastName = req.body.lastName;
  let qr_code = imgsrc;
  console.log(qr_code);
  console.log(id_bank, bank_name, account_number, firstName, lastName, qr_code);

  sql_update =
    "UPDATE bank_admins SET bank_name = ? , account_number = ? , firstName = ? , lastName = ? , qr_code = ? WHERE bank_admin_id = ? ";
  update_data = [
    req.body.bank_name,
    account_number,
    firstName,
    lastName,
    qr_code,
    id_bank,
  ];

  dbConn.query(sql_update, update_data, function (error, results, fields) {
    if (error) throw error;
    return res.send({
      error: false,
      message: "User has been updated seccessfully",
    });
  });
});

//Call Bank
app.get("/bank", function (req, res) {
  dbConn.query(
    "SELECT * FROM `bank_admins` WHERE delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        return res.send(results);
      } else {
        return res
          .status(400)
          .send({ error: true, message: "bank_admins id Not Found!!" });
      }
    }
  );
});

//allstadium
app.get("/allstadium", function (req, res) {
  dbConn.query(
    "SELECT * FROM stadium WHERE delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      return res.send(results);
    }
  );
});

//search
app.post("/search/", function (req, res) {
  let stadium_id = req.body.id;
  let data = req.body;
  let time_end = parseInt(req.body.time_end) - 1;
  let time_start = parseInt(req.body.time_start) + 1;
  let Date_req = new Date(req.body.reserve_date);

  let reserve_date;
  let reserve_date1;
  let reserve_date2;
  let reserve_date3;

  if (!stadium_id || !data) {
    return res
      .status(400)
      .send({ error: true, message: "Please provide stadium id and dataaa" });
  }

  dbConn.query(
    "SELECT * FROM reserves WHERE reserve_date = '" +
      req.body.reserve_date +
      "' AND stadium_id = " +
      stadium_id +
      " AND time_start BETWEEN " +
      req.body.time_start +
      " AND " +
      time_end +
      " AND delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        reserve_date = JSON.parse(JSON.stringify(results)).length;
      } else {
        reserve_date = 0;
      }
      console.log(reserve_date);
    }
  );
  dbConn.query(
    "SELECT * FROM reserves WHERE reserve_date = '" +
      req.body.reserve_date +
      "' AND stadium_id = " +
      stadium_id +
      " AND time_end BETWEEN " +
      time_start +
      " AND " +
      req.body.time_end +
      " AND delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        reserve_date3 = JSON.parse(JSON.stringify(results)).length;
      } else {
        reserve_date3 = 0;
      }
      //เช็คคอลั่มจบ => อยู่ระหว่าง
      console.log(reserve_date3);
    }
  );
  dbConn.query(
    "SELECT * FROM reserves WHERE reserve_date = '" +
      req.body.reserve_date +
      "' AND stadium_id = " +
      stadium_id +
      " AND time_start < " +
      req.body.time_start +
      " AND time_end > " +
      req.body.time_end +
      " AND delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        reserve_date1 = JSON.parse(JSON.stringify(results)).length;
      } else {
        reserve_date1 = 0;
      } //เวลาระหว่าง เริ่ม ถึง จบ
      console.log(reserve_date1);
    }
  );
  dbConn.query(
    "SELECT * FROM reserves WHERE reserve_date = '" +
      req.body.reserve_date +
      "' AND stadium_id = " +
      stadium_id +
      " AND time_start = " +
      req.body.time_start +
      " AND time_end =" +
      req.body.time_end +
      " AND delete_at IS NULL",
    function (error, results, fields) {
      if (error) throw error;
      if (results) {
        // reserve_date2 = results
        reserve_date2 = JSON.parse(JSON.stringify(results)).length;
      } else {
        reserve_date2 = 0;
      } //เท่ากัน เริ่ม และ จบ
      console.log(reserve_date2);

      var date_cur = new Date();
      var cur_date = new Date(
        date_cur.getFullYear() +
          "-" +
          (date_cur.getMonth() + 1) +
          "-" +
          date_cur.getDate()
      );
      var time = parseInt(date_cur.getHours());

      if (
        reserve_date2 == 0 &&
        reserve_date1 == 0 &&
        reserve_date == 0 &&
        reserve_date3 == 0 &&
        cur_date < Date_req
      ) {
        console.log(cur_date, Date_req, req.body.reserve_date);
        if (time < parseInt(req.body.time_start)) {
          console.log("2จองได้");
          return res.send(data);
        } else {
          if (cur_date < Date_req) {
            console.log("3จองได้");
            return res.send(data);
          } else {
            console.log("4จองไม่ได้");
            return res.status(400).send({ error: true, message: "e2" });
          }
        }
      } else {
        console.log("6จองไม่ได้");
        return res.status(400).send({ error: true, message: "e3" });
      }
    }
  );
});

//Delete stadium
app.put("/delstadium/:id", function (req, res) {
  let std_id = req.params.id;
  let std = req.body;
  if (!std_id || !std) {
    return res.status(400).send({
      error: true,
      message: "Please provide Stadium id and Stadium data",
    });
  }

  dbConn.query(
    "UPDATE stadium SET ? WHERE stadium_id = ?",
    [std, std_id],
    function (error, results, fields) {
      if (error) throw error;

      return res.send({
        error: false,
        message: "Stadium has been updated seccessfully",
      });
    }
  );
});

//Insert Stadium
app.post("/stadium", function (req, res) {
  let std = req.body;
  if (!std) {
    return res.status(400).send({
      error: true,
      message: "Please provide Stadium ",
    });
  }
  let file = req.files.image;

  if (!req.files || Object.keys(req.files).length === 0) {
    return res.status(400).send("No files were uploaded.");
  }
  var imgName = file.name;
  if (!imgName) {
    return res.status(400).send({
      error: true,
      message: "sNo files were uploaded.",
    });
  }
  console.log(imgName);
  uploadPath = __dirname + "/uploads/" + file.name;
  var imgsrc = "/uploads/" + file.name;
  file.mv(uploadPath, function (err) {
    if (err) return res.status(500).send(err);
  });

  let stadium_name = req.body.stadium_name;
  let stadium_price = req.body.stadium_price;
  let stadium_img = imgsrc;
  let stadium_detail = req.body.stadium_detail;
  let create_at = req.body.create_at;

  console.log(
    stadium_name,
    stadium_price,
    stadium_img,
    stadium_detail,
    create_at
  );

  sql_insert =
    "INSERT INTO stadium(stadium_name,stadium_price, stadium_img, stadium_detail, create_at) VALUES(?,?,?,?,?)";
  insert_stadium = [
    req.body.stadium_name,
    stadium_price,
    stadium_img,
    stadium_detail,
    create_at,
  ];
  dbConn.query(sql_insert, insert_stadium, function (error, results, fields) {
    if (error) throw error;
    return res.send({
      error: false,
      message: "Stadium is added seccessfully",
    });
  });
});

//Update Stadium
app.put("/stadium/:id", function (req, res) {
  let std = req.body;
  if (!std) {
    return res.status(400).send({
      error: true,
      message: "Please provide Stadium ",
    });
  }
  let file = req.files.image;

  if (!req.files || Object.keys(req.files).length === 0) {
    return res.status(400).send("No files were uploaded.");
  }
  var imgName = file.name;
  if (!imgName) {
    return res.status(400).send({
      error: true,
      message: "sNo files were uploaded.",
    });
  }
  console.log(imgName);
  uploadPath = __dirname + "/uploads/" + file.name;
  var imgsrc = "/uploads/" + file.name;
  file.mv(uploadPath, function (err) {
    if (err) return res.status(500).send(err);
  });

  let std_id = req.params.id;
  let stadium_name = req.body.stadium_name;
  let stadium_price = req.body.stadium_price;
  let stadium_img = imgsrc;
  let stadium_detail = req.body.stadium_detail;
  let update_at = req.body.update_at;

  console.log(
    stadium_name,
    stadium_price,
    stadium_img,
    stadium_detail,
    update_at
  );

  sql_update =
    "UPDATE stadium SET stadium_name=?,stadium_price=?,stadium_img=?,stadium_detail=?,update_at=? WHERE stadium_id = ?";
  update_stadium = [
    req.body.stadium_name,
    stadium_price,
    imgsrc,
    stadium_detail,
    update_at,
    std_id,
  ];
  dbConn.query(sql_update, update_stadium, function (error, results, fields) {
    if (error) throw error;
    return res.send({
      error: false,
      message: "Stadium is added seccessfully",
    });
  });
});

//Add payment
app.post("/paymentadd", function (req, res) {
  let uploadPath;
  let file = req.files.image;

  // The name of the input field (i.e. "sampleFile") is used to retrieve the uploaded file
  uploadPath = __dirname + "/uploads/" + file.name;

  // Use the mv() method to place the file somewhere on your server
  file.mv(uploadPath, function (err) {
    if (err) return res.status(500).send(err);

    res.send({ error: false, message: "File uploaded!" });
  });

  if (!req.files || Object.keys(req.files).length === 0) {
    return res.status(400).send("No files were uploaded.");
  }
  var imgName = file.name;
  if (!imgName) {
    return res
      .status(400)
      .send({ error: true, message: "No files were uploaded." });
  }
  // var imgsrc = 'http://127.0.0.1:3000/uploads/' + file.name
  var imgsrc = [
    req.body.bank_admin_id,
    req.body.payment_status,
    "/uploads/" + file.name,
  ];
  var insertData =
    "INSERT INTO payment(bank_admin_id,payment_status,slip_img)VALUES(?,?,?)";

  dbConn.query(insertData, imgsrc, function (error, results, fields) {
    if (error) throw error;
    var pay_id = results.insertId;
    console.log(pay_id);

    reserveData = [
      pay_id,
      req.body.user_id,
      req.body.stadium_id,
      req.body.total_price,
      req.body.reserve_date,
      req.body.time_start,
      req.body.time_end,
    ];

    dbConn.query(
      "INSERT INTO reserves(payment_id,user_id,stadium_id,total_price,reserve_date,time_start,time_end)VALUES(?,?,?,?,?,?,?)",
      reserveData,
      function (error, result, fields) {
        if (error) throw error;
        // return res.send(result);
      }
    );
  });
});

//Call bank_admins
app.get("/qrcode", function (req, res) {
  dbConn.query("SELECT * FROM bank_admins", function (error, results, fields) {
    if (error) throw error;
    return res.send(results);
  });
});

//Call payment
app.get("/allpayment", function (req, res) {
  dbConn.query("SELECT * FROM payment", function (error, results, fields) {
    if (error) throw error;
    return res.send(results);
  });
});

//set port
app.listen(3000, function () {
  console.log("Node app is running on port 3000");
});

module.exports = app;
