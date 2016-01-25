package io.pivotal.contact;

import io.pivotal.error.ForbiddenActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;


@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactsProvider contactProvider;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public Contact save(@RequestBody Contact contact) {
        return contactProvider.save(contact);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Contact> find() {
        return contactProvider.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Contact> findOne(@PathVariable Long id) {
        Contact contact = contactProvider.findOne(id);
        return contact != null ? ResponseEntity.ok(contact) : new ResponseEntity<>(NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        contactProvider.remove(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Contact update(@PathVariable Long id, @RequestBody Contact contact) {
        if(!Objects.equals(id, contact.getId())){
            throw new ForbiddenActionException("Request body does not match the contact id");
        }
        return contactProvider.update(contact);
    }
}
