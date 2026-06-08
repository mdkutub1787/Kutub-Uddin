<?php
/**
 * SupportController - Standard PHP MVC
 */
session_start();
require_once __DIR__ . '/../includes/functions.php';

class SupportController {
    public function index() {
        if (!isset($_SESSION['token'])) { 
            header('Location: ../auth/login.php'); 
            exit; 
        }
        
        $service = new SupportService();
        try {
            $tickets = $service->getTickets();
            include __DIR__ . '/../views/support/support_view.php';
        } catch (Exception $e) { 
            $tickets = []; 
            include __DIR__ . '/../views/support/support_view.php'; 
        }
    }
}

(new SupportController())->index();
?>
