package com.kutub.paymentapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kutub.paymentapp.R;

public class SendMoneyActivity extends AppCompatActivity {

    private static final String TAG = "SendMoneyActivity";

    private EditText amountEditText, phoneNumberEditText;
    private Button sendMoneyButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        // Initialize views
        amountEditText = findViewById(R.id.amountEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        sendMoneyButton = findViewById(R.id.sendMoneyButton);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Set up button click listener
        sendMoneyButton.setOnClickListener(view -> {
            String amount = amountEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();

            if (amount.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(SendMoneyActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get current user ID (sender)
            String senderId = mAuth.getCurrentUser().getUid();

            // Call the sendMoney method to initiate the transfer
            sendMoney(senderId, phoneNumber, amount);
        });
    }

    private void sendMoney(String senderId, String receiverPhoneNumber, String amount) {
        // Validate the amount format
        double sendAmount = parseDoubleSafely(amount);
        if (sendAmount <= 0) {
            Toast.makeText(SendMoneyActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Original phone number: '" + receiverPhoneNumber + "'");

        // Cleaning with multiple steps
        String cleanPhoneNumber = receiverPhoneNumber;
        cleanPhoneNumber = cleanPhoneNumber.replaceAll("[^0-9]", ""); // Remove non-numeric characters
        Log.d(TAG, "Cleaned phone number after removing non-numeric: '" + cleanPhoneNumber + "'");
        cleanPhoneNumber = cleanPhoneNumber.trim(); // Remove leading/trailing spaces
        Log.d(TAG, "Cleaned phone number after trim: '" + cleanPhoneNumber + "'");

        // Validate the format of the phone number
        if (cleanPhoneNumber.length() < 8 || cleanPhoneNumber.length() > 14) {
            Toast.makeText(SendMoneyActivity.this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Querying for phone number: " + cleanPhoneNumber);

        // 1. Get sender's current balance
        DatabaseReference senderRef = databaseReference.child(senderId);

        String finalCleanPhoneNumber = cleanPhoneNumber;
        senderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot task) {
                if (task.exists()) {
                    Object senderBalanceObj = task.child("balance").getValue();
                    if (senderBalanceObj == null) {
                        Log.e(TAG, "Sender balance is invalid or missing");
                        Toast.makeText(SendMoneyActivity.this, "Sender balance is invalid or missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Double senderBalance = null;
                    if (senderBalanceObj instanceof Long) {
                        senderBalance = ((Long) senderBalanceObj).doubleValue();
                    } else if (senderBalanceObj instanceof Double) {
                        senderBalance = (Double) senderBalanceObj;
                    } else if (senderBalanceObj instanceof String) {
                        try {
                            senderBalance = Double.parseDouble((String) senderBalanceObj);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Sender balance is invalid or missing", e);
                            Toast.makeText(SendMoneyActivity.this, "Sender balance format is invalid", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (senderBalance == null) {
                        Log.e(TAG, "Sender balance is invalid or missing");
                        Toast.makeText(SendMoneyActivity.this, "Sender balance is invalid or missing", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // Check if sender has sufficient balance
                    if (senderBalance >= sendAmount) {
                        // Deduct the sender's balance
                        double newSenderBalance = senderBalance - sendAmount;
                        senderRef.child("balance").setValue(newSenderBalance);

                        // 2. Get recipient's data using phone number (assuming phone number is unique)
                        Log.d(TAG, "Database reference before query: " + databaseReference.toString()); // Check the reference
                        Log.d(TAG, "cleanPhoneNumber before query: " + finalCleanPhoneNumber); // Check the number
                        databaseReference.orderByChild("phoneNumber").equalTo(finalCleanPhoneNumber)
                                .get().addOnCompleteListener(receiverTask -> {
                                    if (!receiverTask.isSuccessful()) {
                                        Log.e(TAG, "Query failed: " + receiverTask.getException().toString(), receiverTask.getException());
                                    }
                                    if (receiverTask.isSuccessful() && receiverTask.getResult() != null && receiverTask.getResult().exists()) {
                                        // Fetch the receiver's ID
                                        String receiverId = receiverTask.getResult().getChildren().iterator().next().getKey();
                                        Log.d(TAG, "Found receiver: " + receiverId);

                                        if (receiverId != null) {
                                            // Retrieve the receiver's balance
                                            double receiverBalance = getBalanceFromSnapshot(receiverTask.getResult(), receiverId);

                                            // Add the amount to the receiver's balance
                                            double newReceiverBalance = receiverBalance + sendAmount;
                                            databaseReference.child(receiverId).child("balance").setValue(newReceiverBalance);

                                            // Show success message
                                            Toast.makeText(SendMoneyActivity.this, "Money Sent Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // If recipient is not found, show error
                                        Log.e(TAG, "No results found for phone number: " + finalCleanPhoneNumber);
                                        Toast.makeText(SendMoneyActivity.this, "Recipient not found!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SendMoneyActivity.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If failed to retrieve sender's data, show error
                    Log.e(TAG, "Failed to retrieve sender data");
                    Toast.makeText(SendMoneyActivity.this, "Failed to retrieve sender data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to retrieve sender data", error.toException());
                Toast.makeText(SendMoneyActivity.this, "Failed to retrieve sender data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double parseDoubleSafely(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Double.parseDouble(value.replaceAll("[^0-9.]", "")); // Remove non-numeric characters
            } catch (NumberFormatException e) {
                Log.e(TAG, "Failed to parse value: " + value, e);
                Toast.makeText(SendMoneyActivity.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SendMoneyActivity.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        }
        return 0.0;
    }

    private double getBalanceFromSnapshot(DataSnapshot snapshot, String userId) {
        Object balanceObj = snapshot.child(userId).child("balance").getValue();
        if (balanceObj instanceof Long) {
            return ((Long) balanceObj).doubleValue();
        } else if (balanceObj instanceof Double) {
            return (Double) balanceObj;
        } else if (balanceObj instanceof String){
            return parseDoubleSafely((String) balanceObj);
        } else {
            Log.e(TAG, "Balance for user " + userId + " is invalid or missing");
            return 0.0;
        }
    }
}