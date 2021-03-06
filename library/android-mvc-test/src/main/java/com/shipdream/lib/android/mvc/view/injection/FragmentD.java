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

package com.shipdream.lib.android.mvc.view.injection;

import android.os.Bundle;
import android.view.View;

import com.shipdream.lib.android.mvc.view.MvcApp;
import com.shipdream.lib.android.mvc.view.help.LifeCycleMonitor;
import com.shipdream.lib.android.mvc.view.help.LifeCycleMonitorC;
import com.shipdream.lib.android.mvc.view.injection.controller.ControllerD;
import com.shipdream.lib.android.mvc.view.test.R;

import javax.inject.Inject;

public class FragmentD extends FragmentInjection {
    @Inject
    private ControllerD controllerD;

    private LifeCycleMonitorC lifeCycleMonitorC = MvcApp.lifeCycleMonitorFactory.provideLifeCycleMonitorC();

    @Override
    protected void setUpData() {
    }

    @Override
    protected LifeCycleMonitor getLifeCycleMonitor() {
        return lifeCycleMonitorC;
    }

    View root;

    @Override
    public void onViewReady(View view, Bundle savedInstanceState, Reason reason) {
        super.onViewReady(view, savedInstanceState, reason);
        textViewA.setText("");
        root = view.findViewById(R.id.fragment_injection_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    String[] contents = new String[] {
            "A", "B", "C"
    };

    private void update() {
        long i = controllerD.getAccountManager().getUserId();
        controllerD.setUserId(i + 1);
        controllerD.setStorage(contents[((int) (i)) % contents.length]);

        String msg = String.format("%d:%s", controllerD.getAccountManager().getUserId(),
                controllerD.getAccountManager().getContent());
        textViewA.setText(msg);
    }
}
