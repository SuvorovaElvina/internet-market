CREATE TABLE IF NOT EXISTS users (
  id        bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name      varchar(255) NOT NULL,
  password  varchar(512) NOT NULL
);
CREATE TABLE IF NOT EXISTS roles (
  id        bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name      varchar(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS users_roles (
  user_id   bigint REFERENCES users (id),
  role_id   bigint REFERENCES roles (id),
  PRIMARY KEY (user_id, role_id)
);