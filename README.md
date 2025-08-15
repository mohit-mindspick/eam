# JWT Payload Example

## Before Role Expansion
```json
{
  "username": "alice",
  "roles": ["ADMIN"],
  "permissions": ["PERMISSION_EXPORT"]
}
```

## After Role Expansion (in JWT)
```json
{
  "username": "alice",
  "roles": ["ADMIN"],
  "permissions": [
    "PERMISSION_CREATE",
    "PERMISSION_READ",
    "PERMISSION_UPDATE",
    "PERMISSION_DELETE",
    "PERMISSION_EXPORT"
  ]
}
```

# Secured Endpoint Example

```java
@HasPermission("PERMISSION_CREATE")
@PostMapping("/api/permissions")
public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) { ... }
```

# Error Handling

- 401 Unauthorized: Invalid or expired JWT.
- 403 Forbidden: JWT does not contain required permission.
