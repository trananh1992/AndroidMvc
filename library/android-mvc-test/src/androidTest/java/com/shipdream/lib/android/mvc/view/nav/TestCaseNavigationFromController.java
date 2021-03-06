/*
 * Copyright 2015 Kejun Xia
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

package com.shipdream.lib.android.mvc.view.nav;

import com.shipdream.lib.android.mvc.manager.NavigationManager;
import com.shipdream.lib.android.mvc.manager.internal.Preparer;
import com.shipdream.lib.android.mvc.view.AndroidMvc;
import com.shipdream.lib.android.mvc.view.BaseTestCase;
import com.shipdream.lib.poke.Component;
import com.shipdream.lib.poke.Provides;
import com.shipdream.lib.poke.ScopeCache;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TestCaseNavigationFromController extends BaseTestCase <MvcTestActivityNavigation> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private NavigationManager navigationManager;

    private Comp comp;
    private DisposeCheckerE disposeCheckerEMock;
    private DisposeCheckerF disposeCheckerFMock;
    private DisposeCheckerG disposeCheckerGMock;

    public TestCaseNavigationFromController() {
        super(MvcTestActivityNavigation.class);
    }

    @Override
    protected void waitTest() throws InterruptedException {
        waitTest(200);
    }

    public static class Comp extends Component{
        TestCaseNavigationFromController testCaseNavigation;

        Comp(ScopeCache scopeCache) {
            super(scopeCache);
        }

        @Singleton
        @Provides
        public DisposeCheckerE providesDisposeCheckerE() {
            return testCaseNavigation.disposeCheckerEMock;
        }

        @Singleton
        @Provides
        public DisposeCheckerF providesDisposeCheckerF() {
            return testCaseNavigation.disposeCheckerFMock;
        }

        @Singleton
        @Provides
        public DisposeCheckerG providesDisposeCheckerG() {
            return testCaseNavigation.disposeCheckerGMock;
        }
    }

    @Override
    protected void injectDependencies(ScopeCache mvcSingletonCache) {
        super.injectDependencies(mvcSingletonCache);

        disposeCheckerEMock = mock(DisposeCheckerE.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                logger.debug("Dispose checker E");
                return null;
            }
        }).when(disposeCheckerEMock).onDisposed();
        disposeCheckerFMock = mock(DisposeCheckerF.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                logger.debug("Dispose checker F");
                return null;
            }
        }).when(disposeCheckerFMock).onDisposed();
        disposeCheckerGMock = mock(DisposeCheckerG.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                logger.debug("Dispose checker G");
                return null;
            }
        }).when(disposeCheckerGMock).onDisposed();
        comp = new Comp(mvcSingletonCache);
        comp.testCaseNavigation = this;
        AndroidMvc.graph().register(comp);
    }

    @Override
    protected void cleanDependencies() {
        super.cleanDependencies();
        AndroidMvc.graph().unregister(comp);
    }

    @Test
    public void test_should_release_injected_object_by_pure_navigation_controller_navigation() throws Throwable {
        onView(withText(NavFragmentA.class.getSimpleName())).check(matches(isDisplayed()));

        final String val = "Value = " + new Random().nextInt();

        navigationManager.navigate(this).with(ControllerE.class, new Preparer<ControllerE>() {
            @Override
            public void prepare(ControllerE instance) {
                instance.setValue(val);
            }
        }).to(MvcTestActivityNavigation.Loc.E);

        //The value set to controller e in Injector.getGraph().use should be retained during the
        //navigation
        onView(withText(val)).check(matches(isDisplayed()));

        //The controller should not be disposed yet
        verify(disposeCheckerEMock, times(0)).onDisposed();

        navigationManager.navigate(this).back();

        //Controller should be disposed after navigated away from fragment E
        waitTest();
        verify(disposeCheckerEMock, times(1)).onDisposed();
    }

    @Test
    public void test_should_release_injected_object_by_chained_navigation_controller_navigation() throws Throwable {
        onView(withText(NavFragmentA.class.getSimpleName())).check(matches(isDisplayed()));

        final String valE = "ValueE = " + new Random().nextInt();
        final String valF = "ValueF = " + new Random().nextInt();
        final String valG = "ValueG = " + new Random().nextInt();

        resetDisposeCheckers();
        navigationManager.navigate(this).with(ControllerE.class, new Preparer<ControllerE>() {
            @Override
            public void prepare(ControllerE instance) {
                instance.setValue(valE);
            }
        }).to(MvcTestActivityNavigation.Loc.E);
        waitTest();
        onView(withText(valE)).check(matches(isDisplayed()));
        verify(disposeCheckerEMock, times(0)).onDisposed();
        verify(disposeCheckerFMock, times(0)).onDisposed();
        verify(disposeCheckerGMock, times(0)).onDisposed();

        resetDisposeCheckers();
        navigationManager.navigate(this).with(ControllerF.class, new Preparer<ControllerF>() {
            @Override
            public void prepare(ControllerF instance) {
                instance.setValue(valF);
            }
        }).to(MvcTestActivityNavigation.Loc.F);
        waitTest();
        onView(withText(valF)).check(matches(isDisplayed()));
        verify(disposeCheckerEMock, times(0)).onDisposed();
        verify(disposeCheckerFMock, times(0)).onDisposed();
        verify(disposeCheckerGMock, times(0)).onDisposed();

        resetDisposeCheckers();
        navigationManager.navigate(this).with(ControllerG.class, new Preparer<ControllerG>() {
            @Override
            public void prepare(ControllerG instance) {
                instance.setValue(valG);
            }
        }).to(MvcTestActivityNavigation.Loc.G);
        waitTest();
        onView(withText(valG)).check(matches(isDisplayed()));
        verify(disposeCheckerEMock, times(0)).onDisposed();
        verify(disposeCheckerFMock, times(0)).onDisposed();
        verify(disposeCheckerGMock, times(0)).onDisposed();

        resetDisposeCheckers();
        //The value set to controller e in Injector.getGraph().use should be retained during the
        //navigation
        navigationManager.navigate(this).back();
        waitTest();
        onView(withText(valF)).check(matches(isDisplayed()));
        verify(disposeCheckerEMock, times(0)).onDisposed();
        verify(disposeCheckerFMock, times(0)).onDisposed();
        verify(disposeCheckerGMock, times(0)).onDisposed();

        resetDisposeCheckers();
        navigationManager.navigate(this).back();
        waitTest();
        onView(withText(valE)).check(matches(isDisplayed()));
        verify(disposeCheckerEMock, times(0)).onDisposed();
        verify(disposeCheckerFMock, times(1)).onDisposed();
        //__MvcGraphHelper retaining all cache is dangerous. Try to only retain relevant injected instances.
        verify(disposeCheckerGMock, times(0)).onDisposed();

        resetDisposeCheckers();
        navigationManager.navigate(this).back();
        waitTest();
        verify(disposeCheckerEMock, times(1)).onDisposed();
        verify(disposeCheckerFMock, times(0)).onDisposed();
        verify(disposeCheckerGMock, times(1)).onDisposed();
    }

    private void resetDisposeCheckers() {
        reset(disposeCheckerEMock);
        reset(disposeCheckerFMock);
        reset(disposeCheckerGMock);
    }

}
