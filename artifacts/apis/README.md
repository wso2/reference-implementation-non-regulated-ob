# API surface — what each endpoint actually does

Which policies each endpoint needs, which reach the bank backend, and why. The answers are not uniform
and are not visible from the OpenAPI specs alone.

The **accelerator** owns the consent and may call one of this repo's extensions for a decision before
letting a request through. The **bank backend** holds the data and knows nothing about consents. A
*user* token (authorization code) is bound to a consent; a *client* token (client credentials) is not.

Policies are applied in the order listed, with **dynamic endpoint always last** — it is what routes the
call on to the backend. **Consent enforcement** is the policy that sends the request through
`ValidateConsentAccessApi` for a decision.

## Account Information API — `/open-banking/{version}/account-information`

| Endpoint | Token | Policies | Bank backend | Why |
|---|---|---|---|---|
| `GET /accounts` | user | mTLS, JWT claim based access, consent enforcement, dynamic endpoint | ✅ | Customer's data — needs their authority, and it lives in the bank |
| `GET /accounts/{AccountId}` | user | ″ | ✅ | ″ |
| `GET /accounts/{AccountId}/balances` | user | ″ | ✅ | ″ |
| `GET /accounts/{AccountId}/transactions` | user | ″ | ✅ | ″ |
| `GET /balances` | user | ″ | ✅ | ″ |
| `GET /transactions` | user | ″ | ✅ | ″ |
| `DELETE /consents/{ConsentId}` | **client** | mTLS, JWT claim based access, dynamic endpoint | ❌ | **No consent enforcement** — it acts on the consent rather than under it. Handled by `PreProcessConsentRevokeApi` |

## Payment Initiation API — `/open-banking/{version}/payment-initiation`

| Endpoint | Token | Policies | Bank backend | Why |
|---|---|---|---|---|
| `POST /domestic-payments` | user | mTLS, JWT claim based access, consent enforcement, dynamic endpoint | ✅ | The body must match the authorized `Initiation`, field by field |
| `GET /domestic-payments/{DomesticPaymentId}` | **client** | mTLS, JWT claim based access, dynamic endpoint | ✅ | **No consent enforcement** — a status lookup carries a client token with no consent bound to enforce against |
| `POST /domestic-scheduled-payments` | user | mTLS, JWT claim based access, consent enforcement, dynamic endpoint | ✅ | As `POST /domestic-payments` |
| `GET /domestic-scheduled-payments/{DomesticScheduledPaymentId}` | **client** | mTLS, JWT claim based access, dynamic endpoint | ✅ | Status lookup |
| `POST /domestic-standing-orders` | user | mTLS, JWT claim based access, consent enforcement, dynamic endpoint | ✅ | As `POST /domestic-payments` |
| `GET /domestic-standing-orders/{DomesticStandingOrderId}` | **client** | mTLS, JWT claim based access, dynamic endpoint | ✅ | Status lookup |
| `POST /international-payments` | user | mTLS, JWT claim based access, consent enforcement, dynamic endpoint | ✅ | As `POST /domestic-payments` |
| `GET /international-payments/{InternationalPaymentId}` | **client** | mTLS, JWT claim based access, dynamic endpoint | ✅ | Status lookup |

Two endpoints are deliberately absent here, both because a payment consent is consumed by its
submission:

- **No consent DELETE.** Nothing long-lived is left to revoke, which is why
  `PreProcessConsentRevokeApiImpl` refuses payment types.
- **No funds confirmation.** A submission needs to validate the funds as well.
