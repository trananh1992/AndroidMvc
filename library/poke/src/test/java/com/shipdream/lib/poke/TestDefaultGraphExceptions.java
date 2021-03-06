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

package com.shipdream.lib.poke;

import com.shipdream.lib.poke.exception.CircularDependenciesException;
import com.shipdream.lib.poke.exception.PokeException;
import com.shipdream.lib.poke.exception.ProvideException;
import com.shipdream.lib.poke.exception.ProviderConflictException;
import com.shipdream.lib.poke.exception.ProviderMissingException;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public class TestDefaultGraphExceptions extends BaseTestCases {
    interface Pet{
    }

    static class Cat implements Pet {
    }

    static class Dog implements Pet {
    }

    @Test
    public void suppress_constructor_miss_coverage_of_pokerHelper() {
        new PokeHelper();
    }

    @Test(expected = ProviderConflictException.class)
    public void shouldDetectConflictingClassRegistry() throws ProviderConflictException {
        SimpleGraph graph = new SimpleGraph();
        graph.register(Pet.class, Cat.class);
        graph.register(Pet.class, Dog.class);
    }

    @Test(expected = ProviderConflictException.class)
    public void shouldDetectConflictingNameRegistry() throws ProviderConflictException, ClassNotFoundException {
        SimpleGraph graph = new SimpleGraph();
        graph.register(Pet.class, Cat.class.getName());
        graph.register(Pet.class, Dog.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ProviderConflictException.class)
    public void shouldDetectConflictingProviderRegistry() throws ProviderConflictException, ProvideException, ClassNotFoundException {
        SimpleGraph graph = new SimpleGraph();
        Provider provider = new ProviderByClassType<>(Pet.class, Cat.class);
        Provider provider2 = new ProviderByClassName(Pet.class, Dog.class.getName());
        graph.register(provider);
        graph.register(provider2);
    }

    @Test(expected = ClassNotFoundException.class)
    public void shouldDetectBadClassException() throws ProviderConflictException, ClassNotFoundException {
        SimpleGraph graph = new SimpleGraph();
        graph.register(Pet.class, "BadClass");
    }

    @Test
    public void shouldBeGoodInjection()
            throws ProviderConflictException, CircularDependenciesException, ProviderMissingException, ProvideException {
        SimpleGraph graph = new SimpleGraph();
        graph.register(Pet.class, Dog.class);

        class Family {
            @MyInject
            private Pet pet;
        }

        Family family = new Family();
        graph.inject(family, MyInject.class);
    }

    static class Rabbit implements Pet {
        String name;
        public Rabbit(String name) {
            this.name = name;
        }
    }

    @Test(expected = ProvideException.class)
    public void shouldDetectProvideExceptionWithClassDoesHaveDefaultConstructor()
            throws ProviderConflictException, CircularDependenciesException, ProviderMissingException, ProvideException {
        SimpleGraph graph = new SimpleGraph();
        graph.register(Pet.class, Rabbit.class);

        class Family {
            @MyInject
            private Pet pet;
        }

        Family family = new Family();
        graph.inject(family, MyInject.class);
    }

    private static abstract class AbstractBean {
    }

    @Test
    public void should_handle_InstantiationException_when_create_class_instance_in_ProviderByClassName()
            throws ProviderConflictException, ClassNotFoundException, ProvideException, CircularDependenciesException, ProviderMissingException {
        SimpleGraph graph = new SimpleGraph();
        Provider provider = new ProviderByClassName(AbstractBean.class, AbstractBean.class.getName());
        graph.register(provider);

        class Consumer {
            @MyInject
            AbstractBean bean;
        }

        Consumer c = new Consumer();

        try {
            graph.inject(c, MyInject.class);
            fail("Should have caught InstantiationException but not");
        } catch (PokeException e) {
            Assert.assertTrue(e.getCause() instanceof InstantiationException);
        }
    }

    private static class BadInstantiatingBean {
        {
            int x = 1 / 0;
        }
    }

    @Test
    public void should_handle_InvocationTargetException_when_create_class_instance_in_ProviderByClassName()
            throws ProviderConflictException, ClassNotFoundException, ProvideException, CircularDependenciesException, ProviderMissingException {
        SimpleGraph graph = new SimpleGraph();
        Provider provider = new ProviderByClassName(BadInstantiatingBean.class, BadInstantiatingBean.class.getName());
        graph.register(provider);

        class Consumer {
            @MyInject
            BadInstantiatingBean bean;
        }

        Consumer c = new Consumer();

        try {
            graph.inject(c, MyInject.class);

            fail("Should have caught InvocationTargetException but not");
        } catch (PokeException e) {
            Assert.assertTrue(e.getCause() instanceof InvocationTargetException);
        }
    }

    @Test
    public void should_handle_NoSuchMethodException_when_create_class_instance_in_ProviderByClassName()
            throws ProviderConflictException, ClassNotFoundException, ProvideException, CircularDependenciesException, ProviderMissingException {
        class BadBean {
        }

        SimpleGraph graph = new SimpleGraph();
        Provider provider = new ProviderByClassName(BadBean.class, BadBean.class.getName());
        graph.register(provider);

        class Consumer {
            @MyInject
            BadBean bean;
        }

        Consumer c = new Consumer();

        try {
            graph.inject(c, MyInject.class);

            fail("Should have caught NoSuchMethodException but not");
        } catch (PokeException e) {
            Assert.assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

}
