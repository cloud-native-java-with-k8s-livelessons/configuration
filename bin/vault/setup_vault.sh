#!/usr/bin/env bash
source vault_env.sh 
vault kv put secret/bootiful message-from-vault-server="Hello Spring Cloud Vault"
