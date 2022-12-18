package by.avorakh.common.optionalapi

import spock.lang.Shared
import spock.lang.Specification

class CommonSpecification extends Specification {

    @Shared
    def id = UUID.randomUUID().toString()
    @Shared
    def username = "username"
    @Shared
    def created = System.currentTimeMillis()
}
