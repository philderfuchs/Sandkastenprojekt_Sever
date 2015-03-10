import ratpack.handling.Handler;
import ratpack.handling.Context;

import static ratpack.jackson.Jackson.json
import static ratpack.groovy.Groovy.ratpack


ratpack {
    handlers {
        get("events") {
            new event("Saxons", "29.3.2015", "SAXONY", "Dresden")
            render json(event)
        }
        //assets('../Sandkastenprojekt_ember/dist')


    }
}