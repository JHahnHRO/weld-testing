/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.junit5.initiator.discovery;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.initiator.bean.Foo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests a case where WeldInitiator is an Object typed field
 *
 * @author Björn Kautler
 */
@EnableWeld
public class ObjectWeldInitiatorTest {

    @WeldSetup
    public Object weld = WeldInitiator
        .of(new Weld()
            .addBeanClass(Foo.class));

    @Test
    public void testObjectWeldInit() {
        final Foo foo = ((WeldInitiator) weld).select(Foo.class).get();
        assertNotNull(foo);
    }
}
