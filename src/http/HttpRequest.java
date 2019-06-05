package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static http.HttpConstants.*;

public class HttpRequest {

    protected static String locationUrl = "";
    URL obj;
    HttpURLConnection con;
    int responseCode;
    BufferedReader in;

    List<String> latestHyperMediaLinks = new ArrayList<>();

    // HTTP GET request
    public void sendGet(final String requestUrl) throws Exception {

        obj = new URL(requestUrl);
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(GET);

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accepts", RESTBUCKS_CONTENT_TYPE);

        responseCode = con.getResponseCode();
        System.out.println(SENDING_REQUEST_LOG + requestUrl);
        System.out.println(RESPONSE_CODE_LOG + responseCode);

        in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(RESPONSE_LOG_HEADER);
        System.out.println(CODE_LOG_HEADER+con.getResponseCode());
        System.out.println(HEADERS_LOG_HEADER);
        logHeadersToConsole(con.getHeaderFields());

        //print result
        System.out.println(response.toString());

        if (response.toString().contains("ns3:link")) {
            latestHyperMediaLinks = retrivePostHyperMediaLinks(response.toString());
        }

    }

    // HTTP POST request
    public void sendPost(final String url, final String body) throws Exception {
        obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(POST);

        //add reuqest header
        con.setRequestProperty(CONTENT_TYPE_HEADER, RESTBUCKS_CONTENT_TYPE);
        con.setRequestProperty("Accepts", RESTBUCKS_CONTENT_TYPE);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        responseCode = con.getResponseCode();
        System.out.println(SENDING_REQUEST_LOG + url);
        System.out.println(BODY_LOG + body);
        System.out.println(RESPONSE_CODE_LOG + responseCode);

        in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(RESPONSE_LOG_HEADER);
        System.out.println(CODE_LOG_HEADER+con.getResponseCode());
        System.out.println(HEADERS_LOG_HEADER);

        logHeadersToConsole(con.getHeaderFields());

        if (!con.getHeaderFields().get(LOCATION_HEADER).toString().isEmpty()) {
            locationUrl = con.getHeaderFields().get(LOCATION_HEADER).toString()
                .substring(1, con.getHeaderFields().get(LOCATION_HEADER).toString().length() - 1);
        }

        //print result
        System.out.println(response.toString());

        if (response.toString().contains("ns3:link")) {
            latestHyperMediaLinks = retrivePostHyperMediaLinks(response.toString());
        }

    }

    // HTTP PUT request
    public void sendPut(final String url, final String body) throws Exception {
        obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(PUT);

        //add reuqest header
        con.setRequestProperty(CONTENT_TYPE_HEADER, RESTBUCKS_CONTENT_TYPE);
        con.setRequestProperty("Accepts", RESTBUCKS_CONTENT_TYPE);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();

        responseCode = con.getResponseCode();
        System.out.println(SENDING_REQUEST_LOG + url);
        System.out.println(BODY_LOG + body);
        System.out.println(RESPONSE_CODE_LOG + responseCode);

        in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(RESPONSE_LOG_HEADER);
        System.out.println(CODE_LOG_HEADER+con.getResponseCode());
        System.out.println(HEADERS_LOG_HEADER);

        logHeadersToConsole(con.getHeaderFields());

        //print result
        System.out.println(response.toString());

        if (response.toString().contains("link")) {
            latestHyperMediaLinks = retrivePutHyperMediaLinks(response.toString());
        }
    }

    private static void logHeadersToConsole(final Map<String, List<String>> map){
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                + " ,Value : " + entry.getValue());
        }
    }

    private static List<String> retrivePostHyperMediaLinks(final String response) {
        String tempResponse = response.substring(response.indexOf("dap\">")+5, response.indexOf("<ns2:name"));

        List<String> hyperMediaLinks = new ArrayList<>();

       while (!tempResponse.isEmpty()) {
           hyperMediaLinks.add(tempResponse.substring(tempResponse.indexOf("<ns3:link"),tempResponse.indexOf("/>")+2));
           tempResponse = tempResponse.replace(tempResponse.substring(tempResponse.indexOf("<ns3:link"),tempResponse.indexOf("/>")+2), "");
       }

       return hyperMediaLinks;
    }

    private static List<String> retrivePutHyperMediaLinks(final String response) {
        String tempResponse = response.substring(response.indexOf("<link"), response.indexOf("<ns2:amount"));

        List<String> hyperMediaLinks = new ArrayList<>();

        while (!tempResponse.isEmpty()) {
            hyperMediaLinks.add(tempResponse.substring(tempResponse.indexOf("<link"),tempResponse.indexOf("/>")+2));
            tempResponse = tempResponse.replace(tempResponse.substring(tempResponse.indexOf("<link"),tempResponse.indexOf("/>")+2), "");
        }

        return hyperMediaLinks;
    }

    public static String getLocationUrl() {
        return locationUrl;
    }

    public List<String> getLatestHyperMediaLinks() {
        return latestHyperMediaLinks;
    }

}