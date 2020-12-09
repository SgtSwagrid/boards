# boards schema

# --- !Ups

CREATE TABLE States (
  Id SERIAL PRIMARY KEY
);

CREATE TABLE Boards (
  Id CHAR(5) PRIMARY KEY,
  GameId INT NOT NULL,
  StateId BIGINT UNSIGNED,
  Open BOOLEAN NOT NULL,
  RematchBoardId CHAR(5),
  ParentBoardId CHAR(5),
  ParentStateId BIGINT UNSIGNED,
  FOREIGN KEY (StateId) REFERENCES States(Id),
  FOREIGN KEY (RematchBoardId) REFERENCES Boards(Id),
  FOREIGN KEY (ParentBoardId) REFERENCES Boards(Id),
  FOREIGN KEY (ParentStateId) REFERENCES States(Id)
);

CREATE TABLE Users (
  Id SERIAL PRIMARY KEY,
  Username TEXT NOT NULL,
  Password TEXT NOT NULL
);

CREATE TABLE Friendships (
  Id SERIAL PRIMARY KEY,
  User1Id BIGINT UNSIGNED NOT NULL,
  User2Id BIGINT UNSIGNED NOT NULL,
  Date DATETIME NOT NULL,
  FOREIGN KEY (User1Id) REFERENCES Users(Id),
  FOREIGN KEY (User2Id) REFERENCES Users(Id)
);

CREATE TABLE Invites (
  Id SERIAL PRIMARY KEY,
  InviterId BIGINT UNSIGNED NOT NULL,
  InviteeId BIGINT UNSIGNED NOT NULL,
  BoardId CHAR(5) NOT NULL,
  FOREIGN KEY (InviterId) REFERENCES Users(Id),
  FOREIGN KEY (InviteeId) REFERENCES Users(Id),
  FOREIGN KEY (BoardId) REFERENCES Boards(Id)
);

CREATE TABLE Players (
  Id SERIAL PRIMARY KEY,
  UserId BIGINT UNSIGNED NOT NULL,
  BoardId CHAR(5) NOT NULL,
  TurnOrder INT NOT NULL,
  IsOwner BOOLEAN NOT NULL,
  Time INT NOT NULL,
  ResignationOffered BOOLEAN NOT NULL,
  DrawOffered BOOLEAN NOT NULL,
  FOREIGN KEY (UserId) REFERENCES Users(Id),
  FOREIGN KEY (BoardId) REFERENCES Boards(Id)
);

# --- !Downs

DROP TABLE States;
DROP TABLE Boards;
DROP TABLE Users;
DROP TABLE Friendships;
DROP TABLE Invites;
DROP TABLE Players;