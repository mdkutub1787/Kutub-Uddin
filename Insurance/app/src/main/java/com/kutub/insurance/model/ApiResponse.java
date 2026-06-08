package com.kutub.insurance.model;

public class ApiResponse {
    private boolean status;
    private ResultSet resultSet;
    private String message;

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Inner class for ResultSet
    public static class ResultSet {
        private String saveMessage;

        // Getters and Setters
        public String getSaveMessage() {
            return saveMessage;
        }

        public void setSaveMessage(String saveMessage) {
            this.saveMessage = saveMessage;
        }
    }
}