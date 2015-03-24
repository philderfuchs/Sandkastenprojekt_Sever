import sandkastenprojekt.server.Event
import sandkastenprojekt.server.Tag
import sandkastenprojekt.server.service.TestDataGenerationService

import static ratpack.groovy.Groovy.context
import static ratpack.groovy.Groovy.ratpack
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import redis.clients.jedis.Jedis

ratpack {
    handlers {

        Jedis redis = new Jedis("localhost");

        handler("api/events") {
            context.byMethod {
                get {

                    // get the sorted event id's of the next few months
                    double sixMonthsInSeconds = 365 / 2 * 24 * 60 * 60
                    Set events = redis.zrangeByScore("eventsByDate", (((new Date()).getTime()) / 1000 - 24*60*60).doubleValue(), (((new Date()).getTime()) / 1000).doubleValue() + sixMonthsInSeconds)

                    // load all events and associated events in the given timeframe
                    String allEvents = "";
                    String allTags = "";
                    for (String event_id in events) {
                        allEvents += redis.get("events:${event_id}") + ", "
                        for (String tag_id in redis.smembers("events:${event_id}:tags")) {
                            allTags += redis.get("tags:${tag_id}") + ", "
                        }
                    }

                    // delete last ", "
                    if(allEvents){
                        allEvents = allEvents.substring(0, allEvents.length() - 2)
                    }
                    if(allTags){
                        allTags = allTags.substring(0, allTags.length() - 2)
                    }

                    String responseString = '{ "events": [' + allEvents + '], "tags": [' + allTags + '] }';

                    // TODO: is there a new more fancy way to do this ?
                    context.response.contentType("application/json")
                    context.response.getHeaders().add('Access-Control-Allow-Origin', '*')
                    render(responseString)
                }

                post {

                    def jsonSlurper = new JsonSlurper()
                    def event = new Event(jsonSlurper.parseText(context.request.body.text.substring(9, context.request.body.text.length()-1)))
                    def eventID = 1
                    while(redis.exists("events:${eventID}")){
                        eventID++
                    }
                    event.id = eventID.toString()
                    redis.set("events:${eventID}", JsonOutput.toJson(event))
                    redis.zadd("eventsByDate", (new Date(event.date).getTime()/1000).doubleValue(), eventID.toString())
                    for(String tagID in event.tags){
                        redis.sadd("events:${eventID}:tags", tagID)
                    }

                    context.response.contentType("application/json")
                    context.response.getHeaders().add('Access-Control-Allow-Origin', '*')
                    render('{"event": ' + JsonOutput.toJson(event) + '}')
                }
            }
        }


        handler("api/tags") {
            context.byMethod {
                get {

                    // TODO: is there a new more fancy way to do this ?
                    context.response.contentType("application/json")
                    context.response.getHeaders().add('Access-Control-Allow-Origin', '*')
                    render("hallo")
                }
                post {
                    def jsonSlurper = new JsonSlurper()
                    def tag = new Tag(jsonSlurper.parseText(context.request.body.text.substring(7, context.request.body.text.length()-1)))
                    def tagID = 1
                    while(redis.exists("tags:${tagID}")){
                        tagID++
                    }
                    tag.id = tagID.toString()
                    redis.set("tags:${tagID}", JsonOutput.toJson(tag))

                    context.response.contentType("application/json")
                    context.response.getHeaders().add('Access-Control-Allow-Origin', '*')
                    render('{"tag": ' + JsonOutput.toJson(tag) + '}')

                }
            }
        }


    }
}