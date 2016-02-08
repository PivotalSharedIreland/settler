package io.pivotal.property;

import io.pivotal.contact.Contact;
import io.pivotal.contact.ContactsProvider;
import io.pivotal.error.ForbiddenActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Autowired
    private PropertyProvider propertyProvider;

    @Autowired
    private ContactsProvider contactsProvider;

    @RequestMapping
    public List<Property> getList() {
        return propertyProvider.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> get(@PathVariable Long id) {
        Property property = propertyProvider.findOne(id);
        HttpStatus statusCode = exists(property) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(property, statusCode);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public Property save(@Valid @RequestBody Property property) {

        createNewContact(property);

        return propertyProvider.save(property);
    }

    private void createNewContact(@Valid @RequestBody Property property) {
        for (Contact contact : property.getContacts()){
            //check contact
            // if property contains id -> save property
            // else create the contact
            // and then save the property
            if(contact.getId() == null){
                Contact saved = contactsProvider.save(contact);
                contact.setId(saved.getId());
            }
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyProvider.remove(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Property update(@PathVariable Long id, @RequestBody Property property) {
        if (!Objects.equals(id, property.getId())) {
            throw new ForbiddenActionException("Request body does not match the property id");
        }

        createNewContact(property);

        return propertyProvider.update(property);
    }

    private boolean exists(Property property) {
        return property != null;
    }
}
