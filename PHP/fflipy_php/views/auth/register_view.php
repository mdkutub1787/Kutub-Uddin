<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Join FFLIPY | Register</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        :root {
            --primary: #1e3a8a;
            --primary-light: #3b82f6;
            --secondary: #10b981;
            --bg-gradient: linear-gradient(135deg, #1e3a8a 0%, #1e1b4b 100%);
            --glass-bg: rgba(255, 255, 255, 0.05);
            --glass-border: rgba(255, 255, 255, 0.1);
        }

        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Inter', sans-serif; }

        body {
            background: var(--bg-gradient);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            padding: 40px 20px;
        }

        .login-card {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--glass-border);
            padding: 40px;
            border-radius: 24px;
            width: 100%;
            max-width: 500px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
        }

        .logo-section { text-align: center; margin-bottom: 30px; }
        .logo-text { font-size: 24px; font-weight: 800; color: #fff; }

        .welcome-text { text-align: center; margin-bottom: 30px; }
        .welcome-text h1 { font-size: 24px; font-weight: 700; margin-bottom: 8px; }
        .welcome-text p { color: #94a3b8; font-size: 14px; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 16px; }

        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; margin-bottom: 6px; color: #cbd5e1; }
        
        .form-control {
            width: 100%;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 10px;
            padding: 12px 14px;
            color: white;
            font-size: 14px;
            transition: 0.3s;
        }

        .form-control:focus { outline: none; border-color: var(--primary-light); background: rgba(255, 255, 255, 0.1); }

        .error-msg {
            background: rgba(239, 68, 68, 0.1);
            color: #f87171;
            padding: 12px;
            border-radius: 8px;
            font-size: 13px;
            margin-bottom: 20px;
            display: flex;
            align-items: center; gap: 8px;
        }

        .btn-submit {
            width: 100%;
            background: var(--primary-light);
            color: white;
            border: none;
            padding: 14px;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: 0.3s;
            display: flex; align-items: center; justify-content: center; gap: 8px;
            margin-top: 10px;
        }

        .btn-submit:hover { transform: translateY(-2px); box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.3); }

        .card-footer { margin-top: 24px; text-align: center; font-size: 13px; color: #94a3b8; }
        .card-footer a { color: var(--primary-light); text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="logo-section">
            <div class="logo-text">FFLIPY</div>
        </div>

        <div class="welcome-text">
            <h1>Create Account</h1>
            <p>Join thousands of users transferring money globally</p>
        </div>

        <?php if (isset($error)): ?>
            <div class="error-msg">
                <i data-lucide="alert-circle" style="width: 16px; height: 16px;"></i>
                <?php echo htmlspecialchars($error); ?>
            </div>
        <?php endif; ?>

        <form method="POST">
            <div class="form-grid">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" name="firstname" class="form-control" placeholder="Rakibul" required>
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" name="lastname" class="form-control" placeholder="Islam" required>
                </div>
            </div>

            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" class="form-control" placeholder="rakib123" required>
            </div>

            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
            </div>

            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phone" class="form-control" placeholder="+123456789" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-submit">
                <span>Register Now</span>
                <i data-lucide="user-plus" style="width: 18px; height: 18px;"></i>
            </button>
        </form>

        <div class="card-footer">
            <p>Already have an account? <a href="login.php">Log In</a></p>
        </div>
    </div>

    <script>lucide.createIcons();</script>
</body>
</html>
