package by.avorakh.common.optionalapi.svc

import by.avorakh.common.optionalapi.CommonSpecification
import by.avorakh.common.optionalapi.resource.SubscriptionType
import by.avorakh.common.optionalapi.resource.UserAccount
import by.avorakh.common.optionalapi.dao.UserAccountDao

class DefaultUserAccountServiceTest extends CommonSpecification {

    UserAccountDao userAccountDao
    SubscriptionService subscriptionService

    UserAccountService service

    def setup() {

        userAccountDao = Mock(UserAccountDao)
        subscriptionService = Mock(SubscriptionService)
        service = new DefaultUserAccountService(userAccountDao, subscriptionService)
    }

    def "should find empty user account by id if account does not exist"() {

        given:
            def nonExistedId = "testId"
        when:
            def actual = service.getUserAccount(nonExistedId)
        then:
            1 * userAccountDao.find(nonExistedId) >> Optional.empty()
            0 * subscriptionService._
        then:
            !actual.isPresent()
    }

    def "should successfully find user account by id if account exists"() {

        given:
            def exitedAccount = new UserAccount(id, username, created)
        when:
            def actual = service.getUserAccount(id)
        then:
            1 * userAccountDao.find(id) >> Optional.of(exitedAccount)
            1 * subscriptionService.findSubscriptionType(exitedAccount) >> SubscriptionType.FREE
        then:
            actual.isPresent()
            def actualAccount = actual.get()
            actualAccount.subscription == null
    }

    def "should successfully find user account by id and set subscription if user subscription type is #premiumType"(SubscriptionType premiumType) {

        given:
            def exitedAccount = new UserAccount(id, username, created)
        when:
            def actual = service.getUserAccount(id)
        then:
            1 * userAccountDao.find(id) >> Optional.of(exitedAccount)
            1 * subscriptionService.findSubscriptionType(exitedAccount) >> premiumType
        then:
            actual.isPresent()
            def actualAccount = actual.get()
            actualAccount.subscription != null
            actualAccount.subscription.type == premiumType
        where:
            premiumType << [SubscriptionType.SILVER, SubscriptionType.GOLD]
    }
}
