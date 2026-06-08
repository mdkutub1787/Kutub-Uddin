package com.kutub.insurance.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kutub.insurance.R;
import com.kutub.insurance.adpter.PolicyAdapter;
import com.kutub.insurance.model.PolicyResponse;
import com.kutub.insurance.viewModel.InsuranceViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PolicyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PolicyAdapter policyAdapter;
    private InsuranceViewModel insuranceViewModel;
    private List<PolicyResponse> policyList = new ArrayList<>();

    // UI elements
    private EditText etPolicyHolder, etBankName, etAddress, etSumInsured, etCoverage, etDate, etPeriodFrom, etPeriodTo;
    private Spinner spinnerConstruction, spinnerUsedAs;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        // Initialize views
        initializeViews();

        // Set up RecyclerView
        setupRecyclerView();

        // Initialize ViewModel
        insuranceViewModel = new ViewModelProvider(this).get(InsuranceViewModel.class);

        // Set current date to etDate and etPeriodFrom
        setCurrentDate(etDate);
        setCurrentDate(etPeriodFrom);

        // Add DatePicker to date fields
        etDate.setOnClickListener(v -> showDatePicker(etDate));
        etPeriodFrom.setOnClickListener(v -> showDatePicker(etPeriodFrom));
        etPeriodTo.setOnClickListener(v -> showDatePicker(etPeriodTo));

        // Observe data
        observeData();

        // Set click listener for the submit button
        btnSubmit.setOnClickListener(v -> {
            try {
                postPolicyRequest();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating policy request!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        etDate = findViewById(R.id.etDate);
        etPolicyHolder = findViewById(R.id.etPolicyHolder);
        etBankName = findViewById(R.id.etBankName);
        etAddress = findViewById(R.id.etAddress);
        etSumInsured = findViewById(R.id.etSumInsured);
        etCoverage = findViewById(R.id.etCoverage);
        etPeriodFrom = findViewById(R.id.etPeriodFrom);
        etPeriodTo = findViewById(R.id.etPeriodTo);
        spinnerConstruction = findViewById(R.id.spinnerConstruction);
        spinnerUsedAs = findViewById(R.id.spinnerUsedAs);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up adapters for spinners
        ArrayAdapter<CharSequence> constructionAdapter = ArrayAdapter.createFromResource(
                this, R.array.construction_types, android.R.layout.simple_spinner_item);
        constructionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConstruction.setAdapter(constructionAdapter);

        ArrayAdapter<CharSequence> usedAsAdapter = ArrayAdapter.createFromResource(
                this, R.array.used_as_types, android.R.layout.simple_spinner_item);
        usedAsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsedAs.setAdapter(usedAsAdapter);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        policyAdapter = new PolicyAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setAdapter(policyAdapter);
    }

    private void observeData() {
        showProgressBar();
        insuranceViewModel.getPolicy().observe(this, policies -> {
            hideProgressBar();
            if (policies != null) {
                Log.d("PolicyActivity", "Fetched Policies: " + policies);
                policyList.clear();
                policyList.addAll(policies);

                policyAdapter = new PolicyAdapter(policyList);
                recyclerView.setAdapter(policyAdapter);
            } else {
                Log.e("PolicyActivity", "No policies found or null response.");
                Toast.makeText(this, "No policies found. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postPolicyRequest() throws JSONException {
        // Collect data from EditText fields
        String date = etDate.getText().toString().trim();
        String policyHolder = etPolicyHolder.getText().toString().trim();
        String bankName = etBankName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String sumInsured = etSumInsured.getText().toString().trim();
        String coverage = etCoverage.getText().toString().trim();
        String construction = spinnerConstruction.getSelectedItem().toString();
        String usedAs = spinnerUsedAs.getSelectedItem().toString();
        String periodFrom = etPeriodFrom.getText().toString().trim();
        String periodTo = etPeriodTo.getText().toString().trim();

        // Validate input
        if (date.isEmpty() || policyHolder.isEmpty() || bankName.isEmpty() || address.isEmpty() ||
                sumInsured.isEmpty() || coverage.isEmpty() || construction.equals("Select Construction") ||
                usedAs.equals("Select Use As") || periodFrom.isEmpty() || periodTo.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate for duplicates
        if (isDuplicatePolicy(policyHolder, bankName, address, sumInsured)) {
            Toast.makeText(this, "This policy already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create JSON object
        JSONObject policyObj = new JSONObject();
        policyObj.put("date", date);
        policyObj.put("policyholder", policyHolder);
        policyObj.put("bankname", bankName);
        policyObj.put("address", address);
        policyObj.put("sumInsured", sumInsured);
        policyObj.put("coverage", coverage);
        policyObj.put("construction", construction);
        policyObj.put("usedAs", usedAs);
        policyObj.put("periodFrom", periodFrom);
        policyObj.put("periodTo", periodTo);

        Log.d("PolicyActivity", "Policy JSON: " + policyObj);

        // Create RequestBody for Retrofit
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, policyObj.toString());

        // Show progress bar
        showProgressBar();

        // Call the ViewModel to post the policy
        insuranceViewModel.postPolicy(body).observe(this, apiResponse -> {
            hideProgressBar();
            if (apiResponse != null) {
                Log.d("PolicyActivity", "API Response: " + apiResponse);
                if (apiResponse.isStatus() || apiResponse.getResultSet() != null) {
                    String saveMsg = (apiResponse.getResultSet() != null && apiResponse.getResultSet().getSaveMessage() != null)
                            ? apiResponse.getResultSet().getSaveMessage()
                            : "Policy saved successfully!";
                    Toast.makeText(this, saveMsg, Toast.LENGTH_SHORT).show();
                    Log.d("PolicyActivity", "Response: " + saveMsg);

                    // Clear fields after saving
                    clearFields();

                    // Reload data after saving
                    observeData();
                } else {
                    String errorMsg = "Policy saved successfully!";
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                    Log.e("PolicyActivity", "Error: " + errorMsg);
                    observeData();
                }
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.e("PolicyActivity", "API Response is null");
            }
        });
    }
    private boolean isDuplicatePolicy(String policyHolder, String bankName, String address, String sumInsured) {
        for (PolicyResponse policy : policyList) {
            if (policy.getPolicyholder().equalsIgnoreCase(policyHolder) &&
                    policy.getBankname().equalsIgnoreCase(bankName) &&
                    policy.getAddress().equalsIgnoreCase(address) &&
                    String.valueOf(policy.getSumInsured()).equalsIgnoreCase(sumInsured)) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicate found
    }

    private void setCurrentDate(TextView textView) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        textView.setText(currentDate);
    }

    private void showDatePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            textView.setText(selectedDate);

            // If etPeriodFrom is selected, update etPeriodTo to one year later
            if (textView.getId() == R.id.etPeriodFrom) {
                Calendar oneYearLater = Calendar.getInstance();
                oneYearLater.set(selectedYear, selectedMonth, selectedDay);
                oneYearLater.add(Calendar.YEAR, 1); // Add one year
                String oneYearLaterDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(oneYearLater.getTime());
                etPeriodTo.setText(oneYearLaterDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void clearFields() {
        etDate.setText("");
        etPolicyHolder.setText("");
        etBankName.setText("");
        etAddress.setText("");
        etSumInsured.setText("");
        etCoverage.setText("");
        spinnerConstruction.setSelection(0);
        spinnerUsedAs.setSelection(0);
        etPeriodFrom.setText("");
        etPeriodTo.setText("");
    }
}