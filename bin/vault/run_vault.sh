#!/usr/bin/env bash

source vault_env.sh 
vault server --dev --dev-root-token-id="$VAULT_TOKEN"
