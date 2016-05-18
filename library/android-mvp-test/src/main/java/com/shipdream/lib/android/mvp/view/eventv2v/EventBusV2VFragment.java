/*
 * Copyright 2016 Kejun Xia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shipdream.lib.android.mvp.view.eventv2v;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shipdream.lib.android.mvp.MvpFragment;
import com.shipdream.lib.android.mvp.view.eventv2v.controller.V2VTestPresenter;
import com.shipdream.lib.android.mvp.view.test.R;

import javax.inject.Inject;

public class EventBusV2VFragment extends MvpFragment implements V2VTestPresenter.View{
    private TextView textView;
    private View buttonDialog;
    private View buttonService;

    @Inject
    V2VTestPresenter presenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mvc_v2v;
    }

    @Override
    public void onViewReady(View view, Bundle savedInstanceState, Reason reason) {
        super.onViewReady(view, savedInstanceState, reason);

        presenter.view = this;

        textView = (TextView) view.findViewById(R.id.fragment_mvc_v2v_text);

        buttonDialog = view.findViewById(R.id.fragment_mvc_v2v_btnDialog);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EventBusV2VDialogFragment cityDialog = new EventBusV2VDialogFragment();
                cityDialog.show(getFragmentManager(), "EventBusV2VDialogFragment");
            }
        });

        buttonService = view.findViewById(R.id.fragment_mvc_v2v_btnService);
        buttonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventBusV2VService.class);
                getActivity().startService(intent);
            }
        });
    }

    private void onEvent(Events.OnFragmentTextChanged event) {
        textView.setText(event.getText());
    }

    @Override
    public void updateDialogButton(String text) {
        postEvent2V(new Events.OnDialogButtonChanged(this, text));
    }
}