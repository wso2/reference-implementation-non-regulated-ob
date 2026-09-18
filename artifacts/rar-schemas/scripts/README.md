# RAR authorization details types for WSO2 Identity Server

This folder registers the Open Banking **Rich Authorization Requests (RAR, RFC 9396)**
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
  scripts/
    types.json                             resource groupings + per-type name/description/schema
    register.sh                            registers the API resources (assembles bodies via jq, POSTs them)
    authorize-app.sh                        authorizes an application to use the registered API resources
    README.md
```

Both scripts resolve `types.json` relative to their own location, and resolve each type's `schemaFile`
(from `types.json`) one directory up in `rar-schemas/` — so they work regardless of which directory
you invoke them from.

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

- `Account Information API` (`account_information_api`, 1 type: `account_information_v1.0`).
- `Payments API` (`payments_api`, 4 types: `domestic_payment_v1.0`, `domestic_scheduled_payment_v1.0`,
  `domestic_standing_order_v1.0`, `international_payment_v1.0`).

## Authorizing an application

Registering the API resources (above) makes their RAR types known to IS, but an application
still needs to be explicitly authorized to use them before it can request `authorization_details`
against them. `authorize-app.sh` does this via the **Application Management REST API** —
`POST /api/server/v1/applications/{applicationId}/authorized-apis`. It looks up each API
resource's `id` by its `identifier` (from `types.json`) and authorizes the given application for
both `Account Information API` and `Payments API` in one run.

```bash
# Dry run — print the request bodies without calling IS:
APP_ID=<application-id> ./authorize-app.sh print

# Authorize (POST to IS):
APP_ID=<application-id> ./authorize-app.sh authorize
```

`APP_ID` is required — it's the application's id in WSO2 IS (visible in the Console URL when
viewing the application, or via `GET /api/server/v1/applications?filter=name+eq+<app-name>`).
Override the target/credentials the same way as `register.sh`:

```bash
IS_HOST=https://my-is-host:9443 IS_AUTH=admin:'S3cret!' APP_ID=<application-id> ./authorize-app.sh authorize
```

Run `register.sh register` before `authorize-app.sh authorize` — the latter depends on the API
resources already existing in IS.
