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

import java.util.Map;

public interface EtcdClient {
    /**
     * Retrieve the value of the given key, if set.
     *
     * @param key Key to look up
     * @return value Value for the given key
     * @throws EtcdException in case of an error (e.g. key doesn't exist)
     */
    String get(String key) throws EtcdException;

    /**
     * Set value for the given key.
     *
     * @param key Key to set value for
     * @param value New value for the key
     * @throws EtcdException in case of an error
     */
    void set(String key, String value) throws EtcdException;

    /**
     * Delete value for the given key.
     *
     * @param key Key to delete value for
     * @throws EtcdException in case of an error
     */
    void delete(String key) throws EtcdException;

    /**
     * List directory at given path.
     *
     * @param path Given path
     * @return Map of key,value pairs under the path
     * @throws EtcdException in case of an error
     */
    Map<String, String> list(String path) throws EtcdException;

    /**
     * Test the value at a given key and update to newValue if the oldValue
     * matches.
     *
     * @param key Key to test/set value at
     * @param oldValue Old value
     * @param newValue New value
     * @return The previous value
     * @throws EtcdException in case of an error
     */
    String testAndSet(String key, String oldValue, String newValue)
        throws EtcdException;
}
