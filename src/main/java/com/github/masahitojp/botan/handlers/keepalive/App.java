package com.github.masahitojp.botan.handlers.keepalive;

import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.Method.GET;

public class App extends NanoHTTPD {

    public App(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        final Method method = session.getMethod();

        final String uri = session.getUri();
        System.out.println(method + " " + uri);
        switch (method) {
            case GET:
            case HEAD:
                if (uri.startsWith("/botan/keepalive")) {
                    return newFixedLengthResponse("");
                }
                break;
            default:
                // do nothing

        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html","");
    }
}