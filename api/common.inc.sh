#!/bin/bash

SCRIPT_PATH=$(dirname $(readlink -f $0 ))
API_BASE='https://srv-iot.diatel.upm.es/api'
TOKEN_PATH="$SCRIPT_PATH/.token.secret"

api_call()
{
    local endpoint="$1"
    local payload="$2"
    local options=('--header' 'Content-Type: application/json' '--header' 'Accept: application/json')

    test -n "$payload" && {
        options+=('-d' "$payload")
        options+=('-X' 'POST')
    }

    test -f "$TOKEN_PATH" && {
        local token=$(cat "$TOKEN_PATH")
        options+=('--header' "X-Authorization: Bearer $token")
    }

    curl -s "${options[@]}" "$API_BASE/$endpoint"
}
