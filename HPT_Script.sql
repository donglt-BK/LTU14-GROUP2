CREATE DATABASE olympia;
USE olympia;

CREATE TABLE User
(
    Id       INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(20) UNIQUE NOT NULL,
    Password VARCHAR(20)        NOT NULL,
    Name     VARCHAR(20)        NOT NULL,
    Gender   INT                NOT NULL,
    Balance  INT DEFAULT 50000
);

CREATE TABLE PlayerData
(
    Id      INT NOT NULL,
    User_Id INT NOT NULL,
    Money   INT NOT NULL,
    FOREIGN KEY (User_Id) REFERENCES User (Id)
);

CREATE TABLE Room
(
    Id            INT PRIMARY KEY AUTO_INCREMENT,
    Lobby_Id      INT NOT NULL,
    Max_Users     INT NOT NULL,
    Bet_Value     INT NOT NULL,
    Current_Level INT DEFAULT 0,
    Max_Questions INT NOT NULL
);

CREATE TABLE RoomPlayer
(
    Room_Id   INT NOT NULL,
    Player_Id INT NOT NULL,
    FOREIGN KEY (Room_Id) REFERENCES Room (Id),
    FOREIGN KEY (Player_Id) REFERENCES PlayerData (Id)
);

CREATE TABLE Topic
(
    Id                INT PRIMARY KEY AUTO_INCREMENT,
    Topic_Name        VARCHAR(50) UNIQUE NOT NULL,
    Topic_Description VARCHAR(500) DEFAULT ''
);

CREATE TABLE QuestionSet
(
    Id         INT PRIMARY KEY AUTO_INCREMENT,
    Topic_Id   INT                   NOT NULL,
    Difficulty INT                   NOT NULL,
    Question   NVARCHAR(1000) UNIQUE NOT NULL,
    FOREIGN KEY (Topic_Id) REFERENCES Topic (Id)
);

CREATE TABLE Answer
(
    Answer_Id   INT PRIMARY KEY AUTO_INCREMENT,
    Question_Id INT           NOT NULL,
    Answer      NVARCHAR(100) NOT NULL,
    Is_Correct  BIT           NOT NULL,
    FOREIGN KEY (Question_Id) REFERENCES QuestionSet (Id)
);


