#!/bin/bash

set -e

# Echoes extra logging info
log() {
  if [ "$VERBOSE" = true ]
    then
      echo "--${1}"
  fi
}

# Checks if an env value is present and not empty
checkEnv() {
  echo "Checking"
  env_value=$(printf '%s\n' "${!1}")
  if [ -z ${env_value} ]; then
    echo "$1 is undefined, exiting..."
    exit 1
  else
    log "Found value for $1"
  fi
}

checkEnv HOOT_ENCRYPT_KEY

echo "Decrypting files"
openssl aes-256-cbc -a -d -md sha256 -in enc/KeyStore.kt.aes -out buildSrc/src/main/java/hoot/KeyStore.kt -k $HOOT_ENCRYPT_KEY
openssl aes-256-cbc -a -d -md sha256 -in enc/release.keystore.aes -out signing/release.keystore -k $HOOT_ENCRYPT_KEY
openssl aes-256-cbc -a -d -md sha256 -in enc/play.json.aes -out signing/play.json -k $HOOT_ENCRYPT_KEY
openssl aes-256-cbc -a -d -md sha256 -in enc/google-services.json.aes -out app/google-services.json -k $HOOT_ENCRYPT_KEY
log "Files decrypted"

echo "Finishing up"