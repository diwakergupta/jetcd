jetcd: A Java client for etcd
=============================

h2. Download

    <dependency>
      <groupId>net.floatingsun</groupId>
      <artifactId>jetcd</artifactId>
      <version>0.1.0</version>
    </dependency>

h2. Usage

    // Connects to "http://127.0.0.1:4001" by default
    EtcdClient client = EtcdClientFactory.newInstance();
    // Use EtcdClientFactory.newInstance(serverUrl) to override

    client.setKey("hello", "world");
    client.getKey("hello") // returns "world"
    client.deleteKey("hello")

TODO

- multiple servers with redirect
- retries and better failure handling
- better unification of 'getKey' and 'list'
- support for watch
- 
