package com.example.webview;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {

    public HttpServer(int port) throws Exception{
        super(port);
    }

    public HttpServer(String hostname, int port) throws Exception{
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse("服务器返回json");
    }
}
