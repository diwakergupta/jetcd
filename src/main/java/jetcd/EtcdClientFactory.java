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

public class EtcdClientFactory {

    public enum APILevel {
        V1(1), V2(2);

        private final int version;

        private APILevel(int version) {
            this.version = version;
        }
    }

    private EtcdClientFactory() {
        // Factory
    }

    public static EtcdClient newInstance() {
        return newInstance("http://127.0.0.1:4001");
    }

    public static EtcdClient newInstance(final String server) {
        return new EtcdClientImpl(server);
    }

    public static EtcdClient newInstance(final String server, APILevel api) {
        if (api == APILevel.V1) {
            return new EtcdClientImpl(server);
        } else {
            return new EtcdClientImplV2(server);
        }
    }
}
