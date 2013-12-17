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

import jetcd.trusted.Credentials;
import jetcd.trusted.TrustedApacheClient;

public class EtcdClientFactory {

    private EtcdClientFactory() {
        // Factory
    }

    public static EtcdClient newInstance() {
        return newInstance("http://127.0.0.1:4001");
    }

    public static EtcdClient newInstance(final String server) {
        return new EtcdClientImpl(server);
    }

    public static EtcdClient newTrustedInstance(final String server,
                                                final byte[] caCert,
                                                final byte[] clientKey,
                                                final byte[] clientCert) {
        Credentials credentials =
                new Credentials(caCert, clientKey, clientCert);
        TrustedApacheClient client = new TrustedApacheClient(credentials);
        return new EtcdClientImpl(server, client);
    }

    public static EtcdClient newTrustedInstance(final String server,
                                                final Credentials credentials) {
        TrustedApacheClient client = new TrustedApacheClient(credentials);
        return new EtcdClientImpl(server, client);
    }

}
