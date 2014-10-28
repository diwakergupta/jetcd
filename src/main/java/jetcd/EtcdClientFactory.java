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

import com.google.common.base.Optional;

import retrofit.client.Client;

/** Factory for Etcd clients. */
public final class EtcdClientFactory {
  private EtcdClientFactory() {
    // Factory
  }

  public static EtcdClient newInstance() {
    return newInstance("http://127.0.0.1:4001");
  }

  public static EtcdClient newInstance(final String server) {
    return new EtcdClientImpl(Optional.<Client>absent(), server);
  }

  public static EtcdClient newInstance(final Client client,
      final String server) {
    return new EtcdClientImpl(Optional.of(client), server);
  }
}
