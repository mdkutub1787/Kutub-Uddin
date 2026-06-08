<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | FFLIPY</title>
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

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        body {
            background: var(--bg-gradient);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            overflow: hidden;
        }

        .background-blobs {
            position: absolute;
            width: 100%;
            height: 100%;
            z-index: -1;
            overflow: hidden;
        }

        .blob {
            position: absolute;
            width: 400px;
            height: 400px;
            background: var(--primary-light);
            filter: blur(80px);
            opacity: 0.15;
            border-radius: 50%;
            animation: move 20s infinite alternate;
        }

        @keyframes move {
            from { transform: translate(-10%, -10%); }
            to { transform: translate(10%, 10%); }
        }

        .login-card {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--glass-border);
            padding: 48px;
            border-radius: 24px;
            width: 100%;
            max-width: 440px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
        }

        .logo-section {
            text-align: center;
            margin-bottom: 40px;
        }

        .logo-img {
            height: 48px;
            margin-bottom: 16px;
        }

        .logo-text {
            font-size: 24px;
            font-weight: 800;
            letter-spacing: -0.5px;
            color: #fff;
        }

        .welcome-text {
            text-align: center;
            margin-bottom: 32px;
        }

        .welcome-text h1 {
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 8px;
        }

        .welcome-text p {
            color: #94a3b8;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 24px;
            position: relative;
        }

        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 8px;
            color: #cbd5e1;
        }

        .input-wrapper {
            position: relative;
        }

        .input-wrapper i {
            position: absolute;
            left: 16px;
            top: 50%;
            transform: translateY(-50%);
            width: 18px;
            height: 18px;
            color: #64748b;
        }

        .form-control {
            width: 100%;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 12px;
            padding: 14px 16px 14px 48px;
            color: white;
            font-size: 15px;
            transition: 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary-light);
            background: rgba(255, 255, 255, 0.1);
            box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
        }

        .error-msg {
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.2);
            color: #f87171;
            padding: 12px;
            border-radius: 8px;
            font-size: 13px;
            margin-bottom: 24px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .btn-submit {
            width: 100%;
            background: var(--primary-light);
            color: white;
            border: none;
            padding: 16px;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        .btn-submit:hover {
            background: #2563eb;
            transform: translateY(-2px);
            box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.3);
        }

        .card-footer {
            margin-top: 32px;
            text-align: center;
            font-size: 14px;
            color: #94a3b8;
        }

        .card-footer a {
            color: var(--primary-light);
            text-decoration: none;
            font-weight: 600;
        }

        .card-footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="background-blobs">
        <div class="blob" style="top: -10%; left: -10%;"></div>
        <div class="blob" style="bottom: -10%; right: -10%; background: var(--secondary);"></div>
    </div>

    <div class="login-card">
        <div class="logo-section">
            <img src="/assets/logo/logo.png" alt="FFLIPY" class="logo-img" onerror="this.src='https://ui-avatars.com/api/?name=FFLIPY&background=1e3a8a&color=fff'">
            <div class="logo-text">FFLIPY</div>
        </div>

        <div class="welcome-text">
            <h1>Welcome Back</h1>
            <p>Secure login to your FFLIPY account</p>
        </div>

        <?php if (isset($error)): ?>
            <div class="error-msg">
                <i data-lucide="alert-circle" style="width: 16px; height: 16px;"></i>
                <?php echo htmlspecialchars($error); ?>
            </div>
        <?php endif; ?>

        <form method="POST" action="">
            <div class="form-group">
                <label>Username or Email</label>
                <div class="input-wrapper">
                    <i data-lucide="user"></i>
                    <input type="text" name="username" class="form-control" placeholder="Enter your username" required autofocus>
                </div>
            </div>

            <div class="form-group">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                    <label style="margin-bottom: 0;">Password</label>
                    <a href="forgot_password.php" style="color: var(--primary-light); font-size: 13px; font-weight: 600; text-decoration: none;">Forgot Password?</a>
                </div>
                <div class="input-wrapper">
                    <i data-lucide="lock"></i>
                    <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>
            </div>

            <button type="submit" class="btn-submit">
                <span>Sign In</span>
                <i data-lucide="arrow-right" style="width: 18px; height: 18px;"></i>
            </button>
        </form>

        <div class="card-footer">
            <p>Don't have an account? <a href="register.php">Sign up now</a></p>
            <p style="margin-top: 12px; font-size: 12px;">© <?php echo date('Y'); ?> FFLIPY. All rights reserved.</p>
        </div>
    </div>

    <script>
        lucide.createIcons();
    </script>
</body>
</html>
