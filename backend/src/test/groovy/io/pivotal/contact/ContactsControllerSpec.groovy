package io.pivotal.contact

import io.pivotal.error.EntityNotFoundException
import io.pivotal.error.ErrorHandler
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ContactsControllerSpec extends Specification{

    MockMvc mockMvc
    ContactsController contactsController

    void setup() {
        contactsController = new ContactsController(contactProvider: Mock(ContactsProvider))
        mockMvc = MockMvcBuilders.standaloneSetup(contactsController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "create a new contact" () {
        given:
        def content = '{"name": "Contact 1", "phone": "0815554444", "email": "test@settler.io"}'

        when:
        def response = mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * contactsController.contactProvider.save(_ as Contact) >> { Contact contact ->

            assert contact.name == 'Contact 1'
            assert contact.phone == '0815554444'
            assert contact.email == 'test@settler.io'

            new Contact(name: "Contact 1", phone: "0815554444", email: "test@settler.io")
        }
        response.andExpect(status().isCreated()).andExpect(jsonPath('$.name', equalTo('Contact 1')))
    }

    def "return a list of contacts" () {
        when:
        def response = mockMvc.perform(get("/contacts").contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        1 * contactsController.contactProvider.findAll() >> {
            [new Contact(name: "Contact 1", phone: "0815554444"), new Contact(name: "Contact 2", phone: "12345")]
        }
        response.andExpect(status().isOk()).andExpect(jsonPath('$', hasSize(2)))
    }

    def "return contact details" () {
        when:
        def response = mockMvc.perform(get("/contacts/1").contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        1 * contactsController.contactProvider.findOne(1) >> {
            new Contact(id: 1, name: "Existing contact", phone: "0815554444", email: "test@settler.io")
        }
        response.andExpect(status().isOk()).andExpect(jsonPath('$.name', equalTo('Existing contact')))
    }

    def "return not found if contact does not exist" () {
        when:
        def response = mockMvc.perform(get("/contacts/1").contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        1 * contactsController.contactProvider.findOne(1) >> {
            null
        }
        response.andExpect(status().isNotFound())
    }

    def "delete a contact" () {
        when:
        def response = mockMvc.perform(delete("/contacts/1"))

        then:
        1 * contactsController.contactProvider.remove(1) >> {
            null
        }
        response.andExpect(status().isNoContent())
    }

    def "update a contact" () {
        given:
        def content = '{"id": 1, "name": "Contact 1", "phone": "1234", "email": "test@settler.io"}'

        when:
        def response = mockMvc.perform(put("/contacts/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * contactsController.contactProvider.update(_ as Contact) >> { Contact contact ->

            assert contact.name == 'Contact 1'
            assert contact.phone == '1234'
            assert contact.email == 'test@settler.io'

            new Contact(name: "Contact 1", phone: "updated number", email: "new@settler.io")
        }
        response.andExpect(status().isOk()).andExpect(jsonPath('$.phone', equalTo('updated number')))
    }

    def "return not found trying to update a non-existing contact" () {
        given:
        def content = '{"id": 1, "name": "Contact 1", "phone": "1234"}'

        when:
        def response = mockMvc.perform(put("/contacts/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * contactsController.contactProvider.update(_ as Contact) >> { throw new EntityNotFoundException("Contact does not exist") }
        response.andExpect(status().isNotFound())
    }

    def "return forbidden if id does not match the contact" () {
        given:
        def content = '{"id": 10, "name": "Contact 1", "phone": "1234"}'

        when:
        def response = mockMvc.perform(put("/contacts/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        0 * contactsController.contactProvider.update(_)
        response.andExpect(status().isForbidden())
    }
}
