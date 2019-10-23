const mysql = require('mysql');
const fs = require('fs');

const connection = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "Hoidelamgi1",
  database: "olympia"
});

connection.connect(function (err) {
  if (err) throw err;
  else {
    console.log("Connection finished!")
    const filename = "answer.csv";
    fs.readFile(filename, "utf8", function (err, data) {
      if (err) throw err;
      else {
        let rows = [];
        rows = transformCSV(data);
        insertDataToTable("Answer", "Question_Id, Answer, Is_Correct", rows);
      }
    });
  }
});

function insertDataToTable(tableName = "", columnRange = "", rows = []) {
  if (!!tableName && !!columnRange) {
    let query = `INSERT INTO ${tableName} (${columnRange}) VALUES ?`;
    connection.query(query, [rows], (error, response) => {
      console.log(error || response);
    });
  }

}

function transformCSV(data = "") {
  let result = [];
  const splitNewline = data.split("\n");
  splitNewline.shift();
  splitNewline.map(x => {
    result.push(x.split(",").map(y => {
      if (+y) {
        y = +y;
      }
      else if (y === "true") {
        y = 1;
      }
      else if (y === "false") {
        y = 0;
      }
      return y;
    }))
  });
  return result;
}