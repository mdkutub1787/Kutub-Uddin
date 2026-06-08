package com.kutub.insurance.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.kutub.insurance.adpter.BillAdapter;
import com.kutub.insurance.model.ApiResponse;
import com.kutub.insurance.model.BillResponse;
import com.kutub.insurance.model.PolicyResponse;
import com.kutub.insurance.viewModel.InsuranceViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BillActivity extends AppCompatActivity {

    private Spinner spinnerPolicyHolder;
    private EditText etFire, etRsd, etNetPremium, etGrossPremium, etTax;
    private TextView tvBankName, tvSumInsured;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private List<PolicyResponse> policyList = new ArrayList<>();
    private PolicyResponse selectedPolicy;

    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private InsuranceViewModel insuranceViewModel;
    // Add a variable to store the tax rate
    private double storedTaxRate = 0.0;
    // Add a variable to store the fire rate
    private double storedFireRate = 0.0;
    // Add a variable to store the rsd rate
    private double storedRsdRate = 0.0;
    // Add a constant for the default spinner text
    private static final String DEFAULT_SPINNER_TEXT = "Select Policyholder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initializeUI();
        initializeViewModel();
        loadPolicyAndBillData();
        btnSubmit.setOnClickListener(v -> saveBill());
    }

    private void initializeUI() {
        spinnerPolicyHolder = findViewById(R.id.spinnerPolicyHolder);
        tvBankName = findViewById(R.id.tvBankName);
        tvSumInsured = findViewById(R.id.tvSumInsured);
        etFire = findViewById(R.id.etFire);
        etRsd = findViewById(R.id.etRsd);
        etNetPremium = findViewById(R.id.etNetPremium);
        etGrossPremium = findViewById(R.id.etGrossPremium);
        etTax = findViewById(R.id.etTax);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        billAdapter = new BillAdapter();
        recyclerView.setAdapter(billAdapter);
        setupTextWatchers();
    }

    private void initializeViewModel() {
        insuranceViewModel = new ViewModelProvider(this).get(InsuranceViewModel.class);
    }

    private void loadPolicyAndBillData() {
        progressBar.setVisibility(View.VISIBLE);

        android.util.Log.d("API_DEBUG", "Fetching policies from https://6807123ce81df7060eb8baf2.mockapi.io/policy");

        insuranceViewModel.getPolicy().observe(this, policies -> {
            if (policies != null) {
                android.util.Log.d("API_RESPONSE", "Policy API Response: " + policies.toString());

                policyList.clear();
                policyList.addAll(policies);
                setupPolicyHolderSpinner();
                fetchBills();
            } else {
                android.util.Log.e("API_ERROR", "Failed to fetch policies.");
                showError("Failed to load policies.");
            }
        });
    }

    private void fetchBills() {
        android.util.Log.d("API_DEBUG", "Fetching bills from https://6807123ce81df7060eb8baf2.mockapi.io/bill");

        insuranceViewModel.getBill().observe(this, bills -> {
            if (bills != null) {
                android.util.Log.d("API_RESPONSE", "Bill API Response: " + bills.toString());

                mapBillsToPolicies(bills);
                billAdapter.updateData(policyList, bills);
            } else {
                android.util.Log.e("API_ERROR", "Failed to fetch bills.");
                showError("Failed to load bills.");
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void mapBillsToPolicies(List<BillResponse> bills) {
        for (PolicyResponse policy : policyList) {
            for (BillResponse bill : bills) {
                android.util.Log.d("MAPPING_DEBUG", "Mapping Policy ID: " + policy.getId() + " with Bill ID: " + bill.getId());
                if (policy.getId().equals(bill.getId())) {
                    policy.setBillResponse(bill);
                    android.util.Log.d("MAPPING_DEBUG", "Mapped Bill: " + bill.toString() + " to Policy: " + policy.toString());
                    break;
                }
            }
        }
    }

    private void setupPolicyHolderSpinner() {
        // Extract policy holder names from policyList
        List<String> policyHolderNames = policyList.stream()
                .map(PolicyResponse::getPolicyholder)
                .collect(Collectors.toList());
        // Create a new list to hold the spinner items, starting with the default text
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add(DEFAULT_SPINNER_TEXT);
        spinnerItems.addAll(policyHolderNames);

        // Create ArrayAdapter and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPolicyHolder.setAdapter(adapter);

        // Set up item selected listener
        spinnerPolicyHolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the default text is selected
                if (position == 0) {
                    selectedPolicy = null;
                    clearBillFields();
                } else {
                    // Adjust the position to account for the default item
                    selectedPolicy = policyList.get(position - 1);
                    updateUIWithPolicyAndBillData();
                    calculatePremium();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPolicy = null;
                clearBillFields();
            }
        });
        // Set the default selection to "Select Policyholder"
        spinnerPolicyHolder.setSelection(0);
    }


    private void updateUIWithPolicyAndBillData() {
        if (selectedPolicy != null) {
            tvBankName.setText(selectedPolicy.getBankname());
            tvSumInsured.setText(String.valueOf(selectedPolicy.getSumInsured()));

            BillResponse bill = selectedPolicy.getBillResponse();
            if (bill != null) {
                etFire.setText(String.valueOf(bill.getFire()));
                etRsd.setText(String.valueOf(bill.getRsd()));
                etNetPremium.setText(String.valueOf(bill.getNetPremium()));
                etGrossPremium.setText(String.valueOf(bill.getGrossPremium()));
                // if a previous tax rate was stored, set it
                if (bill.getTax() != 0.0) {
                    etTax.setText(String.valueOf(bill.getTax()));
                }
                // Store the rates when updating the UI
                storedTaxRate = bill.getTax();
                storedFireRate = bill.getFire();
                storedRsdRate = bill.getRsd();
            } else {
                clearBillFields();
            }
        }
    }

    private void clearBillFields() {
        etFire.setText("");
        etRsd.setText("");
        etNetPremium.setText("");
        etGrossPremium.setText("");
        etTax.setText("");
        //reset when clear all feild.
        storedTaxRate = 0.0;
        storedFireRate = 0.0;
        storedRsdRate = 0.0;
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculatePremium();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        TextWatcher textTaxWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Store the tax rate when it changes
                try {
                    if (!s.toString().isEmpty()) {
                        storedTaxRate = Double.parseDouble(s.toString());
                    } else {
                        storedTaxRate = 0.0;
                    }
                } catch (NumberFormatException e) {
                    storedTaxRate = 0.0;
                }

                calculatePremium();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textFireWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Store the fire rate when it changes
                try {
                    if (!s.toString().isEmpty()) {
                        storedFireRate = Double.parseDouble(s.toString());
                    } else {
                        storedFireRate = 0.0;
                    }
                } catch (NumberFormatException e) {
                    storedFireRate = 0.0;
                }

                calculatePremium();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textRsdWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Store the rsd rate when it changes
                try {
                    if (!s.toString().isEmpty()) {
                        storedRsdRate = Double.parseDouble(s.toString());
                    } else {
                        storedRsdRate = 0.0;
                    }
                } catch (NumberFormatException e) {
                    storedRsdRate = 0.0;
                }

                calculatePremium();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etFire.addTextChangedListener(textFireWatcher);
        etRsd.addTextChangedListener(textRsdWatcher);
        etTax.addTextChangedListener(textTaxWatcher);
    }

    private void saveBill() {
        if (selectedPolicy == null) {
            showError("Please select a policyholder.");
            android.util.Log.e("CALCULATION_ERROR", "No policy selected.");
            return;
        }

        // Ensure calculatePremium() is called before creating BillResponse
        calculatePremium();

        //Check validation
        if (isBillAlreadyExists(selectedPolicy)) {
            showError("Bill already exists for this policyholder.");
            return;
        }

        try {
            double fireRate = Double.parseDouble(etFire.getText().toString());
            double rsdRate = Double.parseDouble(etRsd.getText().toString());
            double netPremium = Double.parseDouble(etNetPremium.getText().toString());
            double grossPremium = Double.parseDouble(etGrossPremium.getText().toString());

            // Create a JSONObject to hold the data
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fire", fireRate);
            jsonObject.put("rsd", rsdRate);
            jsonObject.put("netPremium", netPremium);
            jsonObject.put("grossPremium", grossPremium);
            jsonObject.put("tax", storedTaxRate);
            jsonObject.put("id", selectedPolicy.getId());


            // Convert the JSONObject to a RequestBody
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

            // Now you have RequestBody object, proceed to post it
            insuranceViewModel.postBill(requestBody).observe(this, response -> {
                if (response != null) {
                    Toast.makeText(this, "Bill saved successfully!", Toast.LENGTH_SHORT).show();

                    // Refresh data after saving
                    loadPolicyAndBillData();
                } else {
                    showError("Failed to save bill.");
                }
            });
        } catch (Exception e) {
            showError("Invalid input. Please check your data.");
        }
    }
    //Method to check Bill already Exist or Not.
    private boolean isBillAlreadyExists(PolicyResponse policy) {
        if (policy.getBillResponse() != null) {
            return true;
        }
        return false;
    }

    private void calculatePremium() {
        if (selectedPolicy == null) {
            // showError("Please select a policyholder.");
            android.util.Log.e("CALCULATION_ERROR", "No policy selected.");
            return;
        }

        try {
            double sumInsured = selectedPolicy.getSumInsured();
            //Check and set default value
            // Use stored rates if they are set, otherwise get from et fields
            double fireRate = (storedFireRate != 0.0) ? storedFireRate : (etFire.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(etFire.getText().toString()));
            double rsdRate = (storedRsdRate != 0.0) ? storedRsdRate : (etRsd.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(etRsd.getText().toString()));
            double taxRate = (storedTaxRate != 0.0) ? storedTaxRate : (etTax.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(etTax.getText().toString()));


            android.util.Log.d("CALCULATION_DEBUG", "Sum Insured: " + sumInsured);
            android.util.Log.d("CALCULATION_DEBUG", "Fire Rate: " + fireRate);
            android.util.Log.d("CALCULATION_DEBUG", "RSD Rate: " + rsdRate);
            android.util.Log.d("CALCULATION_DEBUG", "tax Rate: " + taxRate);

            if (fireRate > 100 || rsdRate > 100 || taxRate > 100) {
                showError("Rates must be less than or equal to 100%.");
                return;
            }

            double netPremium = (sumInsured * (fireRate + rsdRate)) / 100;
            double grossPremium = netPremium + (netPremium * taxRate) / 100;

            netPremium = Math.round(netPremium);
            grossPremium = Math.round(grossPremium);

            android.util.Log.d("CALCULATION_DEBUG", "Net Premium: " + netPremium);
            android.util.Log.d("CALCULATION_DEBUG", "Gross Premium: " + grossPremium);

            etNetPremium.setText(String.format("%.0f", netPremium));
            etGrossPremium.setText(String.format("%.0f", grossPremium));

            //  Toast.makeText(this, "Bill calculated successfully!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            // showError("Please enter valid numbers.");
            android.util.Log.e("CALCULATION_ERROR", "Invalid input: " + e.getMessage());
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}