package io.pivotal.integration

import io.pivotal.SettlerApplication
import io.pivotal.contact.Contact
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


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
        def emptyContactList = restTemplate.getForEntity(url(), Contact[] )

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
        def getContact = restTemplate.getForEntity(url("/$createdContact.body.id"), Contact.class)

        then:
        // get a contact
        getContact.body == createdContact.body

    }


}
