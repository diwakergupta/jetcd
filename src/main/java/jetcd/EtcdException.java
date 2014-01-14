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

import com.fasterxml.jackson.annotation.JsonProperty;

/** An Etcd Exception. */
public final class EtcdException extends Exception {
  private final int errorCode;
  private final String cause;
  private final String message;
  private final long index;

  public EtcdException(@JsonProperty("errorCode") final int errorCode,
             @JsonProperty("cause") final String cause,
             @JsonProperty("message") final String message,
             @JsonProperty("index") final long index) {
    this.errorCode = errorCode;
    this.cause = cause;
    this.message = message;
    this.index = index;
  }

  public int getErrorCode() {
    return errorCode;
  }

  @Override
  public String toString() {
    return cause;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public long getIndex() {
    return index;
  }
}
