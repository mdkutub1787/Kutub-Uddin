<?php
include_once "models/Report.php";
class ReportController {
    private $report;
    public function __construct($conn) { $this->report = new Report($conn); }
    public function index() {
        $summary = $this->report->summary();
        include "views/reports/summary.php";
    }
}
