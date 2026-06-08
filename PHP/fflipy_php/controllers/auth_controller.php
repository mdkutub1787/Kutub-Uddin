<?php
/**
 * AuthController - Standard PHP MVC
 * Usage: Handle Login, Register, Verify, and Logout
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class AuthController {
    private $authService;

    public function __construct() {
        $this->authService = new AuthService();
    }

    public function login() {
        if (isset($_SESSION['token'])) { header('Location: ../dashboard/home.php'); exit; }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $username = $_POST['username'] ?? '';
            $password = $_POST['password'] ?? '';

            try {
                $response = $this->authService->login($username, $password);
                if ($response && $response->isSuccessful()) {
                    $this->setSession($response);
                    
                    // Check if verification is needed
                    if ($response->user->email_verification == 0) {
                        header('Location: verify.php'); exit;
                    }
                    
                    header('Location: ../dashboard/home.php'); exit;
                }
                $error = __('invalid_credentials');
            } catch (Exception $e) { $error = $e->getMessage(); }
        }
        include __DIR__ . '/../views/auth/login_view.php';
    }

    public function register() {
        if (isset($_SESSION['token'])) { header('Location: ../dashboard/home.php'); exit; }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $data = [
                'firstname' => $_POST['firstname'] ?? '',
                'lastname'  => $_POST['lastname'] ?? '',
                'username'  => $_POST['username'] ?? '',
                'email'     => $_POST['email'] ?? '',
                'phone'     => $_POST['phone'] ?? '',
                'password'  => $_POST['password'] ?? ''
            ];

            try {
                $response = $this->authService->register($data);
                if ($response && $response->isSuccessful()) {
                    $this->setSession($response);
                    
                    // Redirect to verification if required by API
                    if (isset($response->user->email_verification) && $response->user->email_verification == 0) {
                        header('Location: verify.php?msg=' . urlencode($response->message)); exit;
                    }

                    header('Location: ../dashboard/home.php?msg=' . urlencode($response->message)); exit;
                }
                $error = $response->message ?? __('registration_failed');
            } catch (Exception $e) { $error = $e->getMessage(); }
        }
        include __DIR__ . '/../views/auth/register_view.php';
    }

    public function verify() {
        if (!isset($_SESSION['token'])) { header('Location: login.php'); exit; }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            if (isset($_POST['resend'])) {
                try {
                    $this->authService->resendCode();
                    $success = __('code_resent');
                } catch (Exception $e) { $error = $e->getMessage(); }
            } else {
                $code = $_POST['code'] ?? '';
                try {
                    $response = $this->authService->verifyEmail($code);
                    if ($response['success']) {
                        // Update session user if needed, or just redirect
                        header('Location: ../dashboard/home.php?msg=' . urlencode($response['message'])); exit;
                    }
                    $error = $response['message'] ?? __('verification_failed');
                } catch (Exception $e) { $error = $e->getMessage(); }
            }
        }
        include __DIR__ . '/../views/auth/verify_view.php';
    }

    public function forgot_password() {
        if (isset($_SESSION['token'])) { header('Location: ../dashboard/home.php'); exit; }

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $email = $_POST['email'] ?? '';
            try {
                $response = $this->authService->forgotPassword($email);
                if (isset($response['success']) && $response['success'] === true || isset($response['status']) && $response['status'] === true) {
                    $success = $response['message'] ?? 'Password reset link sent to your email.';
                } else {
                    $error = $response['message'] ?? 'Failed to send reset link.';
                }
            } catch (Exception $e) { $error = $e->getMessage(); }
        }
        include __DIR__ . '/../views/auth/forgot_password_view.php';
    }

    private function setSession($response) {
        $_SESSION['token'] = $response->token;
        $_SESSION['user']  = serialize($response->user);
        $_SESSION['lang']  = $response->user->language ?? 'en';
    }

    public function logout() {
        session_destroy();
        header('Location: ../auth/login.php'); exit;
    }
}

$controller = new AuthController();
$action = $_GET['action'] ?? 'login';

if ($action === 'logout') {
    $controller->logout();
} elseif ($action === 'register') {
    $controller->register();
} elseif ($action === 'verify') {
    $controller->verify();
} elseif ($action === 'forgot_password') {
    $controller->forgot_password();
} else {
    $controller->login();
}
?>
