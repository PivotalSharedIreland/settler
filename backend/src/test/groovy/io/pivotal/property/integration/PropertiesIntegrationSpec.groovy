package io.pivotal.property.integration

import io.pivotal.SettlerApplication
import io.pivotal.property.Property
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.http.HttpMethod.PUT

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = SettlerApplication.class)
@WebAppConfiguration
@IntegrationTest
class PropertiesIntegrationSpec extends Specification{

    @Value('${server.port}')
    int port

    String basePath = "properties"

    RestTemplate restTemplate = new TestRestTemplate()

    String url(String path = "") {
        "http://127.0.0.1:$port/${basePath}${path}"
    }

    def "check all Rest API"() {

        given:
        def property = null

        //Check the properties list is empty
        when:
        def propertiesList = loadProperties()

        then:
        propertiesList.statusCode == HttpStatus.OK
        propertiesList.body.size() == 0

        //Create one property
        when:
        def createResponse = createProperty(new Property(address: 'Pearse st, Dublin'))

        then:
        createResponse.statusCode == HttpStatus.CREATED
        createResponse.body.id != null
        createResponse.body.address == 'Pearse st, Dublin'

        //Check there is just one property
        when:
        property = createResponse.body
        propertiesList = loadProperties()

        then:
        propertiesList.statusCode == HttpStatus.OK
        propertiesList.body.size() == 1
        propertiesList.body[0].id == property.id

        //Update the property and check it has actually changed
        when:
        property.address = 'Grand Canal Dock'
        def updateResponse = updateProperty(property)
        propertiesList = loadProperties()

        then:
        updateResponse.statusCode == HttpStatus.OK
        propertiesList.statusCode == HttpStatus.OK
        propertiesList.body.size() == 1
        propertiesList.body[0].address == property.address

        //delete the property
        when:
        deleteProperty(property)
        propertiesList = loadProperties()

        then:
        propertiesList.statusCode == HttpStatus.OK
        propertiesList.body.size() == 0

    }

    private deleteProperty(Property property) {
        restTemplate.delete(url('/{id}'), property.id)
    }

    private ResponseEntity<Property> updateProperty(Property property) {
        restTemplate.exchange(url("/{id}"), PUT, new HttpEntity<Property>(property), Property.class, property.id)
    }

    private ResponseEntity<Property> createProperty(Property property) {
        restTemplate.postForEntity(url(), property , Property.class)
    }

    private ResponseEntity<Property> loadProperties() {
        restTemplate.getForEntity(url(), Property[]) as ResponseEntity<Property>
    }
}
