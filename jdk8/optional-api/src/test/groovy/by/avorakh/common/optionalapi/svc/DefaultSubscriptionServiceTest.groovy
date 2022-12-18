package by.avorakh.common.optionalapi.svc

import by.avorakh.common.optionalapi.CommonSpecification
import by.avorakh.common.optionalapi.resource.SubscriptionData
import by.avorakh.common.optionalapi.resource.SubscriptionType
import by.avorakh.common.optionalapi.resource.UserAccount
import by.avorakh.common.optionalapi.dao.SubscriptionDataDao

class DefaultSubscriptionServiceTest extends CommonSpecification {

    SubscriptionDataDao subscriptionDataDao
    SubscriptionService service

    def setup() {

        subscriptionDataDao = Mock(SubscriptionDataDao)
        service = new DefaultSubscriptionService(subscriptionDataDao)
    }

    def "should successfully Find Subscription Type"() {

        given:
            def expectedSubscriptionType = supportedType
        and:
            def account = new UserAccount(id, username, null, new SubscriptionData(type: supportedType), created, created)
        expect:
            service.findSubscriptionType(account) == expectedSubscriptionType
        where:
            supportedType << SubscriptionType.values()
    }

    def "should thrown exception on Finding Subscription Type if the type is not present"() {

        when:
            service.findSubscriptionType(account)
        then:
            thrown(NoSuchElementException)
        where:
            account << [
                null,
                new UserAccount(id, username, created),
                new UserAccount(id, username, null, new SubscriptionData(type: null), created, created)
            ]
    }

    def "should successfully find SubscriptionData by id if this is present"() {

        when:
            def actual = service.findSubscriptionData(id)
        then:
            1 * subscriptionDataDao.find(id) >> new SubscriptionData(id: id, type: SubscriptionType.GOLD, created: created, modified: created)
        then:
            actual != null
            with (actual){
                getId() != null
                getType() != null
                getCreated() != null
                getModified() != null
            }
    }

    def "should thrown exception on finding SubscriptionData by id if this does not exist" () {
        when:
            service.findSubscriptionData(id)
        then:
            1 * subscriptionDataDao.find(id) >> null
        then:
            thrown(NoSuchElementException)
    }
}
