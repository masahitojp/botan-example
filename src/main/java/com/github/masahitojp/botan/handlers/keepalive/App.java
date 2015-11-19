package com.github.masahitojp.botan.handlers.keepalive;

import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public App(final String ipAddress, final int port) {
        super(ipAddress, port);
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