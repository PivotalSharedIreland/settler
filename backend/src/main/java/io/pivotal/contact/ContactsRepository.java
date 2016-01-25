package io.pivotal.contact;

import org.springframework.data.repository.CrudRepository;

public interface ContactsRepository extends CrudRepository<Contact,Long> {
}
