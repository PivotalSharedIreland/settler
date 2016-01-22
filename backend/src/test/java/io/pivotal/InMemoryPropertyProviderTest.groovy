package io.pivotal

import spock.lang.Specification

class InMemoryPropertyProviderTest extends Specification {

    InMemoryPropertyProvider provider

    void setup() {
        provider = new InMemoryPropertyProvider();
    }

    def "GetList"() {
        when:
        def properties = provider.find()

        then:
        properties != null
    }

    def "findOne" () {

    }

    def "save" () {

    }
}
