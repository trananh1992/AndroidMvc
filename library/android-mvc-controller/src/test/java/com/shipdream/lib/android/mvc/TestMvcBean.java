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

package com.shipdream.lib.android.mvc;

import org.junit.Assert;
import org.junit.Test;

public class TestMvcBean {
    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_bind_null_to_a_mvcBean() {
        MvcBean mvcBean = new MvcBean() {
            @Override
            public Class modelType() {
                return String.class;
            }
        };

        mvcBean.bindModel(null);
    }

    @Test
    public void should_rebind_state_after_restoring_mvcBean() {
        MvcBean<String> mvcBean = new MvcBean() {

            @Override
            public Class modelType() {
                return String.class;
            }
        };

        Assert.assertNull(mvcBean.getModel());

        mvcBean.restoreModel("A");

        Assert.assertEquals("A", mvcBean.getModel());
    }

    @Test
    public void should_call_on_restore_call_back_after_a_stateful_mvcBean_is_restored() {
        class MyMvcBean extends MvcBean<String> {
            private boolean called = false;

            @Override
            public Class modelType() {
                return String.class;
            }

            @Override
            public void onRestored() {
                super.onRestored();
                called = true;
            }
        };

        MyMvcBean mvcBean = new MyMvcBean();

        Assert.assertFalse(mvcBean.called);

        mvcBean.restoreModel("A");

        Assert.assertTrue(mvcBean.called);
    }

    @Test
    public void should_not_call_on_restore_call_back_after_a_non_stateful_mvcBean_is_restored() {
        class MyMvcBean extends MvcBean<String> {
            private boolean called = false;

            @Override
            public Class modelType() {
                return null;
            }

            @Override
            public void onRestored() {
                super.onRestored();
                called = true;
            }
        };

        MyMvcBean mvcBean = new MyMvcBean();

        Assert.assertFalse(mvcBean.called);

        mvcBean.restoreModel("A");

        Assert.assertFalse(mvcBean.called);
    }

    public void should_create_state_instance_on_construct_when_the_state_type_is_specified_for_a_mvcBean() {
        class MyMvcBean extends MvcBean<String> {
            @Override
            public Class modelType() {
                return String.class;
            }
        };
        MyMvcBean mvcBean = new MyMvcBean();

        Assert.assertNull(mvcBean.getModel());

        mvcBean.onConstruct();

        Assert.assertNotNull(mvcBean.getModel());
    }

    public void should_NOT_create_state_instance_on_construct_when_the_state_type_is_null_for_a_mvcBean() {
        class MyMvcBean extends MvcBean {
            @Override
            public Class modelType() {
                return null;
            }
        };
        MyMvcBean mvcBean = new MyMvcBean();

        Assert.assertNull(mvcBean.getModel());

        mvcBean.onConstruct();

        Assert.assertNull(mvcBean.getModel());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_out_when_creating_state_failed() {
        class BadClass {
            {int x = 1 / 0;}
        }

        class MyMvcBean extends MvcBean<BadClass> {
            @Override
            public Class<BadClass> modelType() {
                return BadClass.class;
            }
        };

        MyMvcBean mvcBean = new MyMvcBean();

        mvcBean.onConstruct();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_binding_null_to_stateful_mvcBean() {
        class MyMvcBean extends MvcBean<String> {
            @Override
            public Class<String> modelType() {
                return String.class;
            }
        };

        MyMvcBean mvcBean = new MyMvcBean();

        mvcBean.bindModel(null);
    }

    @Test
    public void should_be_able_to_successfully_bind_state_to_stateful_mvcBean() {
        class MyMvcBean extends MvcBean<String> {
            @Override
            public Class<String> modelType() {
                return String.class;
            }
        };

        MyMvcBean mvcBean = new MyMvcBean();

        Assert.assertNotEquals("B", mvcBean.getModel());

        mvcBean.bindModel("B");

        Assert.assertEquals("B", mvcBean.getModel());
    }

}
