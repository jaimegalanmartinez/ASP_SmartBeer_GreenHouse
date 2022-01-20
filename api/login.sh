#!/bin/bash

SCRIPT_PATH=$(dirname $(readlink -f $0 ))
source "$SCRIPT_PATH/common.inc.sh"

# ---------------------------------------------------------------------------------------------------------------------

rm -f "$TOKEN_PATH"

echo -n "Password: "
read -s PASSWORD

TOKEN=$(api_call 'auth/login' "{\"username\":\"$1\", \"password\":\"$PASSWORD\"}" | jq -r -e '.token')

! test "$?" -eq 0 && {
    echo "Failed to get token"
    exit 1
}

echo ""
echo "$TOKEN" > "$TOKEN_PATH"
echo "Successfully logged in!"
