# boards schema

# --- !Ups

CREATE TABLE Boards (
  Id CHAR(5) PRIMARY KEY,
  GameId INT NOT NULL,
  IsPublic BOOLEAN NOT NULL,
  Status INT UNSIGNED,
  RematchBoardId CHAR(5),
  ParentBoardId CHAR(5),
  Modified TEXT,
  FOREIGN KEY RematchBoardFk (RematchBoardId) REFERENCES Boards(Id) ON DELETE SET NULL,
  FOREIGN KEY ParentBoardFk (ParentBoardId) REFERENCES Boards(Id) ON DELETE SET NULL
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
  Status INT UNSIGNED,
  Date DATETIME NOT NULL,
  FOREIGN KEY User1Fk (User1Id) REFERENCES Users(Id) ON DELETE CASCADE,
  FOREIGN KEY User2Fk (User2Id) REFERENCES Users(Id) ON DELETE CASCADE
);

CREATE TABLE Invites (
  Id SERIAL PRIMARY KEY,
  InviterId BIGINT UNSIGNED NOT NULL,
  InviteeId BIGINT UNSIGNED NOT NULL,
  BoardId CHAR(5) NOT NULL,
  FOREIGN KEY InviterFk (InviterId) REFERENCES Users(Id) ON DELETE CASCADE,
  FOREIGN KEY InviteeFk (InviteeId) REFERENCES Users(Id) ON DELETE CASCADE,
  FOREIGN KEY BoardFk (BoardId) REFERENCES Boards(Id) ON DELETE CASCADE
);

CREATE TABLE Players (
  Id SERIAL PRIMARY KEY,
  UserId BIGINT UNSIGNED,
  BoardId CHAR(5) NOT NULL,
  TurnOrder INT NOT NULL,
  IsOwner BOOLEAN NOT NULL,
  Time INT NOT NULL,
  ResignOffer BOOLEAN NOT NULL,
  DrawOffer BOOLEAN NOT NULL,
  UndoOffer BOOLEAN NOT NULL,
  FOREIGN KEY UserFk (UserId) REFERENCES Users(Id) ON DELETE SET NULL,
  FOREIGN KEY BoardFk (BoardId) REFERENCES Boards(Id) ON DELETE CASCADE
);

CREATE TABLE Actions (
  Id SERIAL PRIMARY KEY,
  BoardId CHAR(5) NOT NULL,
  Action INT NOT NULL,
  ActionOrder INT NOT NULL,
  TurnOrder INT NOT NULL
);

# --- !Downs

DROP TABLE Boards;
DROP TABLE Users;
DROP TABLE Friendships;
DROP TABLE Invites;
DROP TABLE Players;
DROP TABLE Actions;