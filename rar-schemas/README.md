# RAR authorization details types for WSO2 Identity Server

This folder registers the UK Open Banking **Rich Authorization Requests (RAR, RFC 9396)**
authorization details *types* into WSO2 Identity Server (IS 7.x), so IS can validate incoming
`authorization_details` on the token / authorize endpoints.

Registration uses the **API Resource Management REST API** — `POST /api/server/v1/api-resources`.
Each API resource carries an `authorizationDetailsTypes[]` array; each entry is
`{type, name, description, schema}` where `schema` is a **JSON Schema Draft 2020-12** document.
See the [WSO2 RAR guide](https://is.docs.wso2.com/en/next/guides/authorization/rich-authorization-requests/).

## Contents

```
rar-schemas/
  account_information.schema.json          one full-fidelity JSON Schema (Draft 2020-12) per type
  domestic_payment.schema.json
  domestic_scheduled_payment.schema.json
  domestic_standing_order.schema.json
  international_payment.schema.json
  types.json                               resource groupings + per-type name/description/schema
  register.sh                              assembles the request bodies (jq) and POSTs them
  README.md
```

## The 5 types

| type | source spec object |
|---|---|
| `account_information` | `OBReadConsent1` (Permissions + Expiration/Transaction date range) |
| `domestic_payment` | `OBWriteDomestic2` (Initiation + Risk + consent wrappers) |
| `domestic_scheduled_payment` | `OBWriteDomesticScheduled2` |
| `domestic_standing_order` | `OBWriteDomesticStandingOrder3` |
| `international_payment` | `OBWriteInternational3` |

## Prerequisites

- `jq` and `curl` on the PATH.
- A running WSO2 IS 7.x with API-resource management enabled.
- Admin credentials for Basic auth (default `admin:admin`).

## Usage

```bash
# Dry run — print the request bodies without calling IS:
./register.sh print

# Register (POST to IS):
./register.sh register      # Account Information API + Payments API (2 resources)

# Confirm what IS now advertises:
./register.sh verify        # GET /oauth2/token/.well-known/openid-configuration
```

Override the target/credentials via environment variables:

```bash
IS_HOST=https://my-is-host:9443 IS_AUTH=admin:'S3cret!' ./register.sh register
```

### The two API resources

`types.json` always registers both resources together:

- `Account Information API` (`account_information_api`, 1 type: `account_information`).
- `Payments API` (`payments_api`, 4 types: `domestic_payment`, `domestic_scheduled_payment`,
  `domestic_standing_order`, `international_payment`).
