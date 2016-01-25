package io.pivotal.integration

import io.pivotal.SettlerApplication
import io.pivotal.contact.Contact
import io.pivotal.property.Property
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.http.HttpMethod.PUT

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = SettlerApplication.class)
@WebIntegrationTest
class ContactsIntegrationSpec extends Specification {

    @Value('${server.port}')
    int port

    String basePath = "contacts"

    RestTemplate restTemplate = new TestRestTemplate()

    String url(String path = "") {
        "http://127.0.0.1:$port/${basePath}${path}"
    }


    def "check all /CONTACTS REST endpoints" () {

        when:
        def emptyContactList = contactList()

        then:
        // Contacts list is empty
        emptyContactList.body.size() == 0

        when:
        def contact = new Contact(name: "Contact 1", phone: "0815554444")
        def createdContact = restTemplate.postForEntity(url(), contact, Contact.class)

        then:
        // created and return a contact
        createdContact.body.id != null
        createdContact.body.name == "Contact 1"
        createdContact.body.phone == "0815554444"

        when:
        def persistedContact = restTemplate.getForEntity(url("/$createdContact.body.id"), Contact.class)

        then:
        // get a contact
        persistedContact.body == createdContact.body

        then:
        // get the list with created contact
        contactList().body.size() == 1

        when:
        def changedContact = new Contact(id: persistedContact.body.id, phone: "0000000000")
        def updatedContact = restTemplate.exchange(url("/$persistedContact.body.id"), PUT, new HttpEntity<Contact>(changedContact),Contact.class)

        then:
        // update a contact
        updatedContact.body != persistedContact.body
        updatedContact.body.id == persistedContact.body.id
        updatedContact.body.name != persistedContact.body.name
        updatedContact.body.phone != persistedContact.body.phone

        // delete a contact
        // empty list after contact delete
        when:
        restTemplate.delete(url("/$persistedContact.body.id"))

        then:
        contactList().body.size() == 0
    }


    def contactList = {
       return restTemplate.getForEntity(url(), Contact[])
    }



}
