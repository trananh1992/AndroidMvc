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

package com.shipdream.lib.android.mvc.inject.testNameMapping.controller.internal;

import com.shipdream.lib.android.mvc.controller.internal.BaseControllerImpl;
import com.shipdream.lib.android.mvc.inject.testNameMapping.controller.PrintController;
import com.shipdream.lib.android.mvc.inject.testNameMapping.controller.PrintModel;

public class PrintControllerImpl extends BaseControllerImpl<PrintModel> implements PrintController {
    @Override
    public String print() {
        return getModel().getContent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class modelType() {
        return PrintModel.class;
    }
}
