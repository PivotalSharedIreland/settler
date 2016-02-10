package io.pivotal.property

import io.pivotal.contact.ContactsProvider
import spock.lang.Specification

class PropertyManagerSpec extends Specification {

    private PropertyManager manager

    void setup() {
        manager = new PropertyManager(Mock(PropertyProvider), Mock(ContactsProvider))
    }

    def "return a list of properties"() {
        when:
        def properties = manager.findAll()

        then:
        1 * manager.propertyProvider.findAll() >> {
            [new Property(id: 1, address: "Address 1")]
        }
        properties.size() == 1
    }

    def "return a single property"() {
        given:
        def property = new Property(id: 1, address: "Address 1")

        when:
        def result = manager.findOne(1)

        then:
        1 * manager.propertyProvider.findOne(1) >> {
            property
        }

        result == property
    }

    def "save a new property"() {
        given:
        def property = new Property(address: "Address 1")

        when:
        manager.save(property)

        then:
        1 * manager.propertyProvider.save(property) >> {
            property.id = 1L

            property
        }

        property.id == 1L
    }

    def "delete a specific property"() {
        given:
        Property property = new Property(id: 1, address: "1 O'Connell")

        when:
        manager.remove(property.id)

        then:
        1 * manager.propertyProvider.remove(property.id)
    }

    def "update a specific property"() {

        given:
        Property property = new Property(id: 1, address: "New Address")

        when:
        def updatedProperty = manager.update(property)

        then:
        1 * manager.propertyProvider.update(property) >> {
            new Property(id: 1, address: "New Address updated")
        }

        updatedProperty.id == property.id
        updatedProperty.address == "New Address updated"

    }
}
