package io.pivotal.contact;

import com.google.common.collect.Lists;
import io.pivotal.error.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class DefaultContactsProvider implements ContactsProvider {

    @Autowired
    private ContactsRepository repository;

    @Override
    public Contact save(Contact contact) {
        return repository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        if(!repository.exists(contact.getId())){
            throw new EntityNotFoundException(String.format("Contacts with id {} does not exist", contact.getId()));
        }

        return repository.save(contact);
    }

    @Override
    public List<Contact> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public Contact findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public void remove(Long id) {
        repository.delete(id);
    }

}
