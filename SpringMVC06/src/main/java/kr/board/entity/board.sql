CREATE TABLE MYBOARD(
	idx INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(100) NOT NULL,
	content VARCHAR(2000) NOT NULL,
	writer VARCHAR(30) NOT NULL,
	indate DATETIME DEFAULT NOW(),
	count INT DEFAULT 0,
	PRIMARY KEY(idx)
);

CREATE TABLE mem_security(
	memIdx INT NOT NULL,
	memID VARCHAR(20) NOT NULL,
	memPassword VARCHAR(68) NOT NULL,
	memName VARCHAR(20) NOT NULL,
	memAge INT,
	memGender VARCHAR(20),
	memEmail VARCHAR(50),
	memProfile VARCHAR(50),
	PRIMARY KEY (memID)
);

CREATE TABLE mem_auth(
	no INT NOT NULL AUTO_INCREMENT,
	memID VARCHAR(50) NOT NULL,
	auth VARCHAR(50) NOT NULL,
	PRIMARY KEY(no),
	CONSTRAINT fk_member_auth FOREIGN KEY(memID) REFERENCES mem_security(memID)
);