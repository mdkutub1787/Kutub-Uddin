package com.logicsoftbd.lsl.ui.process;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.data.network.ApiHelper;
import com.logicsoftbd.lsl.data.network.model.MenuResponse;
import com.logicsoftbd.lsl.data.prefs.PreferencesHelper;
import com.logicsoftbd.lsl.ui.base.BaseInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by janisharali on 20/07/17.
 */

public class ProcessInteractor extends BaseInteractor
        implements ProcessMvpInteractor {

    @Inject
    public ProcessInteractor(PreferencesHelper preferencesHelper,
                             ApiHelper apiHelper) {

        super(preferencesHelper, apiHelper);
    }
    @Override
    public String getUsername() {
        return String.valueOf(getPreferencesHelper().getCurrentUserName());    }

    @Override
    public String getPassword() {
        return String.valueOf(getPreferencesHelper().getAccessToken());
    }
    @Override
    public Observable<List<Process>> getProcessData() {
        return getProcessList();
    }

    @Override
    public Observable<MenuResponse> getMenuList(String userName,String password) {
        return getApiHelper().getMenuList(userName,password);
    }

    private Observable<List<Process>> getProcessList() {
        List<Process> processList = new ArrayList<>();

        processList.add(new Process(R.drawable.grey_roll_receive, "Grey Roll Receive", "Receive",
                new Process.DataParam("grey_roll", "receive")));

        processList.add(new Process(R.drawable.grey_roll_issue, "Grey Roll Issue", "Issue",
                new Process.DataParam("grey_roll", "issue")));

        processList.add(new Process(R.drawable.process, "Cutting Qc", "Cutting Qc",
                new Process.DataParam("cutting_qc", "input")));
        processList.add(new Process(R.drawable.process, "Print Issue","Print Issue",
                new Process.DataParam("print", "issue")));

        processList.add(new Process(R.drawable.process, "Print Receive","Print Receive",
                new Process.DataParam("print", "receive")));

        processList.add(new Process(R.drawable.process, "Embroidery Issue","Embroidery Issue",
                new Process.DataParam("embroidery", "issue")));

        processList.add(new Process(R.drawable.process, "Embroidery Receive","Embroidery Receive",
                new Process.DataParam("embroidery", "receive")));

        processList.add(new Process(R.drawable.process, "Special Work Issue","Special Work Issue",
                new Process.DataParam("special_work", "issue")));

        processList.add(new Process(R.drawable.process, "Special Work Receive","Special Work Receive",
                new Process.DataParam("special_work", "receive")));

        processList.add(new Process(R.drawable.sewing, "Sewing Input", "Sewing",
                new Process.DataParam("sewing", "input")));

        processList.add(new Process(R.drawable.sewing, "Sewing Output", "Sewing",
                new Process.DataParam("sewing", "output")));
        processList.add(new Process(R.drawable.process, "Finish Fabric Production QC", "Fabric",
                new Process.DataParam("finish", "fabric")));

       processList.add(new Process(R.drawable.process, "Finish Fabric Production QC Print", "code",
            new Process.DataParam("code", "fabric")));
//        processList.add(new Process(R.drawable.process, "Dyeing Production", "code",
//                new Process.DataParam("code", "fabric")));

        processList.add(new Process(R.drawable.process, "Finish Fabric Production Result Entry", "result",
                new Process.DataParam("result", "fabric")));
        processList.add(new Process(R.drawable.process, "Finish Fabric Roll Issue Barcode Scan ", "result",
                new Process.DataParam("issue", "fabric")));
        processList.add(new Process(R.drawable.process, "Finish Fabric Roll Receive by Store", "result",
                new Process.DataParam("store", "fabric")));
        processList.add(new Process(R.drawable.process, "Knitting QC Result Entry", "result",
                new Process.DataParam("store", "knitting")));
       /* processList.add(new Process(R.drawable.process, "Cutting QC",
                new Process.DataParam("cutting_qc", "issue")));

        processList.add(new Process(R.drawable.process, "Print Issue",
                new Process.DataParam("print", "Knitting QC Result Entry")));

        processList.add(new Process(R.drawable.process, "Print Receive",
                new Process.DataParam("print", "receive")));

        processList.add(new Process(R.drawable.process, "Embroidery Issue",
                new Process.DataParam("embroidery", "issue")));

        processList.add(new Process(R.drawable.process, "Embroidery Receive",
                new Process.DataParam("embroidery", "receive")));

        processList.add(new Process(R.drawable.process, "Special Work Issue",
                new Process.DataParam("special_work", "issue")));

        processList.add(new Process(R.drawable.process, "Special Work Receive",
                new Process.DataParam("special_work", "receive")));

        *//*processList.add(new Process(R.drawable.process, "Wash Issue",
                new Process.DataParam("wash", "issue")));

        processList.add(new Process(R.drawable.process, "Wash Receive",
                new Process.DataParam("wash", "receive")));
*//*
        processList.add(new Process(R.drawable.process, "Sewing Input",
                new Process.DataParam("sewing", "issue")));

        processList.add(new Process(R.drawable.process, "Sewing Output",
                new Process.DataParam("sewing", "receive")));*/

        return Observable.fromArray(processList);
    }


}
