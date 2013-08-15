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

import retrofit.RetrofitError;

public class EtcdException extends Exception {
    private final EtcdError etcdError;

    private static final class EtcdError {
        int errorCode;
        String message;
        String cause = null;

        EtcdError() { }
    }

    public EtcdException(RetrofitError error) {
        super(error.getResponse().getReason());
        etcdError = (EtcdError) error.getBodyAs(EtcdError.class);
    }

    public int getErrorCode() {
        return etcdError.errorCode;
    }
}
