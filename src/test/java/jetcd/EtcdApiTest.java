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

import org.junit.Test;
import retrofit.RestAdapter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class EtcdApiTest {
    private static final EtcdApi etcd;
    static {
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("http://127.0.0.1:4001")
            .build();
        etcd = restAdapter.create(EtcdApi.class);
    }

    @Test
    public void testEtcd() throws Exception {
        final String value = "Hello World!";

        // Set a key
        Response response = etcd.setKey("message", value);
        assertThat(response.value).isEqualTo(value);

        // Verify value
        response = etcd.getKey("message");
        assertThat(response.value).isEqualTo(value);

        // Delete the key
        response = etcd.deleteKey("message");
        assertThat(response.prevValue).isEqualTo(value);

        // Create multiple keys
        etcd.setKey("foo/bar", "Bar");
        etcd.setKey("foo/baz", "Baz");

        // List directory
        List<Response> entries = etcd.list("foo");
        assertThat(entries).hasSize(2);

        // Test and Set
        etcd.setKey("message", value);
        try {
            etcd.testAndSet("message", "bad-old", "new");
            fail();
        } catch (Exception e) {
            // expected
        }
        response = etcd.testAndSet("message", value, "New Value");
        assertThat(response.prevValue).isEqualTo(value);
        assertThat(response.value).isEqualTo("New Value");
    }
}
