/*
 * Copyright 2013 Diwaker Gupta
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

package jetcd;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class EtcdClientV2Test {
    private final EtcdClient client = EtcdClientFactory.newInstance();
    private boolean localEtcdAvailable = true;

    @Before
    public void setUp() throws EtcdException {
        // Setup some keys
        try {
            client.set("hello", "world");
        } catch (Exception e) {
            localEtcdAvailable = false;
        }
        assumeTrue(localEtcdAvailable);
    }

    @Test
    public void testGet() throws EtcdException {
        client.get("hello");
        try {
            client.get("non-existent");
            fail();
        } catch (EtcdException e) {
            assertThat(e.getErrorCode()).isEqualTo(100);
        }
    }

    @Test
    public void testSet() throws EtcdException {
        client.set("newKey", "newValue");
        String g = client.get("newKey");
        assertThat(client.get("newKey")).isEqualTo("newValue");

        // Set a pre-existing key
        client.set("hello", "newValue");
        assertThat(client.get("hello")).isEqualTo("newValue");
    }

    @Test
    public void testSetWithTtl() throws Exception {
        client.set("newKey", "newValue", 1);
        assertThat(client.get("newKey")).isEqualTo("newValue");

        // Wait for key to expire
        Thread.sleep(1500);
        try {
            client.get("newKey");
            fail();
        } catch (EtcdException e) {
            assertThat(e.getErrorCode()).isEqualTo(100);
        }
    }

    @Test
    public void testDelete() throws EtcdException {
        client.delete("hello");
        try {
            client.get("hello");
            fail();
        } catch (EtcdException e) {
            assertThat(e.getErrorCode()).isEqualTo(100);
        }

        try {
            client.delete("non-existent");
            fail();
        } catch (EtcdException e) {
            assertThat(e.getErrorCode()).isEqualTo(100);
        }
    }

    @Test
    public void testList() throws EtcdException {
        client.set("b/bar", "baz");
        client.set("b/foo", "baz");
        assertThat(client.list("b")).hasSize(2)
                .containsEntry("/b/bar", "baz").containsEntry("/b/foo", "baz");
    }

    @Test
    public void testGetAndSet() throws EtcdException {
        String oldValue = client.testAndSet("hello", "world", "new value");
        assertThat(oldValue).isEqualTo("world");
        assertThat(client.get("hello")).isEqualTo("new value");

        try {
            client.testAndSet("hello", "bad old", "world");
            fail();
        } catch (EtcdException e) {
            assertThat(e.getErrorCode()).isEqualTo(101);
        }
    }
}
