

CREATE TABLE user_data (
username VARCHAR  UNIQUE,
password VARCHAR NOT NULL,
PRIMARY KEY (username)
);

CREATE TABLE token (
token_id SERIAL,
token VARCHAR NOT NULL,
username VARCHAR NOT NULL,
PRIMARY KEY (token_id),
FOREIGN KEY (username) REFERENCES user_data(username)
);
