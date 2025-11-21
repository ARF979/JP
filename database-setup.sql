-- Google Drive-like Backend Database Setup
-- Run this script to create the database

-- Create database (run as postgres superuser)
CREATE DATABASE gdrive_db;

-- Connect to the database
\c gdrive_db;

-- The tables will be auto-created by Hibernate when you run the application
-- with spring.jpa.hibernate.ddl-auto=update

-- You can verify the tables after running the application:
-- \dt

-- Expected tables:
-- - folders
-- - file_items

