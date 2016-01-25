package io.pivotal.contact

import io.pivotal.property.PropertyProvider
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ContactsControllerSpec extends Specification{

    MockMvc mockMvc
    ContactsController contactsController

    void setup() {
        contactsController = new ContactsController(contactProvider: Mock(ContactProvider))
        mockMvc = MockMvcBuilders.standaloneSetup(contactsController).build()
    }

    def "create a new contact" () {
        given:
        def content = '{"name": "Contact 1", "phone": "0815554444"}'

        when:
        def response = mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * contactsController.contactProvider.save(_ as Contact) >> {
            new Contact(name: "Contact 1", phone: "0815554444")
        }
        response.andExpect(status().isCreated()).andExpect(jsonPath('$.name', equalTo('Contact 1')))
    }
}
