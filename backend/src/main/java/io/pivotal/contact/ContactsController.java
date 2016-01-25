package io.pivotal.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactProvider contactProvider;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public Contact save(Contact contact) { return contactProvider.save(contact); }


}
