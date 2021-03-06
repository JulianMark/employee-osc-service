CREATE TABLE EMPLOYEE_TYPES
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
TYPE_EMPLOYEE VARCHAR (20)
);

CREATE TABLE OSC
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
DESCRIPTION VARCHAR (100)
);

CREATE TABLE LOCATIONS
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
DESCRIPTION VARCHAR (30)
);

CREATE TABLE CAMPAIGN_TYPES
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
DESCRIPTION VARCHAR (30)
);

CREATE TABLE CAMPAIGNS
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
NAME VARCHAR (30),
ID_LOC INT,
ID_OSC INT,
ID_TYPE_CAMPAIGN INT
);

CREATE TABLE EMPLOYEES
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
NAME VARCHAR (20),
LASTNAME VARCHAR (20),
ID_TYPE_EMPLOYEE INT,
NICKNAME NVARCHAR(20),
PASSWORD NVARCHAR (15)
);

CREATE TABLE TEAM_EMPLOYEES
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
ID_EMPLOYEE INT,
INITIAL_DATE DATETIME,
FINAL_DATE DATETIME,
ID_CAMPAIGN INT
);

CREATE TABLE PAY_TYPES
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
DESCRIPTION VARCHAR (10)
);

CREATE TABLE DONATIONS
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
AMOUNT FLOAT,
ID_TYPE_PAY INT,
ID_EMPLOYEE_TEAM INT,
ID_CAMPAIGN INT
);

CREATE TABLE DAILY_RECORDS
(
ID INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
ID_EMPLOYEE_TEAM INT,
ID_CAMPAIGN INT,
PRODUCTIVE_HOURS FLOAT,
NON_PRODUCTIVE_HOURS FLOAT,
TODAY DATETIME
);


ALTER TABLE CAMPAIGNS
ADD FOREIGN KEY (ID_LOC) REFERENCES LOCATIONS;
ALTER TABLE CAMPAIGNS
ADD FOREIGN KEY (ID_OSC) REFERENCES OSC;
ALTER TABLE CAMPAIGNS
ADD FOREIGN KEY (ID_TYPE_CAMPAIGN) REFERENCES CAMPAIGN_TYPES;

ALTER TABLE EMPLOYEES
ADD FOREIGN KEY (ID_TYPE_EMPLOYEE) REFERENCES EMPLOYEE_TYPES;

ALTER TABLE TEAM_EMPLOYEES
ADD FOREIGN KEY (ID_EMPLOYEE) REFERENCES EMPLOYEES;
ALTER TABLE TEAM_EMPLOYEES
ADD FOREIGN KEY (ID_CAMPAIGN) REFERENCES CAMPAIGNS;

ALTER TABLE DONATIONS
ADD FOREIGN KEY (ID_TYPE_PAY) REFERENCES PAY_TYPES;
ALTER TABLE DONATIONS
ADD FOREIGN KEY (ID_EMPLOYEE_TEAM) REFERENCES TEAM_EMPLOYEES;
ALTER TABLE DONATIONS
ADD FOREIGN KEY (ID_CAMPAIGN) REFERENCES CAMPAIGNS;

ALTER TABLE DAILY_RECORDS
ADD FOREIGN KEY (ID_EMPLOYEE_TEAM) REFERENCES TEAM_EMPLOYEES;
ALTER TABLE DAILY_RECORDS
ADD FOREIGN KEY (ID_CAMPAIGN) REFERENCES CAMPAIGNS;