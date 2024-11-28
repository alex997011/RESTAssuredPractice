# 🚀 Automation Framework with Rest Assured

For this practice, we will implement 4 test cases for an API in a development environment

# 💻Technology Stack

1. Java
2. Maven
3. RestAssured
4. POJO
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

## TEST CASES

### 1

![image](https://github.com/user-attachments/assets/93e39c7a-5f9f-4e74-9f9b-4db5b991fbe0)

![image](https://github.com/user-attachments/assets/5d2c6b78-d0b9-453d-ad86-54e67d4435d6)

### 2

![image](https://github.com/user-attachments/assets/97118041-67fc-48da-a828-1f4ec3accec2)

![image](https://github.com/user-attachments/assets/767f8cbc-16c3-4c25-95e9-a6fc7f0f270e)

### 3

![image](https://github.com/user-attachments/assets/e02c2326-0197-43c1-9fef-4b04426430d1)

![image](https://github.com/user-attachments/assets/61ebfe92-d040-4744-9216-d0da1ee4cfd0)

### 4

![image](https://github.com/user-attachments/assets/a2145a22-1228-41eb-aa75-0d706505befc)

![image](https://github.com/user-attachments/assets/61a36846-c1c2-4b1b-8240-e4e0e794053a)

# ⚠️ MockAPI Test Data Setup

#### Test Case 1
For proper testing, configure your MockAPI's Clients resource with:
- At least 10 random clients 
- Multiple clients named "Laura" (modify only the name field)

EXAMPLE: 

![image](https://github.com/user-attachments/assets/0fe4eb84-2688-4905-8333-6a04474dda92)


















