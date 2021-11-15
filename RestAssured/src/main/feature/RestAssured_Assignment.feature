Feature:Basic Rest Assured Practice

  # API to send request and receive response for GET,POST,DELETE and UPDATE methods
  Scenario Outline: Validate API methods using Rest Assured
    Given Create a request "<URI>"
    When  send "<Method>" request with body to server "<API>" with "<TestData>"
    Then Validate response Status Code "<Status>"
    Examples:
    |URI|Method|API|TestData|Status|
    |https://reqres.in|GET   |/api/users?page=2||200|
    |https://reqres.in|POST  |/api/users|D:\Data_Backup\RestAssured\src\main\resources\testdata\post.json|201|
    |https://reqres.in|UPDATE|/api/users/2|D:\Data_Backup\RestAssured\src\main\resources\testdata\put.json|200|
    |https://reqres.in|DELETE|/api/users/2||204|

   #send response to file
  Scenario: Send response to file example 2
    Given Create a request "https://reqres.in"
    When  send request with body to server "/api/users?page=2"
    Then send response to file

  #/employees{id} - get method to get employee details
  Scenario: Validate GET method in Rest Assured example 1
    Given Create a request "http://dummy.restapiexample.com/api/v1"
    When  send request with body to server "/employees"
    Then Validate response Status Code 200

  Scenario: Validate GET method response details in Rest Assured example 1
    Given Create a request "http://dummy.restapiexample.com/api/v1"
    When  send request with body to server "/employee/1"
    Then Validate response header details

    #Response Header validation
  Scenario: Validate GET method response header details in Rest Assured example 2
    Given Create a request "https://reqres.in"
    When  send request with body to server "/api/users?page=2"
    Then Validate response header details

    #response body details using JSONObject
  Scenario: Validate GET method response details in Rest Assured example 2
    Given Create a request "https://reqres.in"
    When  send request with body to server "/api/users?page=2"
    Then Validate response body details

  Scenario: Serialize and Deserialize JSONObject example 2
    Given Create a request "https://reqres.in"
    When  send request with body to server "/api/users?page=2"
    Then serialize and deserialize JSONObject
