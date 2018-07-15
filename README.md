# Task Manager

**This is just a skeleton app meant to demonstrate the api created in the task_manager php project. 
Not meant for production.** 

### API Key
* Create a String variable in gradle.properties called "TEST_API_KEY" and set it equal to on of
the keys generated for one of your users in your database. 

### Server
* Copy your server's .crt file into the project's assets directory and name it "server.crt".
* Create a String variable equal to your server's LAN IP address in gradle.properties and call it
 "TEST_SERVER_IP_ADDRESS" 
 
### App Signing
* Store the .jks file in the root directory of this project to sign a version of the app
* Create a keystore.properties file in the root directory which includes:
  * storePassword=yourPassword
  * keyPassword=yourOtherPassword
  * keyAlias=aliasName
  * storeFile=yourKeystoreName.jks