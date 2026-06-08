/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.ui.process;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.model.Process;
import com.logicsoftbd.lsl.di.component.ActivityComponent;
import com.logicsoftbd.lsl.ui.base.BaseFragment;
import com.logicsoftbd.lsl.ui.dyeingProduction.DyeingProductionActivity;
import com.logicsoftbd.lsl.ui.process.greyroll.FinishFabricQrCodeActivity;
import com.logicsoftbd.lsl.ui.process.scanprocess.ScannerActivity;
import com.logicsoftbd.lsl.utils.GridSpacingItemDecoration;
import com.logicsoftbd.lsl.utils.ScreenUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by janisharali on 25/05/17.
 */

public class ProcessFragment extends BaseFragment implements
        ProcessMvpView, ProcessAdapter.Callback {

    private static final String TAG = "ProcessFragment";

    @Inject
    ProcessMvpPresenter<ProcessMvpView, ProcessMvpInteractor> mPresenter;

    @Inject
    ProcessAdapter mAdapter;

    @Inject
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.repo_recycler_view)
    RecyclerView mRecyclerView;

    public static ProcessFragment newInstance() {
        Bundle args = new Bundle();
        ProcessFragment fragment = new ProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            mAdapter.setCallback(this);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {
       /* mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);*/

        RecyclerView.LayoutManager  gridLayoutManager = new GridLayoutManager(getContext(), ScreenUtils.calculateNoOfColumns(getContext()));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ScreenUtils.dpToPx(getContext(), 5), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);


        mPresenter.getMenu();
    }

    @Override
    public void onRepoEmptyViewRetryClick() {

    }

    @Override
    public void onItemClick(int position, Process process) {

        if(process.getSubTitle().equals("code")){
            if(process.getTitle().equals("Dyeing Production")){
                startActivity( new Intent(getContext(), DyeingProductionActivity.class));
            }else if(process.getTitle().equals("Slitting Squeezing")){
                startActivity( ScannerActivity.getStartIntent(getContext(), new Process(R.drawable.process, "",
                        new Process.DataParam("result", "slitting"))));
            } else if(process.getTitle().equals("Stentering")){
                startActivity( ScannerActivity.getStartIntent(getContext(), new Process(R.drawable.process, "",
                        new Process.DataParam("result", "stentering"))));
            } else if(process.getTitle().equals("Compacting")){
                startActivity( ScannerActivity.getStartIntent(getContext(), new Process(R.drawable.process, "",
                        new Process.DataParam("result", "compacting"))));
            } else{
                startActivity( new Intent(getContext(), FinishFabricQrCodeActivity.class));
            }

        }
        else{
            startActivity( ScannerActivity.getStartIntent(getContext(), process));
        }

    }

    @Override
    public void updateRepo(List<Process> repoList) {
        mAdapter.addItems(repoList);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
