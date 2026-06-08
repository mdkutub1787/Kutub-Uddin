package com.mrahmed.myocr.firestore_helper;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {

    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;

    public FirestoreHelper() {
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("scanned_texts");
    }

    public interface FirestoreListCallback {
        void onSuccess(Map<String, String> fileList);

        void onFailure(String error);
    }

    public interface FirestoreCallback {
        void onSuccess();

        void onFailure(String error);
    }
    public interface FirestoreTextCallback {
        void onSuccess(String text);

        void onFailure(String error);
    }
    public interface FirestoreMessageCallback {
        void onSuccess(String message);

        void onFailure(String error);
    }

    public void saveOCRText(String documentId,String text, FirestoreMessageCallback callback) {
        // Create a unique document ID
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("timestamp", com.google.firebase.Timestamp.now());

        collectionReference.document(documentId)
                .set(data)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Saved"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
    public void updateOCRText(String documentId, String text, FirestoreCallback callback) {
        DocumentReference docRef = collectionReference.document(documentId);

        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("timestamp", com.google.firebase.Timestamp.now());

        docRef.update(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreHelper", "Document updated successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreHelper", "Error updating document: " + e.getMessage());
                    callback.onFailure(e.getMessage());
                });
    }


    public void getAllOCRFiles(FirestoreListCallback callback) {
        collectionReference
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, String> fileList = new HashMap<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                fileList.put(document.getId(), document.getString("text"));
                            }
                        }
                        callback.onSuccess(fileList);
                    } else {
                        callback.onFailure("Error getting documents: " + task.getException());
                    }
                });
    }
    public void getOCRText(String documentId, FirestoreTextCallback callback) {
        collectionReference.document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String text = documentSnapshot.getString("text");
                        callback.onSuccess(text);
                    } else {
                        callback.onFailure("Document not found");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}