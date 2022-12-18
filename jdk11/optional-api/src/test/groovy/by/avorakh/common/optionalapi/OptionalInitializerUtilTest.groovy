package by.avorakh.common.optionalapi

import spock.lang.Specification

class OptionalInitializerUtilTest extends Specification {

    def "should successfully initialize Optional Empty"() {

        when:
            def actual = Optional.empty()
        then:
            !actual.isPresent() // since JDK 8
            actual.isEmpty()    // since JDK 11
    }

    def "should successfully initialize #value"(def value) {

        when:
            def actual = Optional.of(value)
        then:
            actual.isPresent()
        where:
            value << [new Object(), "test", 2]
    }

    def "should thrown NPE on null initialization"() {

        when:
            def actual = Optional.of(null)
        then:
            def result = thrown(NullPointerException)
    }

    def "should successfully initialize safely #value"(def value, boolean expected) {

        when:
            def actual = Optional.ofNullable(value)
        then:
            actual.isPresent() == expected
        where:
            value        | expected
            new Object() | true
            "test"       | true
            2            | true
            null         | false
    }
}
