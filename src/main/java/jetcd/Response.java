package jetcd;

import com.google.gson.Gson;

import java.util.Objects;

public class Response {
    private static final Gson gson = new Gson();

    String action;
    String key;
    Long index;
    Boolean dir = null;
    String prevValue = null;
    String value = null;
    Boolean newKey = null;
    Long ttl = null;

    // 0-arg constructor for gson
    public Response() { }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Response)) {
            return false;
        }
        Response o = (Response) other;
        return Objects.equals(action, o.action) &&
            Objects.equals(key, o.key) &&
            Objects.equals(index, o.index) &&
            Objects.equals(dir, o.dir) &&
            Objects.equals(prevValue, o.prevValue) &&
            Objects.equals(value, o.value) &&
            Objects.equals(newKey, o.newKey) &&
            Objects.equals(ttl, o.ttl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, key, index, dir, prevValue, value, newKey,
            ttl);
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
