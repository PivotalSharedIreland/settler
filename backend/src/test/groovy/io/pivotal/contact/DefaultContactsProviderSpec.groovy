package io.pivotal.contact

import io.pivotal.error.EntityNotFoundException
import spock.lang.Specification

class DefaultContactsProviderSpec extends Specification {

    DefaultContactsProvider contactsProvider

    def setup(){
        contactsProvider = new DefaultContactsProvider(repository: Mock(ContactsRepository))
    }

    def "save a new contact"() {
        given:
        Contact contact = new Contact(name: "John Doe")

        when:
        def savedContact = contactsProvider.save(contact)

        then:
        1 * contactsProvider.repository.save(contact) >> { Contact c ->
            c.id = 1

            c
        }
        savedContact.id == 1
    }

    def "update a contact"() {
        given:
        Contact contact = new Contact(id: 1, name: "John Doe")

        when:
        def updatedContact = contactsProvider.update(contact)

        then:
        1 * contactsProvider.repository.exists(contact.id) >> true
        1 * contactsProvider.repository.save(contact) >> { Contact c ->
            c.name = "updated"

            c
        }

        updatedContact.name == "updated"
    }

    def "throw exception updating a non-existing contact"() {
        given:
        Contact contact = new Contact(id: 1, name: "John Doe")


        when:
        contactsProvider.update(contact)

        then:
        1 * contactsProvider.repository.exists(contact.id) >> false
        thrown(EntityNotFoundException)
    }

    def "find all contacts"() {
        when:
        def contacts = contactsProvider.findAll()

        then:
        1 * contactsProvider.repository.findAll() >> {
            [new Contact(id: 1), new Contact(id:2)]
        }
        contacts.size() == 2
        contacts[0].id == 1
        contacts[1].id == 2
    }

    def "find one contact by id"() {
        when:
        def contact = contactsProvider.findOne(1)

        then:
        1 * contactsProvider.repository.findOne(1) >> {
            new Contact(id: 1)
        }
        contact.id == 1
    }

    def "delete by id"() {
        when:
        contactsProvider.remove(1)

        then:
        1 * contactsProvider.repository.delete(1)

    }
}
