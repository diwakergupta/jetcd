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

import java.util.List;

public final class EtcdResponse {
    private final String action;
    private final Node node;

    EtcdResponse(@JsonProperty("action") String action,
                 @JsonProperty("node") Node node) {
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
        private final String prevValue;
        private final long createdIndex;
        private final long modifiedIndex;
        private final long ttl;
        private final List<Node> nodes;

        Node(@JsonProperty("key") String key,
             @JsonProperty("value") String value,
             @JsonProperty("prevValue") String prevValue,
             @JsonProperty("createdIndex") long createdIndex,
             @JsonProperty("modifiedIndex") long modifiedIndex,
             @JsonProperty("ttl") long ttl,
             @JsonProperty("nodes") List<Node> nodes) {
            this.key = key;
            this.value = value;
            this.prevValue = prevValue;
            this.createdIndex = createdIndex;
            this.modifiedIndex = modifiedIndex;
            this.ttl = ttl;
            this.nodes = nodes;
        }

        public String getKey() {
            return key;
        }

        public String getPrevValue() {
            return prevValue;
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
