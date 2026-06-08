<?php
/**
 * NotificationController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class NotificationController {
    public function index() {
        if (!isset($_SESSION['token'])) { 
            header('Location: ../auth/login.php'); 
            exit; 
        }
        
        $service = new NotificationService();
        try {
            $notifications = $service->getNotifications();
            include __DIR__ . '/../views/notifications/notifications_view.php';
        } catch (Exception $e) { 
            $notifications = []; 
            include __DIR__ . '/../views/notifications/notifications_view.php'; 
        }
    }
}

(new NotificationController())->index();
?>
