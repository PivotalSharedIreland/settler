package io.pivotal.property

import io.pivotal.SettlerApplication
import io.pivotal.contact.Contact
import io.pivotal.contact.ContactsProvider
import io.pivotal.contact.ContactsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@WebAppConfiguration
@SpringApplicationConfiguration(SettlerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PropertyManagerTransactionalSpec extends Specification {

    @Autowired
    PropertyManager dbPropertyManager

    @Autowired
    ContactsProvider dbContactProvider

    @Autowired
    ContactsRepository contactsRepository

    @Autowired
    PropertyRepository propertyRepository



    def "save a new property with new contacts"() {

        given:
        def property = new Property(address: "Address 1")
        property.setContacts([new Contact(name: "Contact 1", phone: "5458548888", email: "test@settler.com")])


        when:
        dbPropertyManager.save(property)

        then:
        property.contacts[0].id != null
        property.contacts[0].name == "Contact 1"
        property.contacts[0].phone == "5458548888"
        property.contacts[0].email == "test@settler.com"
    }

    def "save a new property with existing contacts"() {

        given:
        def property = new Property(address: "Address 1")
        def contact = new Contact(name: "Contact 1", phone: "5458548888", email: "test@settler.com")


        when:
        contact = dbContactProvider.save(contact);

        //mock a contact from web request Json
        def contacts = [new Contact(id: contact.id, name: "Test name")]
        property.setContacts(contacts)
        property = dbPropertyManager.save(property)


        then:
        property.contacts.size() == 1
        property.contacts.eachWithIndex { dbContact, index ->
            dbContact.id == contacts[index].id
        }

    }

    def "save a new property with new and existing contacts"() {
        given:
        def property = new Property(address: "Address 1")
        def contact = new Contact(name: "Existing Contact", phone: "11111111", email: "exists@settler.com")
        def nonExistingContact = new Contact(name: "Non Existing Contact", phone: "22222222", email: "nonExisting@settler.com")

        when:
        def existingContact = dbContactProvider.save(contact);

        //mock a contact from web request Json
        def contacts = [new Contact(id: existingContact.id), nonExistingContact]
        property.setContacts(contacts)
        property = dbPropertyManager.save(property)


        then:
        property.contacts.size() == 2
        property.contacts.eachWithIndex { dbContact, index ->
            dbContact.id == contacts[index].id

        }
        dbContactProvider.findOne(existingContact.id).name == "Existing Contact"

    }


    def "should rollback creation of contacts if something fails" () {
        given:
        def property = Mock(Property.class)
        def nonExistingContact = new Contact(name: "Non Existing Contact", phone: "22222222", email: "nonExisting@settler.com")

        when:
        def justSavedContact = dbContactProvider.save(new Contact(name: "Existing Contact", phone: "11111111", email: "exists@settler.com"));

        property.getAddress() >> {
            throw new RuntimeException("Make it fail!");
        }
        property.getContacts() >> {
            [nonExistingContact, justSavedContact]
        }

        dbPropertyManager.save(property)

        then:
        thrown(RuntimeException)
        def List<Contact> allContactsFromDB = dbContactProvider.findAll()
        //should not have only the previously saved contact (saved in another transaction)
        allContactsFromDB.size() == 1
        allContactsFromDB[0] == justSavedContact
    }

}
