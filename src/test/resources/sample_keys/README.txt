Password for all keys is 1234

My etcd config is:

addr = "127.0.0.1:4001"
bind_addr = ""

ca_file = "/etc/etcd/cert/ca.crt"
cert_file = "/etc/etcd/cert/server.crt"
key_file = "/etc/etcd/private/server.key.insecure"

cors_origins = []
cpu_profile_file = ""
data_dir = "/var/lib/etcd/"
peers = []
peers_file = ""
max_cluster_size = 9
max_result_buffer = 1024
max_retry_attempts = 3
name = "default-name"
snapshot = false
verbose = false
very_verbose = false
web_url = ""

[peer]
addr = "127.0.0.1:7001"
bind_addr = ""
ca_file = ""
cert_file = ""
key_file = ""
