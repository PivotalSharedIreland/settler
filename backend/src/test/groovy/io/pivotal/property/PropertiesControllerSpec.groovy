package io.pivotal.property

import io.pivotal.error.EntityAlreadyExistsException
import io.pivotal.error.EntityNotFoundException
import io.pivotal.error.ErrorHandler
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PropertiesControllerSpec extends Specification {

    MockMvc mockMvc
    PropertiesController propertiesController

    void setup() {
        propertiesController = new PropertiesController(Mock(PropertyManager))
        mockMvc = MockMvcBuilders.standaloneSetup(propertiesController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "should return properties"() {
        when:
        def response = mockMvc.perform(get("/properties"))

        then:
        1 * propertiesController.propertyManager.findAll() >> {
            [new Property(id: 1, address: "Address 1"), new Property(id: 2, address: "Address 2")]
        }

        response.andExpect(status().isOk()).andExpect(jsonPath('$', hasSize(2))).andExpect(jsonPath('$[0].address', equalTo("Address 1")))
    }

    def "should return a single property"() {

        when:
        def response = mockMvc.perform(get("/properties/1"))

        then:
        1 * propertiesController.propertyManager.findOne(1L) >> {
            new Property(id: 1, address: "Address 1")
        }

        response.andExpect(status().isOk()).andExpect(jsonPath('$.id', equalTo(1))).andExpect(jsonPath('$.address', equalTo('Address 1')))
    }

    def "should return an error when property does not exist"() {
        when:
        def response = mockMvc.perform(get("/properties/500"))

        then:
        1 * propertiesController.propertyManager.findOne(500) >> {
            null
        }

        response.andExpect(status().isNotFound())
    }

    def "should create a new property"() {
        given:
        def content = '{"address": "Address 2"}'

        when:
        def response = mockMvc.perform(post("/properties").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * propertiesController.propertyManager.save(_ as Property) >> { Property input  ->
            input
        }

        response.andExpect(status().isCreated()).andExpect(jsonPath('$.address', equalTo('Address 2')))
    }


    def "should fail trying to create a property that already exists"() {

        given:
        def content = '{ "address": "something that already exists" }'

        when:
        def response = mockMvc.perform(post("/properties").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * propertiesController.propertyManager.save(_ as Property) >> {
            throw new EntityAlreadyExistsException("Property already exists")
        }

        response.andExpect(status().isConflict())
    }

    def "should delete a property"() {

        given:
        def content = '{ "address": "something that already exists" }'

        when:
        def response = mockMvc.perform(delete("/properties/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * propertiesController.propertyManager.remove(1L)

        response.andExpect(status().isNoContent())
    }

    def "should update the property"() {

        given:
        def content = '{"id" : "1" , "address": "New address" }'

        when:
        def response = mockMvc.perform(put("/properties/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * propertiesController.propertyManager.update(_ as Property) >> { Property property ->
             new Property(id: 1, address: "New address")
        }

        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.id', equalTo(1)))
                .andExpect(jsonPath('$.address', equalTo('New address')))
    }

    def "should fail if I update a non existing property"() {

        given:
        def content = '{"id" : "1" , "address": "Faulty address" }'

        when:
        def response = mockMvc.perform(put("/properties/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        1 * propertiesController.propertyManager.update(_ as Property) >> { Property property ->
            throw new EntityNotFoundException("Property does not exist")
        }

        response.andExpect(status().isNotFound())
    }

    def "should return forbidden if id does not match the proeperty id"() {

        given:
        def content = '{"id" : "2" , "address": "New address" }'

        when:
        def response = mockMvc.perform(put("/properties/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))

        then:
        0 * propertiesController.propertyManager.update(_ as Property)

        response.andExpect(status().isForbidden())
    }
}