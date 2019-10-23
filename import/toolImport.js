/**
 * @author: Ant
 * @description: 
 * - Tool import vào CSDL (MySQL) từ file `.csv`
 * - Để chạy được cần có NodeJS, và cài đặt mysql (`npm install mysql`)
 */

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
    readCSVToImport("Topic");
    readCSVToImport("QuestionSet");
    readCSVToImport("Answer");
  }
});

/**
 * Đọc dữ liệu từ file để import
 * @param {string} filename Tên file cần đọc để import
 */
function readCSVToImport(filename = "") {
  if (!!filename){
    fs.readFile(`${filename}.csv`, "utf8", function (err, data) {
      if (err) throw err;
      else {
        if (data && data.length > 0) {
          const transformResult = transformCSV(data);
          insertDataToTable(filename, transformResult.columnRange, transformResult.rows);
        }
        else {
          console.log("File has no data");
        }
      }
    });
  }
  else {
    console.log("Tên file không đúng dịnh dạng");
  }
}

/**
 * Câu truy vấn Insert vào CSDL
 * @param {string} tableName Tên bảng
 * @param {string} columnRange Dải trường dữ liệu cần import
 * @param {Array} rows Dữ liệu cần import
 */
function insertDataToTable(tableName = "", columnRange = "", rows = []) {
  if (!!tableName && !!columnRange) {
    let query = `INSERT INTO ${tableName} (${columnRange}) VALUES ?`;
    connection.query(query, [rows], (error, response) => {
      console.log(error || response);
    });
  }
}

/**
 * Chuyển dữ liệu đọc từ file thành
 * - `rows`: dữ liệu để import
 * - `columnRange`: dải trường dữ liệu để import
 * @param {string} data Dữ liệu lấy từ file
 */
function transformCSV(data = "") {
  let rows = [],
    columnRange = "";

  const splitNewline = data.split("\n");
  
  columnRange = splitNewline.shift();
  
  splitNewline.map(x => {
    rows.push(x.split(",").map(y => {
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

  return {
    rows,
    columnRange
  };
}