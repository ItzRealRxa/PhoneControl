<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phone Control Panel | Advanced Security</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/lucide-static@0.222.0/lucide.min.js"></script>
</head>
<body>
    <div class="container">
        <header>
            <div class="logo">
                <i data-lucide="smartphone" class="logo-icon"></i>
                <div class="logo-text">
                    <h1>Phone Control <span>v1.0</span></h1>
                </div>
            </div>
            <div class="header-actions">
                <a href="app.apk" class="download-btn" download title="Download APK">
                    <i data-lucide="download-cloud"></i> APK
                </a>
                <div class="status-badge" id="conn-status" onclick="fetchStatus()">
                    <i data-lucide="refresh-cw" class="refresh-icon"></i>
                    <span class="status-dot"></span> Online
                </div>
            </div>
        </header>

        <main>
            <section class="card current-status">
                <h3>Current Command</h3>
                <div id="cmd-display" class="cmd-text">Fetching...</div>
            </section>

            <section class="controls-grid">
                <div class="control-card" onclick="setCommand('flash_on')">
                    <div class="icon-wrap"><i data-lucide="flashlight"></i></div>
                    <h4>Flashlight ON</h4>
                    <p>Activate LED</p>
                </div>
                <div class="control-card" onclick="setCommand('flash_off')">
                    <div class="icon-wrap off"><i data-lucide="flashlight-off"></i></div>
                    <h4>Flashlight OFF</h4>
                    <p>Deactivate LED</p>
                </div>
                <div class="control-card" onclick="setCommand('play_sound')">
                    <div class="icon-wrap sound"><i data-lucide="volume-2"></i></div>
                    <h4>Play Sound</h4>
                    <p>Find your phone</p>
                </div>
                <div class="control-card" onclick="setCommand('get_battery')">
                    <div class="icon-wrap battery"><i data-lucide="battery-charging"></i></div>
                    <h4>Get Battery</h4>
                    <p>Check power level</p>
                </div>
            </section>
        </main>

        <footer>
            <p>&copy; 2024 Phone Control System. Basic Key Secured.</p>
        </footer>
    </div>

    <script>
        const API_KEY = "1234"; // Should match api.php
        
        async function setCommand(cmd) {
            const display = document.getElementById('cmd-display');
            display.innerText = "Sending...";
            
            try {
                const response = await fetch(`set.php?cmd=${cmd}&key=${API_KEY}`);
                const data = await response.json();
                
                if (data.status === 'success') {
                    display.innerText = cmd.toUpperCase();
                    showToast(`Command sent: ${cmd}`);
                } else {
                    display.innerText = "Error: " + data.message;
                }
            } catch (error) {
                display.innerText = "Connection Failed";
                console.error(error);
            }
        }

        async function fetchStatus() {
            try {
                const response = await fetch(`api.php?key=${API_KEY}`);
                const data = await response.json();
                if (data.command) {
                    document.getElementById('cmd-display').innerText = data.command.toUpperCase();
                }
            } catch (e) {}
        }

        function showToast(msg) {
            const toast = document.createElement('div');
            toast.className = 'toast';
            toast.innerText = msg;
            document.body.appendChild(toast);
            setTimeout(() => toast.remove(), 3000);
        }

        // Initialize Lucide icons
        lucide.createIcons();
        
        // Update status occasionally
        setInterval(fetchStatus, 3000);
    </script>
</body>
</html>
