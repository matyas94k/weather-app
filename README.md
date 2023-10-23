# weather-app
Project serving weather forecast requests for specific cities.

### Usage
- **GET** [host]/api/weather?city=[list]
- The port (*4094*) and the supported cities are specified in the *application.properties* file.
- After successfully serving a **GET** request, the result will be also saved in a CSV file.

### Used technologies
- Java 21
- Spring Boot, with minimal addition of dependencies, such as:
- Spring Web
- Spring Test

#### Author: Mátyás
Feel free to review my code and add comments or suggestions.