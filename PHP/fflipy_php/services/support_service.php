<?php
require_once __DIR__ . '/base_service.php';

class SupportService extends BaseService {

    public function getTickets() {
        $response = $this->request(ApiEndpoints::SUPPORT_TICKETS, 'GET');
        return map_support_tickets($response['data'] ?? []);
    }

    public function getTicketDetails($id) {
        $response = $this->request(ApiEndpoints::VIEW_TICKET($id), 'GET');
        return [
            'ticket' => new SupportTicket($response['data']['my_ticket'] ?? []),
            'messages' => map_ticket_messages($response['data']['my_reply'] ?? [])
        ];
    }

    public function createTicket($subject, $message) {
        return $this->request(ApiEndpoints::STORE_TICKET, 'POST', [
            'subject' => $subject,
            'message' => $message
        ]);
    }

    public function replyTicket($id, $message) {
        return $this->request(ApiEndpoints::REPLY_TICKET($id), 'POST', [
            'replayTicket' => 1,
            'message' => $message
        ]);
    }
}
