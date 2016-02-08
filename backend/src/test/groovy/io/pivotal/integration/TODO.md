#TODOs

~~1. Add email to the contact~~
1. Check into nested entities

~~- New Property + New Contact~~
~~- New Property + Existing Contact + New Contact~~
- 


================================

We are looking into many to many relationship in JPA, we have a property entity and a contact entity. Trying to persist both entities in one go, which is working property using CascadeType.PERSIST.

```
{ 
    "property": {
        "address": "Test Property",
        "contacts": [
            {"name": "Test Contact"}
         ]
    }
}
```

But on the case that we want to create a new property using already existing contacts we haven't found the way to deal with that. We were hoping that something like this would do it.

```
{
    "property": {
        "address": "Test Property",
        "contacts": [
            {"id": 1}
        ]
    }
}
```
But we are getting this error: "detached entity passed to persist: io.pivotal.contact.Contact; nested exception is org.hibernate.PersistentObjectException: detached entity passed to persist: io.pivotal.contact.Contact"

https://gist.github.com/PivotalSharedIreland/961ae3acc8546772eabf
