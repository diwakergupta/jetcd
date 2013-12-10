package jetcd;

import com.google.gson.Gson;

public class ResponseV2 {
    private static final Gson gson = new Gson();

    String action;
    Node node;

    public static class Node {
        Boolean dir = null;
        String key = null;
        String value = null;
        String prevValue = null;
        Long createdIndex;
        Long modifiedIndex;
        Node[] nodes = null;
    }

    // 0-arg constructor for gson
    public ResponseV2() { }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
