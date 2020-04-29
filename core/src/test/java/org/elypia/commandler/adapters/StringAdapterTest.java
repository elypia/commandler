/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.commandler.adapters;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class StringAdapterTest {

    private static StringAdapter adapter;

    @BeforeEach
    public void beforeEach() {
        adapter = new StringAdapter();
    }

    @Test
    public void assertStrings() {
        assertAll("Check the same text provided.",
            () -> assertEquals("10000", adapter.adapt("10000")),
            () -> assertEquals("Hello, world!", adapter.adapt("Hello, world!"))
        );
    }
}