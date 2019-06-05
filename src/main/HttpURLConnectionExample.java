package main;

import java.util.List;

import http.HttpRequest;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String getRequestUrl = "";
    private static final String initialUrl = "http://10.0.0.244:8080/order";
    private static final String orderBody = "<order xmlns=\"http://schemas.restbucks.com\">\n" +
        "  <name>Josh</name>\n" +
        "  <item>\n" +
        "    <milk>semi</milk>\n" +
        "    <size>large</size>\n" +
        "    <drink>americano</drink>\n" +
        "  </item>\n" +
        "  <location>takeaway</location>\n" +
        "</order>";

    private static final String paymentBody ="<payment xmlns=\"http://schemas.restbucks.com\">\n" +
        "  <amount>1.80</amount>\n" +
        "  <cardholderName>Josh</cardholderName>\n" +
        "  <cardNumber>1234</cardNumber>\n" +
        "  <expiryMonth>12</expiryMonth>\n" +
        "  <expiryYear>12</expiryYear>\n" +
        "</payment>";

    private static List<String> hyperMediaLinks;

    public static void main(String[] args) throws Exception {

        //main.HttpURLConnectionExample http = new main.HttpURLConnectionExample();
        HttpRequest http = new HttpRequest();


        System.out.println("\n>>>Testing 1 - Send Http POST request - Creating order");
        http.sendPost(initialUrl, orderBody);
        hyperMediaLinks = http.getLatestHyperMediaLinks();

        //System.out.println("\n\n\n>>>Testing 2 - Send Http GET request - Checking order");
        //getRequestUrl = http.getLocationUrl(); //assume order is successful
        //http.sendGet(getRequestUrl);

        System.out.println("\n\n\n>>>Testing 3 - Send Http PUT request - pay for order");
        //search links for payment hypermedia link
        String putUrl = "";
        for (String hypermediaLink: hyperMediaLinks) {
            if (putUrl != "") {
                break;
            }
            if (hypermediaLink.contains("payment")) {
                //set putUrl
                putUrl = hypermediaLink.substring(hypermediaLink.indexOf("uri=\"")+5, hypermediaLink.indexOf("\" m"));
            }
        }
        if (!putUrl.isEmpty()) {
            //pay for coffee
            http.sendPut(putUrl, paymentBody);
            hyperMediaLinks = http.getLatestHyperMediaLinks();
        } else {
            throw new Exception("SHIT HAS HIT THE FAN!");
        }

        //get until status = ready

        //delete recipet once ready

    }

}
