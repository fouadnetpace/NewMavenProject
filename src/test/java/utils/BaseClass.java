package utils;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class BaseClass {

    protected CloseableHttpClient client;
    protected CloseableHttpResponse response;


    protected static final String BASE_ENDPOINT_INVENTA = ApplicationConfiguration.getBaseURL_InventaService();
    protected static final String BASE_ENDPOINT_ADAPTER = ApplicationConfiguration.getBaseURL_AdapterService();

    //For Token
    public static final String SUBJECT = ApplicationConfiguration.getSubject();
    public static final int EXPIRATION_TIME = Integer.parseInt(ApplicationConfiguration.getExpirationTime());
    public static final String SECRET = ApplicationConfiguration.getSecretKey();

    protected static final String token = JWT.create()
            .withSubject(SUBJECT)
            .withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));


    /**
     * Random Number Generation
     */
    static Random rand = new Random();
    protected static final int value = rand.nextInt(5000);

    protected static final String randomString = RandomStringUtils.randomAlphabetic(8);



    /**
     * ID Extraction Implementation
     */

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    //Changes - Add Token implementation in this function
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        URL url1 = new URL(url);
        URLConnection uc = url1.openConnection();

        String basicAuth = "Bearer " + token;

        uc.setRequestProperty ("Authorization", basicAuth);
        InputStream is = uc.getInputStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String getIdFromURL(String url) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // just need one
        String json = readJsonFromUrl(url).toString();
        Map<String,Object> map = mapper.readValue(json, Map.class);
        System.out.println(((Map)((List)((Map)map.get("data")).get("content")).get(0)).get("_id"));

        return ((Map)((List)((Map)map.get("data")).get("content")).get(0)).get("_id")+"";

    }
    public static String getIdFromRolePermissionURL(String url) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // just need one
        String json = readJsonFromUrl(url).toString();
        Map<String,Object> map = mapper.readValue(json, Map.class);
        System.out.println (((Map)((List)map.get("data")).get(3)).get("_id"));

        return ((Map)((List)map.get("data")).get(3)).get("_id")+"";

    }
    public static String getNameFromSaveQueryWizardURL(String url) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // just need one
        String json = readJsonFromUrl(url).toString();
        Map<String,Object> map = mapper.readValue(json, Map.class);
        //System.out.println(((Map)((List)((Map)map.get("data")).get("content")).get(0)).get("name"));

        return ((Map)((List)((Map)map.get("data")).get("content")).get(0)).get("name")+"";
    }

    public static String DEVICE_DETAIL_ID = "";
    public static String USER_ID = "";
    public static String PR_ID = "";
    public static String ROLE_ID = "";
    public static String PERMISSION_ID = "";
    public static String ADMIN_USER_ID = "";
    public static String SAVED_DEVICE_QUERY_NAME = "";
    public static String SAVED_USER_QUERY_NAME = "";
    public static String AUDIT_DETAIL_ID = "";
    public static String DELETE_PR_ID = "";
    public static String DELETE_ADMIN_USER_ID = "";



    static {
        try {

            DEVICE_DETAIL_ID = getIdFromURL("http://inventaserver:9092/devices/getAllDevices?page=0&size=1&sortBy=_id");
            USER_ID = getIdFromURL("http://inventaserver:9092/users/getAllUsers/?page=0&size=1");
            PR_ID = getIdFromURL("http://inventaserver:9092/policy-routine/?page=0&size=1&sort=dateCreated,desc"); //Update Parameter in Endpoint
            ROLE_ID = getIdFromRolePermissionURL("http://inventaserver:9092/role/getAllRole?page=0&size=1");
            PERMISSION_ID = getIdFromRolePermissionURL("http://inventaserver:9092/permission/getAllPermission?page=0&size=1");
            ADMIN_USER_ID = getIdFromURL("http://inventaserver:9092/adminUsers/getAllAdminUsers?page=0&size=1&sort=dateCreated,desc"); // Update Parameter in Endpoint
            SAVED_DEVICE_QUERY_NAME = getNameFromSaveQueryWizardURL("http://inventaserver:9092/saved-query/?type=DEVICE&page=0&size=1");
            SAVED_USER_QUERY_NAME = getNameFromSaveQueryWizardURL("http://inventaserver:9092/saved-query/?type=USER&page=0&size=1");
            AUDIT_DETAIL_ID = getIdFromURL("http://inventaserver:9092/audit/getAllAudit?page=0&size=1");
            DELETE_PR_ID = getIdFromURL("http://inventaserver:9092/policy-routine/?page=0&size=1&sort=dateModified,desc"); //add new endpoint
            DELETE_ADMIN_USER_ID = getIdFromURL("http://inventaserver:9092/adminUsers/getAllAdminUsers?page=0&size=1&sort=dateModified,desc"); //add new endpoint

            //http://inventaserver:9092/devices/getAllDevices?page=0&size=1&sort=firstFetchTime,desc

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
