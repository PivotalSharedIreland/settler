package io.pivotal.contact;

import java.util.List;

public interface ContactsProvider {

    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact> findAll();
    Contact findOne(Long id);
    void delete(Long id);
    boolean exists(Long id);

}
