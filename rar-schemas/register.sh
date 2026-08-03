#!/usr/bin/env bash
#
# Register WSO2 IS Rich Authorization Requests (RAR) authorization details types
# via the API Resource Management REST API (POST /api/server/v1/api-resources).
#
# Reads types.json (resource groupings + per-type metadata) and schemas/*.schema.json,
# assembles the api-resources request body with jq, and POSTs it.
#
# Usage:
#   ./register.sh print        # dry-run: print the 2 request bodies (Accounts + Payments)
#   ./register.sh register     # POST 2 resources: Account Information API + Payments API
#   ./register.sh verify       # GET the discovery endpoint and list supported types
#
# Environment (defaults shown):
#   IS_HOST=https://localhost:9443     target IS base URL
#   IS_AUTH=admin:admin                Basic-auth credentials (user:password)
#
set -euo pipefail

RAR_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TYPES_JSON="$RAR_DIR/types.json"
IS_HOST="${IS_HOST:-https://localhost:9443}"
IS_AUTH="${IS_AUTH:-admin:admin}"

command -v jq >/dev/null 2>&1 || { echo "ERROR: jq is required but not installed." >&2; exit 1; }
command -v curl >/dev/null 2>&1 || { echo "ERROR: curl is required but not installed." >&2; exit 1; }

# Build the full api-resources request body for a resource key (accounts|payments|all).
build_body() {
  local rkey="$1"
  local rname ridentifier rdesc adt t tname tdesc tschemafile schema_path
  rname=$(jq -r ".resources.\"$rkey\".name" "$TYPES_JSON")
  ridentifier=$(jq -r ".resources.\"$rkey\".identifier" "$TYPES_JSON")
  rdesc=$(jq -r ".resources.\"$rkey\".description" "$TYPES_JSON")

  adt="[]"
  while IFS= read -r t; do
    tname=$(jq -r ".types.\"$t\".name" "$TYPES_JSON")
    tdesc=$(jq -r ".types.\"$t\".description" "$TYPES_JSON")
    tschemafile=$(jq -r ".types.\"$t\".schemaFile" "$TYPES_JSON")
    schema_path="$RAR_DIR/$tschemafile"
    [ -f "$schema_path" ] || { echo "ERROR: schema file not found: $schema_path" >&2; exit 1; }
    adt=$(jq --arg type "$t" --arg name "$tname" --arg desc "$tdesc" \
             --slurpfile schema "$schema_path" \
             '. + [{type:$type, name:$name, description:$desc, schema:$schema[0]}]' <<<"$adt")
  done < <(jq -r ".resources.\"$rkey\".types[]" "$TYPES_JSON")

  jq -n --arg name "$rname" --arg id "$ridentifier" --arg desc "$rdesc" --argjson adt "$adt" \
     '{name:$name, identifier:$id, description:$desc, requiresAuthorization:true, authorizationDetailsTypes:$adt}'
}

post_resource() {
  local rkey="$1" body http out
  body=$(build_body "$rkey")
  echo ">> POST $IS_HOST/api/server/v1/api-resources  (resource: $rkey)" >&2
  out=$(mktemp)
  http=$(curl -sk -o "$out" -w '%{http_code}' -X POST \
    "$IS_HOST/api/server/v1/api-resources" \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -u "$IS_AUTH" \
    --data "$body")
  echo "   HTTP $http" >&2
  jq . "$out" 2>/dev/null || cat "$out"
  rm -f "$out"
  case "$http" in
    2*) return 0 ;;
    *)  echo "ERROR: registration failed for resource '$rkey' (HTTP $http)." >&2; return 1 ;;
  esac
}

case "${1:-}" in
  print)
    echo "// ---- Account Information API ----"; build_body accounts
    echo "// ---- Payments API ----"; build_body payments
    ;;
  register)
    post_resource accounts
    post_resource payments
    ;;
  verify)
    echo ">> GET $IS_HOST/oauth2/token/.well-known/openid-configuration" >&2
    curl -sk "$IS_HOST/oauth2/token/.well-known/openid-configuration" \
      | jq '{authorization_details_types_supported}'
    ;;
  *)
    grep '^#' "$0" | sed 's/^# \{0,1\}//'
    exit 2
    ;;
esac
