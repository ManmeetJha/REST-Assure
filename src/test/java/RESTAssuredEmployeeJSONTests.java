import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;


public class RESTAssuredEmployeeJSONTests {
    private int empId;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4000;
        empId = 9;
    }

    public Response getEmployeeList() {
        Response response = RestAssured.get("/employees/getAll");
        return response;
    }

    @Test
    public void onCallingList_ResturnEmployeeList() {
        Response response = getEmployeeList();
        System.out.println("AT FIRST: " + response.asString());
        //response.then().body("id", Matchers.hasItems(2,3,4));
        response.then().body("name", Matchers.hasItems("Lisa"));
    }

    @Test
    public void givenEmployee_OnPost_ShouldReturnAddedEmployee() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\":\"Navneet\",\"salary\":\"8000\"}")
                .when()
                .post("/employees/create");
        System.out.println(response.asString());
    }

    @Test
    public void givenEmployee_OnUpdate_ShouldReturnUpdatedEmployee() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\":\"Lisa Tamaki\",\"salary\":\"20000\"}")
                .when()
                .put("/employees/updated/" + 5);

        response.then().body("name", Matchers.is("Lisa Tamaki"));
        response.then().body("salary", Matchers.is("20000"));
        System.out.println(getEmployeeList().asString());
    }

    @Test
    public void givenEmployee_OnDelete_ShouldReturnSuccessStatus(){
        Response response=RestAssured.delete("/employees/delete/"+3);
        System.out.println(getEmployeeList().asString());
        int statusCode=response.getStatusCode();
        MatcherAssert.assertThat(statusCode, CoreMatchers.is(200));
    }


}
