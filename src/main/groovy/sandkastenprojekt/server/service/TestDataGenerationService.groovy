package sandkastenprojekt.server.service

import groovy.json.JsonOutput
import redis.clients.jedis.Jedis
import sandkastenprojekt.server.Event
import sandkastenprojekt.server.Tag

class TestDataGenerationService {

    public static generateTestData() {
        Jedis redis = new Jedis("localhost");

        // Add "the Truth"

        redis.flushAll();
        def tag = new Tag(
                //        id: context.pathTokens.id.toInteger(),
                id: "1",
                name: 'HipHop'
        )
        redis.set("tags:1", JsonOutput.toJson(tag))
        tag = new Tag(
                id: "2",
                name: 'Workshops'
        )
        redis.set("tags:2", JsonOutput.toJson(tag))

        def events = []
        events[0] = new Event(
                id: "1",
                title: "Saxons",
                date: "Thu Mar 14 2015 01:00:00 GMT+0100 (CET)",
                state: "SAXONY",
                city: "Dresden",
                tags: [2]
        )
        redis.set("events:1", JsonOutput.toJson(events[0]))
        events[1] = new Event(
                id: "2",
                title: "Improve your Style",
                date: "Thu Mar 14 2015 01:00:00 GMT+0100 (CET)",
                state: "THURINGIA",
                city: "Jena",
                tags: [1]
        )
        redis.set("events:2", JsonOutput.toJson(events[1]))

        // add eventsByDate
        redis.zadd("eventsByDate", ((new Date(events[0].date)).getTime()/1000).doubleValue(), events[0].id.toString())
        redis.zadd("eventsByDate", ((new Date(events[1].date)).getTime()/1000).doubleValue(), events[1].id.toString())

        //add events-tag map
        for(Event event in events){
            for(String s in event.tags) {
                redis.sadd("events:${event.id}:tags", s)
            }
        }

    }
}
