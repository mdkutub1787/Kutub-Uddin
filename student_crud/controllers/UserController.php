<?php
include_once "models/User.php";
class UserController {
    private $user;
    public function __construct($conn) { $this->user = new User($conn); }
    public function login() {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $username = $_POST['username'];
            $password = $_POST['password'];
            $user = $this->user->findByUsername($username);
            if ($user && password_verify($password, $user['password'])) {
                $_SESSION['user'] = $user;
                echo json_encode(['success'=>true,'redirect'=>'home.php']); exit;
            } else {
                $error = 'ভুল ইউজারনেম বা পাসওয়ার্ড!';
            }
        }
        include "views/users/login.php";
    }
    public function logout() {
        session_destroy();
        header('Location: index.php');
        exit;
    }
}
