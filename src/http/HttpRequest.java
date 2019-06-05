package http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static http.HttpConstants.*;

public class HttpRequest {

    protected static String locationUrl = "";
    URL obj;
    HttpURLConnection con;
    int responseCode;
    BufferedReader in;

    // HTTP GET request
    public void sendGet(final String requestUrl) throws Exception {

        obj = new URL(requestUrl);
        con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(GET);

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

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

    }

    // HTTP POST request
    public void sendPost(final String url) throws Exception {
        obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(POST);

        //add reuqest header
        con.setRequestProperty("Content-Type", "application/vnd.restbucks+xml");

        String urlBody = "<order xmlns=\"http://schemas.restbucks.com\">\n" +
            "  <name>Josh</name>\n" +
            "  <item>\n" +
            "    <milk>semi</milk>\n" +
            "    <size>large</size>\n" +
            "    <drink>americano</drink>\n" +
            "  </item>\n" +
            "  <location>takeaway</location>\n" +
            "</order>";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlBody);
        wr.flush();
        wr.close();

        responseCode = con.getResponseCode();
        System.out.println(SENDING_REQUEST_LOG + url);
        System.out.println(BODY_LOG + urlBody);
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

        locationUrl = con.getHeaderFields().get(LOCATION_HEADER).toString()
            .substring(1, con.getHeaderFields().get(LOCATION_HEADER).toString().length()-1);

        //print result
        System.out.println(response.toString());

    }

    private static void logHeadersToConsole(final Map<String, List<String>> map){
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                + " ,Value : " + entry.getValue());
        }
    }

    public static String getLocationUrl() {
        return locationUrl;
    }

}