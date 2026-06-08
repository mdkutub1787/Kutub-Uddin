<?php
require_once __DIR__ . '/base_service.php';

class NotificationService extends BaseService {

    public function getNotifications() {
        $response = $this->request(ApiEndpoints::NOTIFICATIONS, 'GET');
        return map_notifications($response['data'] ?? []);
    }

    public function readNotification($id) {
        return $this->request(ApiEndpoints::READ_NOTIFICATION($id), 'GET');
    }
}
