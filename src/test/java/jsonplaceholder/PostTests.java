package jsonplaceholder;

import client.RestClient;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.jsonplaceholder.Post;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PostTests {
    private static final Logger logger = LoggerFactory.getLogger(PostTests.class);

    private static final String BASE_URL = "https://gorest.co.in";

    private static final String USER_ENDPOINT = BASE_URL + "/public-api/users";

    //Please enter your access token here
    private static final String AccessToken = "Access_Token";

    private RestClient client = new RestClient(AccessToken);


    @Test
    public void testCreateUpdateDelete() throws IOException {

        String name = "Mohsen Test00016";
        String gender = "Male";
        String email = "mohsen0016@example.uk";
        String status = "Inactive";

        // creating the endpoint
        String addUserEndPoint = USER_ENDPOINT + "?name=" + name + "&gender=" + gender + "&email=" + email + "&status=" + status;

        //----------------------------created User-------------------------------

        logger.info(() -> "Creating a new User");

        //Check if the access token is given
        assertNotEquals("Access_Token", AccessToken,"Please enter a correct Token !");

        //Send request and create json object out of response
        Response response = client.post(addUserEndPoint, Response.class);
        String responseString = response.getBody().asString();

        JSONObject jso =  (JSONObject) JSONValue.parse(responseString);

        //Converting the user-data json String to Post object
        JSONObject jsoData = (JSONObject) jso.get("data");
        String jsResp = jsoData.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Post post = objectMapper.readValue(jsResp, Post.class);

        logger.info(() -> "Created user Ifo: " + post.toString() );

        //Evaluate the response code
        long code = (long) jso.get("code");
        if(code == 201)
        {
            logger.info(() -> "User Created Successfully !");
        }
        else if( code == 422 ) {
            logger.error(() -> "User Already Exists !");
            return;
        }
        else
        {
            logger.error(() -> "User Creation Failed !");
            return;
        }

        // the code must be 201 to show it was working well
        assertEquals(201, code);

        //----------------------------Verify the new created User----------------
        logger.info(() -> "Verifying the new created User : "+ post.getId());

        //send a get request with the userId
        String getUserEndPoint = USER_ENDPOINT + "/" + post.getId();
        response = client.get(getUserEndPoint, Response.class);

        JsonFactory factory = new JsonFactory();
        jso =  (JSONObject) JSONValue.parse(responseString);
        JsonParser parser = factory.createParser(jso.get("data").toString());


        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            parser.getCurrentName();

            if ("id".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getId(),parser.getValueAsInt());
            }
            if ("name".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getName(),parser.getValueAsString());
            }
            if ("email".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getEmail(),parser.getValueAsString());
            }
            if ("gender".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getGender(),parser.getValueAsString());
            }
            if ("status".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getStatus(),parser.getValueAsString());
            }
        }

        logger.info(() -> "Created User '"+ post.getId() + "' has been successfully verified.");

        //----------------------------Updating User------------------------------
        logger.info(() -> "Updating User : "+ post.getId());

        //giving a new name and status to the post Object
        post.setName("Tricia Takanawa");
        post.setStatus("active");

        logger.info(() -> "Updated user Ifo: " + post );

        String updateUserEndPoint = USER_ENDPOINT + "/" + post.getId() + "?name=" + post.getName() + "&status=" + post.getStatus();

        response = client.put(updateUserEndPoint, Response.class);
        responseString = response.getBody().asString();

        jso =  (JSONObject) JSONValue.parse(responseString);
        code = (long) jso.get("code");
        if(code == 200)
        {
            logger.info(() -> "User Updated Successfully !");
        }
        else
        {
            logger.error(() -> "User Updating Failed !");
            return;
        }
        assertEquals(200, code);

        //----------------------------Verify Updated User------------------------
        logger.info(() -> "Verifying User : "+ post.getId());
        //send a get request with the userId
         getUserEndPoint = USER_ENDPOINT + "/" + post.getId();
        response = client.get(getUserEndPoint, Response.class);

        factory = new JsonFactory();
        jso =  (JSONObject) JSONValue.parse(responseString);
        parser = factory.createParser(jso.get("data").toString());


        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            parser.getCurrentName();

            // Since only name and status updated, only verifying name and status

            if ("name".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getName(),parser.getValueAsString());
            }
            if ("status".equals( parser.getValueAsString())) {
                jsonToken = parser.nextToken();
                assertEquals(post.getStatus(),parser.getValueAsString());
            }
        }

        logger.info(() -> "Updated User '"+ post.getId() + "' has been successfully verified.");

        //----------------------------Deleting User------------------------------
        String deleteUserEndPoint = USER_ENDPOINT + "/" + post.getId();

        response = client.delete(deleteUserEndPoint, Response.class);
        responseString = response.getBody().asString();

        jso =  (JSONObject) JSONValue.parse(responseString);
        code = (long) jso.get("code");


        if(code == 204)
        {
            logger.info(() -> "User Deleted Successfully !");
        }
        else
        {
            logger.error(() -> "User Deleting Failed !");
            return;
        }
        assertEquals(204, code);

        //----------------------------Check User---------------------------------
        String checkUserEndPoint = USER_ENDPOINT + "/" + post.getId();

        response = client.get(checkUserEndPoint, Response.class);
        responseString = response.getBody().asString();

        jso =  (JSONObject) JSONValue.parse(responseString);
        code = (long) jso.get("code");

        jsoData = (JSONObject) jso.get("data");
        String message = jsoData.get("message").toString();
        assertEquals(404, code, "Resource not found");
        logger.info(() -> message+ ", user successfully deleted !");

    }

}
