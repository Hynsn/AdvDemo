package com.example.webview;

import org.json.JSONException;
import org.json.JSONObject;

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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd","张三");
            jsonObject.put("msg","170cm");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response resp= newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT ,jsonObject.toString());
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Max-Age", "3628800");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        resp.addHeader("Access-Control-Allow-Headers", "Authorization");
        resp.setChunkedTransfer(true);
        return resp;
    }
}
