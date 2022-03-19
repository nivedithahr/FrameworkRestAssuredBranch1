import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class GOOGLE_OAUTH_GMAIL {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String access_token = "ya29.A0ARrdaM87UU44U_2PEezJpLxg0Ba7UPqRtJ0NmhxewmYiPI_znnXOPyETuAM7zUxpxMESC6H1bmhe3t9zGFrVJ4Z-jM34RewKwm629VlN-5HANWSlTyfYlS84Hm3jJtCwEqOyOPLZHbWkEggbGc84XjmJX-yIPg";
    @BeforeClass
    public void beforeClass()
    {
        RequestSpecBuilder requestSpec = new RequestSpecBuilder().
                setBaseUri("https://gmail.googleapis.com").
                addHeader("Authorization", "Bearer " + access_token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);

        requestSpecification = requestSpec.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();

    }
    //1.FETCHING ALL THE INBOX EMAIL AND THREADS
    @Test
    public void FetchingDataFromUserProfile(){
        given(requestSpecification).
                basePath("/gmail/v1").
                pathParams("userid", "hrniveditha2000@gmail.com").

                when().
                get("/users/{userid}/profile").

                then().spec(responseSpecification);
    }
    //2.SENDING EMAIL THROUGH GMAIL API AND VALIDATING
    @Test
    public void SendingMessage(){
        String msg = "From: hrniveditha2000@gmail.com\n" +
                "To: nivedithahrram2017@gmail.com\n" +
                "Subject: Test Email\n" + "\n" +
                "Sending from Gmail API";

        String base64Encoded = Base64.getUrlEncoder().encodeToString(msg.getBytes());
        HashMap<String, String> payload = new HashMap<>();
        payload.put("raw", base64Encoded);

        given(requestSpecification).
                basePath("/gmail/v1").
                pathParams("userid", "hrniveditha2000@gmail.com").
                body(payload).

                when().
                post("/users/{userid}/messages/send").

                then().spec(responseSpecification);
    }
}
