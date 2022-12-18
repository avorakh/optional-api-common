package by.avorakh.common.optionalapi.utl

import by.avorakh.common.optionalapi.CommonSpecification
import by.avorakh.common.optionalapi.resource.SubscriptionData
import by.avorakh.common.optionalapi.resource.SubscriptionType
import by.avorakh.common.optionalapi.resource.UserAccount

class SubscriptionUtilTest extends CommonSpecification {

    def "should extract subscription type"() {

        when:
            def result = SubscriptionUtil.extractSubscriptionType(account)
        then:
            result.isPresent() == expected
        where:
            account                                                                                                  | expected
            null                                                                                                     | false
            new UserAccount(id, username, created)                                                                   | false
            new UserAccount(id, username, null, new SubscriptionData(type: null), created, created)                  | false
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.GOLD), created, created) | true
    }

    def "should extract subscription type or default value"(SubscriptionType expected) {

        when:
            def result = SubscriptionUtil.extractSubscriptionType(account, SubscriptionType.FREE)

        then:
            result.isPresent()
            result.get() == expected
        where:
            account                                                                                                    | expected
            null                                                                                                       | SubscriptionType.FREE
            new UserAccount(id, username, created)                                                                     | SubscriptionType.FREE
            new UserAccount(id, username, null, new SubscriptionData(type: null), created, created)                    | SubscriptionType.FREE
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.GOLD), created, created)   | SubscriptionType.GOLD
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.SILVER), created, created) | SubscriptionType.SILVER
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.FREE), created, created)   | SubscriptionType.FREE
    }

    def "should verify that account subscription type is empty"() {

        expect:
            SubscriptionUtil.isEmptySubscriptionType(account) == expected
        where:
            account                                                                                                  | expected
            null                                                                                                     | true
            new UserAccount(id, username, created)                                                                   | true
            new UserAccount(id, username, null, new SubscriptionData(type: null), created, created)                  | true
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.GOLD), created, created) | false
    }
}
