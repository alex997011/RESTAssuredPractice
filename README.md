# 🚀 Automation Framework with Rest Assured

For this practice, we will implement 4 test cases for an API in a development environment

# 💻Technology Stack

1. Java
2. Maven
3. RestAssured
4. Pogo
5. Cucumber

# 🧰 Development Tools & Services
- MockAPI - API mocking and simulation


# 📦Dependencies

1. io.cucumber
2. org.projectlombok
3. com.google.code.gson
4. io.rest-assured
5. org.apache.logging.log4j
6. org.junit.jupiter
7. org.jetbrains

## UTILS PACKAGE
### CONSTANTS

The project uses MockAPI for simulating the API. You can either:
- Use the existing base URL: `https://6748a60c5801f5153591bba0.mockapi.io`
- Replace it with your own MockAPI URL in `utils/constants.js`

### ENDPOINTS
const CLIENTS_PATH = '/Clients';
const RESOURCES_PATH = '/Resources';

You can keep these URLs or replace them with your own MockAPI endpoints. The paths (CLIENTS_PATH, RESOURCES_PATH) should match your MockAPI resource configuration, whether using the existing base URL or your custom one.

### MockAPI Schema
For proper functionality, your MockAPI should follow this schema:
CLIENTS

![image](https://github.com/user-attachments/assets/2638c23d-8643-43e3-b30a-5c39fb01b9fe)

![image](https://github.com/user-attachments/assets/0b2b2547-e7ec-4cff-a73c-ffa38a44d225)

RESOURCES

![image](https://github.com/user-attachments/assets/dfb73c1e-5134-4cef-8900-b70b8985f8db)

![image](https://github.com/user-attachments/assets/8a8c6e64-4ffc-4ba8-a9fa-4fea26e2f749)






