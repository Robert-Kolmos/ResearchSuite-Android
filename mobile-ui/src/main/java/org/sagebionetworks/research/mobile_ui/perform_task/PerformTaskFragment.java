/*
 * BSD 3-Clause License
 *
 * Copyright 2018  Sage Bionetworks. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2.  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3.  Neither the name of the copyright holder(s) nor the names of any contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission. No license is granted to the trademarks of
 * the copyright holders even if such marks are included in this software.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sagebionetworks.research.mobile_ui.perform_task;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sagebionetworks.research.domain.mobile_ui.R;
import org.sagebionetworks.research.domain.mobile_ui.R2;
import org.sagebionetworks.research.domain.ui.show_step.view.GenericStep;
import org.sagebionetworks.research.domain.ui.widget.StepSwitcher;
import org.sagebionetworks.research.mobile_ui.mapper.StepMapper;
import org.sagebionetworks.research.mobile_ui.show_step.StepPresenter;
import org.sagebionetworks.research.mobile_ui.show_step.StepPresenterFactory;
import org.sagebionetworks.research.presentation.model.StepView;
import org.sagebionetworks.research.presentation.model.TaskView;
import org.sagebionetworks.research.presentation.perform_task.PerformTaskViewModel;
import org.sagebionetworks.research.presentation.perform_task.PerformTaskViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkState;

/**
 * A placeholder fragment containing a simple view.
 */
public class PerformTaskFragment extends Fragment {
    private static final String ARGUMENT_TASK_VIEW_MODEL = "TASK_VIEW";

    @Inject
    StepMapper stepMapper;

    @Inject
    StepPresenterFactory stepPresenterFactory;

    @BindView(R2.id.rs2_step_container)
    StepSwitcher stepSwitcher;

    @Inject
    PerformTaskViewModelFactory taskViewModelFactory;

    private PerformTaskViewModel performTaskViewModel;

    private StepPresenter stepPresenter;

    private TaskView taskView;

    private Unbinder unbinder;

    public static PerformTaskFragment newInstance(@NonNull TaskView taskViewModel) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_TASK_VIEW_MODEL, taskViewModel);

        PerformTaskFragment fragment = new PerformTaskFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putParcelable(ARGUMENT_TASK_VIEW_MODEL, taskView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                taskView = getArguments().getParcelable(ARGUMENT_TASK_VIEW_MODEL);
            }
        } else {
            taskView = savedInstanceState.getParcelable(ARGUMENT_TASK_VIEW_MODEL);
        }
        checkState(taskView != null, "no taskView found");

        performTaskViewModel = ViewModelProviders.of(this, taskViewModelFactory.create(taskView))
                .get(PerformTaskViewModel.class);

        performTaskViewModel.getStep().observe(this, this::showStep);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rs2_fragment_task, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @VisibleForTesting
    void showStep(StepView stepView) {
        GenericStep step = stepMapper.create(stepView);
//        StepPresenter previousStepPresenter = stepPresenter;
//        stepPresenter = stepPresenterFactory.create(null, performTaskViewModel);
//        step.setPresenter(stepPresenter);
//        stepSwitcher.show(step, stepView.navDirection);
//        previousStepPresenter.finish();
    }
}
