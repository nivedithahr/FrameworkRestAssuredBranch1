import com.workspacePayload.pojo.Workspace;
import com.workspacePayload.pojo.WorkspaceRoot;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.*;
public class FINAL_FRAMEWORK_TEST {
    RequestSpecification requestSpecification;
    @BeforeClass
    public void ExecutingUrlHeaderBeforeEveryTest()
    {
        requestSpecification = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-623310aeb3148544e5c50cfb-81ebf9db0aa47d51ca5146639a76e27cf3");
    }
    //1. FETCH THE DATA BY BDD FORMAt
    @Test
    public void FetchUserWorkspaceList(){
        given(requestSpecification).

                when().
                get("/workspaces").

                then().
                log().all().
                assertThat().statusCode(200);
    }
    //2. USING ASSERT_THAT TO CHECK RESPONSE BY NON BDD
    @Test
    public void FetchingByNonBDDFormat()
    {
        Response response = requestSpecification.get("/workspaces").
                then().
                log().all().
                extract().
                response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }
    //2.VALIDATE THE DATA USING HAMCREST
    @Test
    public void UseHamcrestForValidatingData(){
        given(requestSpecification).

                when().
                get("/workspaces").

                then().
                log().all().
                assertThat().statusCode(200).
                //using hamcrest huge collection
                body("workspaces.name", hasItems("My Workspace","myWorkSpace"),
                        "workspaces.type", hasItems("personal", "personal"),
                        "workspaces[0].name", equalTo("My Workspace"),
                        "workspaces[0].name",is(equalTo("My Workspace")),//more readable than above
                        "workspaces.size()", equalTo(2),//getting size of array
                        "workspaces.name", hasItem("My Workspace")).//to check particular item
                body("workspaces.name", contains("My Workspace", "myWorkSpace")).
                body("workspaces.name", containsInAnyOrder("myWorkSpace","My Workspace"),
                        "workspaces.name", is(not(emptyArray())),
                        "workspaces.name", hasSize(2),//another method to get
                        //"workspaces.name", everyItem(containsString("work")),
                        "workspaces[0]", hasKey("id"),
                        "workspaces[0]", hasValue("My Workspace"),
                        "workspaces[0]", hasEntry("id", "b7abdd3f-7197-4503-8abf-7e2be1401abb"),
                        "workspaces[0]",not(equalTo(Collections.EMPTY_MAP)),
                        "workspaces[0].name", allOf(startsWith("My"),containsString("Workspace")));
    }
    //3. VALIDATE LOGIN REQUEST AND RESPONSE
    @Test
    public void ValidatingLoginRequestAndResponse(){
        given(requestSpecification).

                config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
                config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key"))).
                log().all().

                when().
                get("/workspaces").

                then().
                log().body().
                assertThat().statusCode(200);

    }
    //4. CREATED JSON FILE IN RESOURCES. FETCHING AND VALIDATING DATA FROM CREATED JSON FILE
    //USING REGEX HERE FOR PATTERN VALIDATION
    @Test
    public void FetchingValidatingByJsonFile(){
        File file = new File("src/main/resources/workspace.json");

        given(requestSpecification).

                body(file).
                log().all().

                when().put("/workspaces/b7583ee8-e9a0-4ed1-bbb5-b7abef5155cb" ).

                then().
                log().all().
                assertThat().
                body("workspace.name", equalTo("SecFirstWorkspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));//USING REGEX TO VALIDATE ID PATTERN
    }
    //5. MULTIPLE USER CREATED BY POJO AND THEN VALIDATED FOR THEIR PRESENCE
    //6. NEW REGISTER AND NEW LOGIN IS SUCCESSFUL AND CAN FIND NEW USER IN THE WORKSPACE
    @Test(dataProvider = "workspace")
    public void CreatingMultipleUserByPOJOA_AndValidating(String name, String type, String description) {
        Workspace workspace = Workspace.builder().name(name).type(type).description(description).build();


        given(requestSpecification).
                body(workspaceRoot).
                log().all().

                when().
                post("/workspaces").


                then().
                log().all().
                assertThat();
    }

    @DataProvider(name = "workspace")
    public Object[][] getWorkspace(){
        return new Object[][]{
                {"myWorkspace 11", "personal", "description"},
                {"myWorkspace 12", "personal", "description"}

        };
    }
    //6. LOGIN VALIDATION NEW USER
    //REST UTIL
    @Test
    public void keyValueValidate() {


        String url = "/workspaces/b7583ee8-e9a0-4ed1-bbb5-b7abef5155cb";
        String key = "id";
        String value = "b7583ee8-e9a0-4ed1-bbb5-b7abef5155cb";
        RestUtils restUtils = new RestUtils();

        Map<String,String > map = new HashMap<String, String>();
        map.put(key,value);
        Response response = restUtils.getWithParams(url,map);
        response.prettyPrint();
    }
    @Test
    public void IdValue()
    {
        String url = "https://reqres.in/api/users";
        String key = "id";
        int value = 10;
        RestUtils restUtils = new RestUtils();

        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put(key,value);
        Response response = restUtils.getId(url,map);
        //system.out.println(response.body().asString());
        response.prettyPrint();
    }
}
