#!/bin/bash

SCRIPT_PATH=$(dirname $(readlink -f $0 ))
source "$SCRIPT_PATH/common.inc.sh"

# ---------------------------------------------------------------------------------------------------------------------

api_call 'auth/user' | jq
