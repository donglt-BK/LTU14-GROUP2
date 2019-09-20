CREATE DATABASE olympia;
USE olympia;

CREATE TABLE User(
	Id INT PRIMARY KEY,
    Username VARCHAR(20) UNIQUE NOT NULL,
    Password VARCHAR(20) NOT NULL,
    Name VARCHAR(50) NOT NULL,
    Gender INT NOT NULL,
    Balance INT DEFAULT 0
);

CREATE TABLE PlayerData(
	Id INT NOT NULL,
    CurrentLevel INT DEFAULT 0,
    PointTotal INT DEFAULT 0,
    FOREIGN KEY (Id) REFERENCES User(Id)
);

CREATE TABLE Room(
	Id INT PRIMARY KEY,
    MaxUsers INT NOT NULL,
    BetValue INT NOT NULL,
    MaxQuestions INT NOT NULL
);

CREATE TABLE RoomPlayer(
	RoomId INT NOT NULL,
    PlayerId INT NOT NULL,
    FOREIGN KEY (RoomId) REFERENCES Room(Id),
    FOREIGN KEY (PlayerId) REFERENCES PlayerData(Id)
);

CREATE TABLE Topic(
	Id INT PRIMARY KEY,
    TopicName VARCHAR(50) UNIQUE NOT NULL,
    TopicDescription VARCHAR(500) DEFAULT ''
);

CREATE TABLE QuestionSet(
	Id INT PRIMARY KEY,
    TopicId INT NOT NULL,
    Difficulty INT NOT NULL,
    Question NVARCHAR(1000) UNIQUE NOT NULL,
    FOREIGN KEY (TopicId) REFERENCES Topic(Id)
);

CREATE TABLE Answer(
	AnswerId INT PRIMARY KEY,
	QuestionId INT NOT NULL,
    Answer NVARCHAR(100) NOT NULL,
    IsCorrect BIT NOT NULL,
    FOREIGN KEY (QuestionId) REFERENCES QuestionSet(Id)
);


