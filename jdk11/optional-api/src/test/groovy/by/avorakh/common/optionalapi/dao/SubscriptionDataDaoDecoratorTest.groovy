package by.avorakh.common.optionalapi.dao

import by.avorakh.common.optionalapi.resource.SubscriptionData
import spock.lang.Shared
import spock.lang.Specification

class SubscriptionDataDaoDecoratorTest extends Specification {

    @Shared
    String id = 'id'
 
    SubscriptionDataDao jdbcSubscriptionDataDao
    SubscriptionDataDao cachedSubscriptionDataDao

    SubscriptionDataDaoDecorator decorator

    def setup() {

        jdbcSubscriptionDataDao = Mock(SubscriptionDataDao)
        cachedSubscriptionDataDao = Mock(SubscriptionDataDao)
        decorator = new SubscriptionDataDaoDecorator(jdbcSubscriptionDataDao, cachedSubscriptionDataDao)
    }
    

    def "should successfully get from cache if the data is present"() {
        
        when:
            def actual = decorator.getFromCache(id)
        then:
            1 * cachedSubscriptionDataDao.find(id) >> new SubscriptionData()
        then:
            actual.isPresent()
    }

    def "should successfully get from cache if the data is not present"() {

        when:
            def actual = decorator.getFromCache(id)
        then:
            1 * cachedSubscriptionDataDao.find(id) >> null
        then:
            !actual.isPresent()
    }

    def "should successfully get from DB if the data is present"() {

        when:
            def actual = decorator.getFromDB(id)
        then:
            1 * jdbcSubscriptionDataDao.find(id) >> new SubscriptionData()
        then:
            actual.isPresent()
    }

    def "should successfully get from DB if the data is not present"() {

        when:
            def actual = decorator.getFromDB(id)
        then:
            1 * jdbcSubscriptionDataDao.find(id) >> null
        then:
            !actual.isPresent()
    }
    
    def "should successfully find data if the data is present in cache"(){
        
        when: 
            def actual  = decorator.find(id)
        then:
            1 * cachedSubscriptionDataDao.find(id) >> new SubscriptionData()
            0 * jdbcSubscriptionDataDao._
        then:
            actual!=null
    }

    def "should successfully find data if the data is present in DB"(){

        when:
            def actual  = decorator.find(id)
        then:
            1 * cachedSubscriptionDataDao.find(id) >> null
            1 * jdbcSubscriptionDataDao.find(id) >> new SubscriptionData()
        then:
            actual!=null
    }
    
    def "should thrown exception on find data find data if data is not present"(){
        when:
          decorator.find(id)
        then:
            1 * cachedSubscriptionDataDao.find(id) >> null
            1 * jdbcSubscriptionDataDao.find(id) >> null
        then:
           thrown(NoSuchElementException)
    }
}
