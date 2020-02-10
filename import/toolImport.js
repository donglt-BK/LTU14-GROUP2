/**
 * @author: Ant
 * @description: 
 * - Tool import vào CSDL (MySQL) từ file `.csv`
 * - Để chạy được cần có NodeJS, và cài đặt mysql (`npm install mysql`)
 */

const mysql = require('mysql');
const fs = require('fs');
const fetch = require('node-fetch');

import sql from "./sqlQuery";


const connection = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "Hoidelamgi1",
  database: "olympia"
});

const SQL_QUERY = sql(connection);

connection.connect(function (err) {
  if (err) throw err;
  else {
    console.log("Connection finished!")
    // readCSVToImport("Topic");
    // readCSVToImport("QuestionSet");
    // readCSVToImport("Answer");
    fetchData();
  }
});

const fetchData = async (amount = 10, category = 21, difficulty = "easy", type = "multiple") => {
  const rawDataFromApi = await fetch(`https://opentdb.com/api.php?amount=${amount}&category=${category}&difficulty=${difficulty}&type=${type}`)
    .then(result => result.json())
    .catch(err => console.log(err));

  if (rawDataFromApi) {
    const freshData = rawDataFromApi.results;
    for (let fresh of freshData) {
      let topicId = +getOneByOne("topic", "id", "topic_name", fresh.category);
      let questionId;
      if (!!topicId) {
        questionId = +getOneByQuery(`SELECT id FROM question_set WHERE topic_id = ${topicId} AND question = '${fresh.question}'`);
        if (!!questionId) {
          continue;
        }
        else {
          insertDataToQuestionAnswer(topicId, fresh);
        }
      }
      else {
        insertDataToTable("topic", "topic_name", [[fresh.category]]);
        topicId = +getOneByOne("topic", "id", "topic_name", fresh.category);
        insertDataToQuestionAnswer(topicId, fresh);
      }
    }
  }
  else {
    console.log("Null data!");
    return;
  }
}

function insertDataToQuestionAnswer(topicId, fresh) {
  insertDataToTable("question_set", "topic_id,question,difficulty", [[topicId, fresh.question, formatDifficulty(fresh.difficulty)]]);
  const questionId = +SQL_QUERY.getOneByQuery(`SELECT id FROM question_set WHERE topic_id = ${topicId} AND question = '${fresh.question}'`);
  insertDataToTable("answer", "question_id,answer,is_correct", [[questionId, fresh.answer, 1]]);

  const incorrectAnswers = [];
  fresh.incorrect_answers.map(x => incorrectAnswers.push([
    questionId,
    x,
    0
  ]));
  insertDataToTable("answer", "question_id,answer,is_correct", incorrectAnswers);
}

function getOneByQuery(query = "", selectColumn = "") {
  let selectValue = "";
  connection.query(query, (err, result) => {
    if (err) {
      console.log(err);
    }
    else {
      console.log(result);
      if (result.length > 0) {
        selectValue = result.pop()[selectColumn];
      }
    }
  });
  return selectValue;
}

function getOneByMany(tableName, selectColumn, [conditionColumn], [conditionValue]) {

}

function getOneByOne(tableName, selectColumn, conditionColumn, conditionValue) {
  let selectValue = "";
  const query = `SELECT ${selectColumn} FROM ${tableName} WHERE ${conditionColumn} = '${conditionValue}'`;
  connection.query(query, (error, result) => {
    if (error) {
      console.log(error);
    }
    else {
      console.log(result);
      if (result.length > 0) {
        selectValue = result.pop()[selectColumn];
      }
    }
  });
  return selectValue;
}

/**
 * Đọc dữ liệu từ file để import
 * @param {string} filename Tên file cần đọc để import
 */
function readCSVToImport(filename = "") {
  if (!!filename) {
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

function formatDifficulty(raw = "") {
  switch (raw) {
    case "easy":
      return 1;
    case "medium":
      return 2;
    case "hard":
      return 3;
    default:
      return 1;
  }
}