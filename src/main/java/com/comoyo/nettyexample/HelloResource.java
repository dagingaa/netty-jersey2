package com.comoyo.nettyexample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created with IntelliJ IDEA.
 * User: dagingaa
 * Date: 1/2/13
 * Time: 12:13 AM
 * To change this template use File | Settings | File Templates.
 */

@Path("/")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String helloWorld() {
        System.err.println("REQUEST: /");
        return "Hello World";
    }
}
