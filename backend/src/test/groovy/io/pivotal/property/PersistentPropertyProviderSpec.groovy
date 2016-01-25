package io.pivotal.property

import io.pivotal.error.EntityAlreadyExistsException
import io.pivotal.error.EntityNotFoundException
import spock.lang.Specification

class PersistentPropertyProviderSpec extends Specification {

    PersistentPropertyProvider provider

    void setup() {
        provider = new PersistentPropertyProvider(propertyRepository: Mock(PropertyRepository));
    }

    def "return a list of properties"() {
        when:
        def properties = provider.findAll()

        then:
        1 * provider.propertyRepository.findAll() >> {
            [new Property(id: 1, address: "Address 1")]
        }
        properties.size() == 1
    }

    def "return a single property"() {
        given:
        def property = new Property(id: 1, address: "Address 1")

        when:
        def result = provider.findOne(1)

        then:
        1 * provider.propertyRepository.findOne(1) >> {
            property
        }

        result == property
    }

    def "save a new property"() {
        given:
        def property = new Property(address: "Address 1")

        when:
        provider.save(property)

        then:
        0 * provider.propertyRepository.exists(_)
        1 * provider.propertyRepository.save(property) >> {
            property.id = 1L

            property
        }

        property.id == 1L
    }

    def "should throw exception if property already exists"() {
        given:
        Property property = new Property(id: 1, address: "1 O'Connell")

        when:
        provider.save(property)

        then:
        1 * provider.propertyRepository.exists(property.id) >> true
        thrown(EntityAlreadyExistsException)
    }

    def "delete a specific property"() {
        given:
        Property property = new Property(id: 1, address: "1 O'Connell")

        when:
        provider.remove(property.id)

        then:
        1 * provider.propertyRepository.findOne(property.id) >> property
        1 * provider.propertyRepository.delete(property)
    }

    def "update a specific property"() {

        given:
        Property property = new Property(id: 1, address: "New Address")

        when:
        def updatedProperty = provider.update(property)

        then:
        1 * provider.propertyRepository.exists(property.id) >> true

        1 * provider.propertyRepository.save(property) >> {
            new Property(id: 1, address: "New Address updated")
        }

        updatedProperty.id == property.id
        updatedProperty.address == "New Address updated"

    }

    def "should fail if a update is attempted on a non existing Property"() {

        given:
        Property property = new Property(id: 1, address: "New Address")

        when:
        provider.update(property)

        then:
        1 * provider.propertyRepository.exists(property.id) >> false
        thrown(EntityNotFoundException)
    }

}
