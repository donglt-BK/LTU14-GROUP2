const sql = (connection) => {
  const getOneByQuery = (query = "", selectColumn = "") => {
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

  const getOneByOne = (tableName, selectColumn, conditionColumn, conditionValue) => {
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

  const getOneByMany = (tableName, selectColumn, [conditionColumn], [conditionValue]) => {
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

  return {
    getOneByQuery,
    getOneByOne,
    getOneByMany
  };
}

export default sql;