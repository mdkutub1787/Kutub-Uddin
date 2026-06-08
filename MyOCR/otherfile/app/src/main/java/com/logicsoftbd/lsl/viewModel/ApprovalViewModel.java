package com.logicsoftbd.lsl.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalMenuDetails;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalNotificationsModel;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ApprovalResponseModel;
import com.logicsoftbd.lsl.repository.ApprovalRepository;

public class ApprovalViewModel extends ViewModel {
    private ApprovalRepository approvalRepository;

    public ApprovalViewModel() {
        approvalRepository = new ApprovalRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return approvalRepository.getIsLoading();
    }

    public MutableLiveData<V1_ApprovalNotificationsModel> getApprovalResponse(String user_id) {
        return approvalRepository.getApprovalMenu(user_id);
    }

    public MutableLiveData<V1_ApprovalMenuDetails> getApprovalDetailsResponse(String user_id, String menu_id) {
        return approvalRepository.getApprovalDetails(user_id, menu_id);
    }

    public MutableLiveData<V1_ApprovalResponseModel> postApprovalResponse(String user_id, String menu_id, String ref_no) {
        return approvalRepository.postApproval(user_id, menu_id, ref_no);
    }

    public MutableLiveData<V1_ApprovalResponseModel> postUnApprovalResponse(String user_id, String menu_id, String ref_no) {
        return approvalRepository.postUnApproval(user_id, menu_id, ref_no);
    }

    public MutableLiveData<V1_ApprovalResponseModel> postDenyApprovalResponse(String user_id, String menu_id, String ref_no, String message) {
        return approvalRepository.postDenyApproval(user_id, menu_id, ref_no, message);
    }


}
