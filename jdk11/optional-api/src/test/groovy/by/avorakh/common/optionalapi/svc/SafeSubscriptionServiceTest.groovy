package by.avorakh.common.optionalapi.svc

import by.avorakh.common.optionalapi.CommonSpecification
import by.avorakh.common.optionalapi.dao.SubscriptionDataDao
import by.avorakh.common.optionalapi.resource.SubscriptionData
import by.avorakh.common.optionalapi.resource.SubscriptionType
import by.avorakh.common.optionalapi.resource.UserAccount

class SafeSubscriptionServiceTest extends CommonSpecification {

    SubscriptionDataDao subscriptionDataDao
    SubscriptionService service

    def setup() {
        subscriptionDataDao = Mock(SubscriptionDataDao)
        service = new SafeSubscriptionService(subscriptionDataDao)
    }

    def "should successfully Find Subscription Type"() {

        expect:
            service.findSubscriptionType(account) == expectedSubscriptionType
        where:
            account                                                                                                  | expectedSubscriptionType
            null                                                                                                     | SubscriptionType.FREE
            new UserAccount(id, username, created)                                                                   | SubscriptionType.FREE
            new UserAccount(id, username, null, new SubscriptionData(type: null), created, created)                  | SubscriptionType.FREE
            new UserAccount(id, username, null, new SubscriptionData(type: SubscriptionType.GOLD), created, created) | SubscriptionType.GOLD
    }

    def "should successfully find SubscriptionData by id if this is present"() {

        when:
            def actual = service.findSubscriptionData(id)
        then:
            1 * subscriptionDataDao.find(id) >> new SubscriptionData(id: id, type: SubscriptionType.GOLD, created: created, modified: created)
        then:
            actual != null
            with(actual) {
                getId() != null
                getType() != null
                getCreated() != null
                getModified() != null
            }
    }

    def "should return SubscriptionData with null fields on finding SubscriptionData by id if this does not exist"() {

        when:
            def actual = service.findSubscriptionData(id)
        then:
            1 * subscriptionDataDao.find(id) >> null
        then:
            actual != null
            with(actual) {
                getId() == null
                getType() == null
                getCreated() == null
                getModified() == null
            }
    }
}
