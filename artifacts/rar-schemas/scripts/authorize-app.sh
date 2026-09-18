#!/usr/bin/env bash
#
# Authorize a WSO2 IS application to use the RAR authorization details types
# registered by register.sh, via the Application Management REST API —
# POST /api/server/v1/applications/{applicationId}/authorized-apis.
#
# Usage:
#   APP_ID=<application-id> ./authorize-app.sh print       # dry run: print the request bodies
#   APP_ID=<application-id> ./authorize-app.sh authorize   # POST: authorize the app for both API resources
#
# Environment (defaults shown):
#   IS_HOST=https://localhost:9443     target IS base URL
#   IS_AUTH=admin:admin                Basic-auth credentials (user:password)
#   APP_ID=                            application id to authorize (required)
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TYPES_JSON="$SCRIPT_DIR/types.json"
IS_HOST="${IS_HOST:-https://localhost:9443}"
IS_AUTH="${IS_AUTH:-admin:admin}"

command -v jq >/dev/null 2>&1 || { echo "ERROR: jq is required but not installed." >&2; exit 1; }
command -v curl >/dev/null 2>&1 || { echo "ERROR: curl is required but not installed." >&2; exit 1; }

require_app_id() {
  if [ -z "${APP_ID:-}" ]; then
    echo "ERROR: APP_ID is required (the application id to authorize)." >&2
    echo "Usage: APP_ID=<application-id> $0 $1" >&2
    exit 1
  fi
}

# Look up an API resource's id by its identifier (e.g. account_information_api).
get_resource_id() {
  local identifier="$1"
  local response id
  response=$(curl -sk -u "$IS_AUTH" \
    "$IS_HOST/api/server/v1/api-resources?filter=identifier+eq+$identifier")
  id=$(echo "$response" | jq -r '.apiResources[0].id // empty')
  if [ -z "$id" ]; then
    echo "ERROR: could not find API resource with identifier '$identifier'. Has register.sh been run?" >&2
    echo "$response" >&2
    exit 1
  fi
  echo "$id"
}

# Build the authorized-apis request body for a resource key (accounts|payments).
build_body() {
  local rkey="$1"
  local identifier resource_id types
  identifier=$(jq -r ".resources.\"$rkey\".identifier" "$TYPES_JSON")
  resource_id=$(get_resource_id "$identifier")
  types=$(jq -c ".resources.\"$rkey\".types" "$TYPES_JSON")
  jq -n --arg id "$resource_id" --argjson types "$types" \
     '{id:$id, policyIdentifier:"RBAC", authorizationDetailsTypes:$types}'
}

authorize_resource() {
  local rkey="$1" body http out
  body=$(build_body "$rkey")
  echo ">> POST $IS_HOST/api/server/v1/applications/$APP_ID/authorized-apis  (resource: $rkey)" >&2
  out=$(mktemp)
  http=$(curl -sk -o "$out" -w '%{http_code}' -X POST \
    "$IS_HOST/api/server/v1/applications/$APP_ID/authorized-apis" \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -u "$IS_AUTH" \
    --data "$body")
  echo "   HTTP $http" >&2
  jq . "$out" 2>/dev/null || cat "$out"
  rm -f "$out"
  case "$http" in
    2*) return 0 ;;
    *)  echo "ERROR: authorization failed for resource '$rkey' (HTTP $http)." >&2; return 1 ;;
  esac
}

case "${1:-}" in
  print)
    echo "// ---- Account Information API ----"; build_body accounts
    echo "// ---- Payments API ----"; build_body payments
    ;;
  authorize)
    require_app_id authorize
    authorize_resource accounts
    authorize_resource payments
    ;;
  *)
    grep '^#' "$0" | sed 's/^# \{0,1\}//'
    exit 2
    ;;
esac
