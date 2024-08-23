#!/bin/sh

sleep 5

# MySQL connection details
MYSQL_USER="root"
MYSQL_PASSWORD="root"
MYSQL_HOST="mysql"
MYSQL_DB="transaction"

# Ensure the MySQL root user uses the mysql_native_password plugin
echo "Setting MySQL root user to use mysql_native_password..."
mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "ALTER USER '$MYSQL_USER'@'%' IDENTIFIED WITH mysql_native_password BY '$MYSQL_PASSWORD'; FLUSH PRIVILEGES;"

# Check if the database exists
DB_EXISTS=$(mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "SHOW DATABASES LIKE '$MYSQL_DB';" | grep "$MYSQL_DB" > /dev/null; echo "$?")

if [ $DB_EXISTS -eq 0 ]; then
  echo "Database '$MYSQL_DB' already exists."
else
  echo "Creating database '$MYSQL_DB'..."
  mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "CREATE DATABASE $MYSQL_DB;"
fi

# Use the database
mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "USE $MYSQL_DB;"

# Check if the table exists
TABLE_EXISTS=$(mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "USE $MYSQL_DB; SHOW TABLES LIKE 'TAccount';" | grep "TAccount" > /dev/null; echo "$?")

if [ $TABLE_EXISTS -eq 0 ]; then
  echo "Table 'TAccount' already exists."
else
  echo "Creating table 'TAccount'..."
  mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST $MYSQL_DB <<EOF
CREATE TABLE IF NOT EXISTS TAccount (
    accountId BIGINT NOT NULL,
    id BIGINT NOT NULL AUTO_INCREMENT,
    pid BIGINT,
    accountClass VARCHAR(255),
    accountType VARCHAR(255),
    code VARCHAR(255),
    name VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
EOF
fi

# Check if the foreign key constraint exists
FK_EXISTS=$(mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST -e "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = '$MYSQL_DB' AND TABLE_NAME = 'TAccount' AND CONSTRAINT_NAME = 'FKasjh4i7dv1151urg8bqvtgnf2';" | grep "FKasjh4i7dv1151urg8bqvtgnf2" > /dev/null; echo "$?")

if [ $FK_EXISTS -eq 0 ]; then
  echo "Foreign key constraint 'FKasjh4i7dv1151urg8bqvtgnf2' already exists."
else
  echo "Adding foreign key constraint..."
  mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST $MYSQL_DB <<EOF
ALTER TABLE TAccount ADD CONSTRAINT FKasjh4i7dv1151urg8bqvtgnf2 FOREIGN KEY (pid) REFERENCES TAccount(id);
EOF
fi

# Insert data if it doesn't already exist
mysql -u$MYSQL_USER -p$MYSQL_PASSWORD -h$MYSQL_HOST $MYSQL_DB <<EOF
INSERT INTO TAccount(accountClass, accountId, accountType, code, name)
SELECT 'A', 456, 'Business', 'XYZ', 'ABC'
WHERE NOT EXISTS (
    SELECT 1 FROM TAccount WHERE accountClass = 'A' AND accountId = 456 AND code = 'XYZ'
);

INSERT INTO TAccount(accountClass, accountId, accountType, code, name)
SELECT 'B', 456, 'Business', 'UTC', 'DEF'
WHERE NOT EXISTS (
    SELECT 1 FROM TAccount WHERE accountClass = 'B' AND accountId = 456 AND code = 'UTC'
);
EOF

echo "Database initialization completed."