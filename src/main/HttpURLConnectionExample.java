package main;

import http.HttpRequest;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String getRequestUrl = "";
    private static final String initialUrl = "http://10.0.0.244:8080/order";

    public static void main(String[] args) throws Exception {

        //main.HttpURLConnectionExample http = new main.HttpURLConnectionExample();
        HttpRequest http = new HttpRequest();


        System.out.println("\n>>>Testing 1 - Send Http POST request - Creating order");
        http.sendPost(initialUrl);

        System.out.println("\n\n\n>>>Testing 2 - Send Http GET request - Checking order");
        getRequestUrl = http.getLocationUrl();
        http.sendGet(getRequestUrl);

    }

}
