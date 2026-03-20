-- Database setup for Android Phone Control
-- NOTE: Database should be created via InfinityFree Control Panel first.

CREATE TABLE IF NOT EXISTS control (
    id INT AUTO_INCREMENT PRIMARY KEY,
    command VARCHAR(50) DEFAULT 'none',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Initial command
INSERT INTO control (command) VALUES ('none');
