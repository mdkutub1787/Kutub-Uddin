package com.kutub.paymentapp.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kutub.paymentapp.model.User;

public class PaymentService {

    private DatabaseReference database;

    public PaymentService() {
        // Initialize Firebase Realtime Database reference
        database = FirebaseDatabase.getInstance().getReference("users");
    }

    /**
     * Method to send money from one user to another.
     *
     * @param senderId           UserId of the sender
     * @param receiverPhoneNumber Phone number of the receiver
     * @param amount             Amount to be sent
     */
    public void sendMoney(String senderId, String receiverPhoneNumber, String amount) {
        // Fetch the sender's details from Firebase
        database.child(senderId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User sender = task.getResult().getValue(User.class);

                if (sender != null) {
                    double senderBalance = Double.parseDouble(sender.getBalance());
                    double transferAmount = Double.parseDouble(amount);

                    // Check if sender has enough balance
                    if (senderBalance >= transferAmount) {
                        // Fetch the receiver's details from Firebase using phone number
                        fetchReceiverAndTransferMoney(sender, senderId, receiverPhoneNumber, transferAmount);
                    } else {
                        logError("Insufficient funds for transfer.");
                    }
                } else {
                    logError("Sender not found.");
                }
            } else {
                logError("Error fetching sender data: " + task.getException().getMessage());
            }
        });
    }

    private void fetchReceiverAndTransferMoney(User sender, String senderId, String receiverPhoneNumber, double transferAmount) {
        database.orderByChild("phoneNumber").equalTo(receiverPhoneNumber).get().addOnCompleteListener(receiverTask -> {
            if (receiverTask.isSuccessful() && receiverTask.getResult().exists()) {
                User receiver = receiverTask.getResult().getChildren().iterator().next().getValue(User.class);
                if (receiver != null) {
                    updateBalancesAndSave(sender, senderId, receiver, transferAmount);
                } else {
                    logError("Receiver not found.");
                }
            } else {
                logError("Receiver not found in the database.");
            }
        });
    }

    private void updateBalancesAndSave(User sender, String senderId, User receiver, double transferAmount) {
        double senderBalance = Double.parseDouble(sender.getBalance());
        double receiverBalance = Double.parseDouble(receiver.getBalance());

        // Update sender and receiver balances
        sender.setBalance(String.valueOf(senderBalance - transferAmount));
        receiver.setBalance(String.valueOf(receiverBalance + transferAmount));

        // Update the Firebase database with new balances
        database.child(senderId).setValue(sender).addOnCompleteListener(senderUpdateTask -> {
            if (senderUpdateTask.isSuccessful()) {
                // Update receiver's balance
                database.child(receiver.getUserId()).setValue(receiver).addOnCompleteListener(receiverUpdateTask -> {
                    if (receiverUpdateTask.isSuccessful()) {
                        System.out.println("Money transfer successful!");
                    } else {
                        logError("Error updating receiver balance: " + receiverUpdateTask.getException().getMessage());
                    }
                });
            } else {
                logError("Error updating sender balance: " + senderUpdateTask.getException().getMessage());
            }
        });
    }

    private void logError(String message) {
        System.out.println(message);
    }
}
