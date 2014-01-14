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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** An Etcd Response. */
public final class EtcdResponse {
  private final String action;
  private final Node node;

  EtcdResponse(@JsonProperty("action") final String action,
               @JsonProperty("node") final Node node) {
    this.action = action;
    this.node = node;
  }

  public String getAction() {
    return action;
  }

  public Node getNode() {
    return node;
  }

  static final class Node {
    private final String key;
    private final String value;
    private final long createdIndex;
    private final long modifiedIndex;
    private final long ttl;
    private final List<Node> nodes;

    Node(@JsonProperty("key") final String key,
       @JsonProperty("value") final String value,
       @JsonProperty("createdIndex") final long createdIndex,
       @JsonProperty("modifiedIndex") final long modifiedIndex,
       @JsonProperty("ttl") final long ttl,
       @JsonProperty("nodes") final List<Node> nodes) {
      this.key = key;
      this.value = value;
      this.createdIndex = createdIndex;
      this.modifiedIndex = modifiedIndex;
      this.ttl = ttl;
      this.nodes = nodes;
    }

    public String getKey() {
      return key;
    }

    public long getCreatedIndex() {
      return createdIndex;
    }

    public long getModifiedIndex() {
      return modifiedIndex;
    }

    public long getTtl() {
      return ttl;
    }

    public String getValue() {
      return value;
    }

    public List<Node> getNodes() {
      return nodes;
    }
  }
}
