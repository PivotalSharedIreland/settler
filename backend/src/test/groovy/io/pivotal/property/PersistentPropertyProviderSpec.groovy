package io.pivotal.property

import spock.lang.Specification

class PersistentPropertyProviderSpec extends Specification {

    PersistentPropertyProvider provider

    void setup() {
        provider = new PersistentPropertyProvider(propertyRepository: Mock(PropertyRepository));
    }

    def "return a list of properties"() {
        when:
        def properties = provider.find()

        then:
        1 * provider.propertyRepository.findAll() >> {
            [new Property(id: 1, address: "Address 1")]
        }
        properties.size() == 1
    }

    def "return a single property" () {
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

    def "save" () {
        given:
        def property = new Property(address: "Address 1")

        when:
        provider.save(property)

        then:
        1 * provider.propertyRepository.save(property) >> {
            property.id = 1L

            property
        }

        property.id == 1L

    }

    def "should throw exception if property already exists"(){
        given:
        Property property = new Property(id: 1, address: "1 O'Connell")
        PersistentPropertyProvider provider = new PersistentPropertyProvider(propertyRepository: Mock(PropertyRepository))

        when:
        provider.save(property)

        then:
        1 * provider.propertyRepository.exists( property.id ) >> true
        thrown(PropertyAlreadyExistsException)
    }
}
