README

I. File list
------------
Included:
 - All source files (.src, .class, .war, .xml)
 - README
 - build.xml : ANT file
 - Directory Structure
    -- src/ 
     ---- movierest/
            ---- MovieREST.java
            ---- Movie.java
    -- war/
    -- WebContent/
     ---- META-INF/
     ---- WEB-INF/
            ---- lib/
            ---- web.xml
     ---- jquery-1.11.2.min.js
     ---- MovieForm.html

            ---- MovieREST.java
            ---- Movie.java

    -- classes/

II. To run
----------
- Navigate to the program folder
{Eg: /wherever_the_program_is_stored/RESTfulWebService}

- Execute the following command: ant make_war
    - This will compile all the source files, external libraries and put the class files into 'classes/' directory.
    - It will create a .war of the program and store it in war/ folder)

- You will find the war file in:
{Eg: /wherever_the_program_is_stored/RESTfulWebService/war/MovieREST.war}
 
- To run the program, execute: java -jar jar/WebServiceClient.jar 
(WebServiceClient: This the Main class)

- To run the program, execute: java -jar jar/WebServiceClient.jar 
(WebServiceClient: This the Main class)
