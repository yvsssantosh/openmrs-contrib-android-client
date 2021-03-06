/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.formentrypatientlist;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.formlist.FormListActivity;
import org.openmrs.mobile.models.retrofit.Patient;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.FontsUtil;
import org.openmrs.mobile.utilities.StringUtils;

import java.util.List;

public class FormEntryPatientListFragment extends Fragment implements  FormEntryPatientListContract.View {

    FormEntryPatientListContract.Presenter mPresenter;
    private RecyclerView mPatientRecyclerView;
    private TextView mEmptyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_entry_patient_list, container, false);

        mPatientRecyclerView = (RecyclerView) root.findViewById(R.id.patientRecyclerView);
        mPatientRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mPatientRecyclerView.setLayoutManager(linearLayoutManager);

        mEmptyList = (TextView) root.findViewById(R.id.emptyPatientList);
        mEmptyList.setText(getString(R.string.search_patient_no_results));

        // Font config
        FontsUtil.setFont((ViewGroup) this.getActivity().findViewById(android.R.id.content));
        return root;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull FormEntryPatientListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void updateAdapter(List<Patient> patientList) {
        FormEntryPatientListAdapter adapter = new FormEntryPatientListAdapter(this,patientList);
        adapter.notifyDataSetChanged();
        mPatientRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateListVisibility(boolean isVisible, int emptyListTextStringId, @Nullable String replacementWord) {
        if (isVisible) {
            mPatientRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
        }
        else {
            mPatientRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isBlank(replacementWord)) {
            mEmptyList.setText(getString(emptyListTextStringId));
        }
        else {
            mEmptyList.setText(getString(emptyListTextStringId, replacementWord));
        }
    }

    @Override
    public void startEncounterForPatient(Long selectedPatientID) {
        Intent intent = new Intent(this.getActivity(), FormListActivity.class);
        intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, selectedPatientID);
        startActivity(intent);
    }

    public static FormEntryPatientListFragment newInstance() {
        return new FormEntryPatientListFragment();
    }

}
