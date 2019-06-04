import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.sun.deploy.util.StringUtils;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String getRequestUrl = "";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("\n>>>Testing 1 - Send Http POST request - Creating order");
        http.sendPost();

        System.out.println("\n\n\n>>>Testing 2 - Send Http GET request - Checking order");
        http.sendGet(getRequestUrl);

    }


    // HTTP GET request
    private void sendGet(final String requestUrl) throws Exception {

        URL obj = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + requestUrl);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("\n\nRESPONSE>>>");
        System.out.println("CODE>>> "+con.getResponseCode());
        System.out.println("HEADERS>>> ");
        Map<String, List<String>> map = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                + " ,Value : " + entry.getValue());
        }

        //print result
        System.out.println(response.toString());

    }


    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "http://10.0.0.244:8080/order";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

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

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post body : " + urlBody);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("\n\nRESPONSE>>>");
        System.out.println("CODE>>> "+con.getResponseCode());
        System.out.println("HEADERS>>> ");

        Map<String, List<String>> map = con.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                + " ,Value : " + entry.getValue());
        }
        getRequestUrl = map.get("Location").toString().substring(1, map.get("Location").toString().length()-1);

        //print result
        System.out.println(response.toString());

    }

}
