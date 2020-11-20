# REST service

>Use database which starts with aplication and auto inserts 3 roles (Admin, Manager, User)

>All methods for all actions with users are available on this endpoint - /users

## Method: Post

### Comment:
- password - must contain uppercase letter and number

- role - one to many relation

### Exemple input:

```json
{
   "login":"login_user",
   "name":"name_user",
   "password":"pass_user",
   "roles":[
      {
         "id":2
      }
   ]
}
```

---

# Method: Get

### Comment:

- If request has no body then response will contain all users array whithout roles
- If request has body with login then response will contain current user with all roles

### Exemple input:

```json
{
	"login" : "login_user"
}
```

---

# Method: Put

### Comment:

- Roles will be setup from request. P.S. if role was before update but not contain in request then role will be delete

### Exemple input:

```json
{
   "login":"login_user",
   "name":"name_user",
   "password":"pass_user",
   "roles":[
      {
         "id":1
      },
      {
         "id":2
      }
   ]
}
```

---

# Method: Delete

### Exemple input:

```json
{
	"login" : "login_user"
}
```
