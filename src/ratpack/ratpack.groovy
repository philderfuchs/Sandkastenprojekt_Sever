import sandkastenprojekt.server.Event
import sandkastenprojekt.server.Tag

import static ratpack.groovy.Groovy.context;
import static ratpack.groovy.Groovy.ratpack
import groovy.json.JsonOutput


ratpack {
    handlers {
        get("api/events") {
            def testlist = []
            testlist[0] = new Event(
                    id: 1,
                    title: "Saxons",
                    date: "29.3.2015",
                    state: "SAXONY",
                    city: "Dresden",
                    tags: [1, 2]
            )
            def json = JsonOutput.toJson(testlist)
            def responseString = '{ "events": ' + json + '}'
            // TODO: is there a new more fancy way to do this ?
            context.response.contentType("application/json")
            context.response.getHeaders().add('Access-Control-Allow-Origin', '*' )
            render(responseString)
        }

        get("api/tags/:id") {
            def tag = new Tag(
                    id: context.pathTokens.id.toInteger(),
                    name: 'HipHop'
            )
            def json = JsonOutput.toJson(tag)
            def responseString = '{ "tag": ' + json + '}'
            // TODO: is there a new more fancy way to do this ?
            context.response.contentType("application/json")
            context.response.getHeaders().add('Access-Control-Allow-Origin', '*' )
            render(responseString)
        }
    }
}