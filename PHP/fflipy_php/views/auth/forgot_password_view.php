<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password | FFLIPY</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <script src="https://unpkg.com/lucide@latest"></script>
    <style>
        :root {
            --primary: #1e3a8a;
            --primary-light: #3b82f6;
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
            padding: 20px;
        }

        .login-card {
            background: var(--glass-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--glass-border);
            padding: 48px;
            border-radius: 24px;
            width: 100%;
            max-width: 440px;
            text-align: center;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
        }

        .icon-box {
            width: 72px;
            height: 72px;
            background: rgba(59, 130, 246, 0.1);
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 32px;
            color: var(--primary-light);
        }

        .welcome-text h1 { font-size: 24px; font-weight: 700; margin-bottom: 12px; }
        .welcome-text p { color: #94a3b8; font-size: 15px; margin-bottom: 32px; line-height: 1.5; }

        .form-group { text-align: left; margin-bottom: 24px; position: relative; }
        .form-group label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 8px; color: #cbd5e1; }
        
        .input-wrapper { position: relative; }
        .input-wrapper i { position: absolute; left: 16px; top: 50%; transform: translateY(-50%); width: 18px; height: 18px; color: #64748b; }
        
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

        .form-control:focus { outline: none; border-color: var(--primary-light); background: rgba(255, 255, 255, 0.1); }

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
            display: flex; align-items: center; justify-content: center; gap: 10px;
        }

        .btn-submit:hover { transform: translateY(-2px); box-shadow: 0 10px 15px -3px rgba(37, 99, 235, 0.3); }

        .error-msg { background: rgba(239, 68, 68, 0.1); color: #f87171; padding: 12px; border-radius: 8px; font-size: 13px; margin-bottom: 24px; }
        .success-msg { background: rgba(16, 185, 129, 0.1); color: #34d399; padding: 12px; border-radius: 8px; font-size: 13px; margin-bottom: 24px; }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="icon-box">
            <i data-lucide="key-round" style="width: 32px; height: 32px;"></i>
        </div>

        <div class="welcome-text">
            <h1>Forgot Password</h1>
            <p>Enter your email address to receive password reset instructions.</p>
        </div>

        <?php if (isset($error)): ?>
            <div class="error-msg"><?php echo htmlspecialchars($error); ?></div>
        <?php endif; ?>
        <?php if (isset($success)): ?>
            <div class="success-msg"><?php echo htmlspecialchars($success); ?></div>
            <div style="margin-top: 24px;">
                <a href="login.php" class="btn-submit" style="text-decoration: none;">Back to Login</a>
            </div>
        <?php else: ?>
            <form method="POST">
                <div class="form-group">
                    <label>Email Address</label>
                    <div class="input-wrapper">
                        <i data-lucide="mail"></i>
                        <input type="email" name="email" class="form-control" placeholder="Enter your email" required autofocus>
                    </div>
                </div>

                <button type="submit" class="btn-submit">
                    <span>Send Reset Link</span>
                    <i data-lucide="send" style="width: 18px; height: 18px;"></i>
                </button>
            </form>

            <div style="margin-top: 32px;">
                <a href="login.php" style="color: #94a3b8; text-decoration: none; font-size: 14px; font-weight: 500;">Back to Login</a>
            </div>
        <?php endif; ?>
    </div>

    <script>lucide.createIcons();</script>
</body>
</html>
