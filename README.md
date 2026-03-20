# Android Phone Control System

A complete remote control system for Android devices with a PHP backend and a modern web dashboard.

## 📁 Project Structure
- `/` : PHP Backend files (`api.php`, `set.php`, `db.php`, `index.php`, `style.css`)
- `/android/` : Android Studio project files (Java)
- `setup.sql` : Database schema

## 🚀 Deployment Instructions (InfinityFree)

### 1. Database Setup
1. Log in to your InfinityFree control panel (cPanel).
2. Go to **MySQL Databases**.
3. Create a new database named `phone_control`.
4. Open **phpMyAdmin** for that database.
5. Click **Import** and upload `setup.sql` (or copy-paste the SQL content).

### 2. Backend Upload
1. Upload all PHP and CSS files to the `htdocs/` folder using FTP (e.g., FileZilla).
2. Edit `db.php` with your InfinityFree database credentials:
   - `$host`: Your MySQL Hostname (e.g., `sql123.epizy.com`)
   - `$user`: Your MySQL Username (e.g., `epiz_12345678`)
   - `$pass`: Your MySQL Password
   - `$dbname`: Your MySQL Database Name

### 3. Android App Setup
1. Open Android Studio.
2. Create a new project with an **Empty Activity** and language **Java**.
3. Copy the provided Java files into your package directory (`com.example.phonecontrol`).
4. Update `ControlService.java`:
   - Replace `http://yourserver.com/api.php?key=1234` with your InfinityFree URL (e.g., `http://yourdomain.rf.gd/api.php?key=1234`).
5. Build and run the app on your phone.

## 🔐 Security
- Access is protected by a simple `key=1234` parameter.
- Commands are automatically reset to `none` after being fetched by the app.

## 📱 Features
- Flashlight Toggle
- Play Sound (Ringtone)
- Check Battery Percentage
- Real-time logging on Dashboard and App
