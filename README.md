Appium is an open-source tool for automated testing of web, native, and hybrid applications. The Appium cross-platform nature allows writing tests on iOS and Android platforms using the same API. Thanks to the server-client architecture, a client written in any language can be used to send appropriate HTTP requests to the server.

In Appium, a web server using REST API receives connections with the client, listens for commands, and also executes them on a given mobile device. The results of executing a given command are visible thanks to the reaction to HTTP responses. Not having to recompile the application for automation is a big advantage of this tool. Moreover, due to the widespread use of Selenium WebDriver, Appium has extended its API with specific methods of testing mobile applications (e.g. MobileElement, AndroidElement, IOSElement).


Automated tests in Appium: an example
The example I will present here is an automated test in the Sauce labs demo application, which will check valid and invalid username and password login. Using the Appium tool to write automated tests requires the following tools:

Java,

Android SDK,

Appium,

A device or an emulator with Android or iOS (connect the physical device to the computer using a USB cable or WiFi),

An application to test(android or iOS app).

In the first step after starting Appium, the connection parameters should be passed in the application options.


The device on which I carry out tests is an emulator created in Android Studio, running on Android version 9.0. In the Android Studio tool, we create a new project where we define the connection parameters to the Appium server. Then we initialise the Appium driver. Here I am initialising the Appium driver for Android and iOS devices. For this I am using switch() statement which can help me with multiple cases.


 

When creating automated tests, we use the Page Object Model (POM) scheme. While writing scripts, testers often encounter problems related to the changes in the tested software. To facilitate the process of creating scripts and prevent such problems, it is worth using the Page Object Model pattern. This scheme separates the elements and methods on the page from the tests. Thanks to this approach, the tests are easier to implement and much simpler to maintain later on.

The class where we initialise elements and perform operations on them:


Here I have created method name enterUserName(String username) , enterPassword(String password) which is  entering username and password  in the input field for the respective test.


 

Each test ends with an assertion checking whether the condition has been met. The test class is as follows:

To run the test, we use the @Test annotation from the TestNG library.


How to develop first Appium test?
Open IntelliJ IDEA.

Create a Maven project.

Expand src/test folder.

Right click on the test folder and add a new Java Class.


Adding the test class

Provide a suitable name for the class. For this tutorial, I will give “LandingPageTest” as the test class name.

After creating the class, you can write the test cases as per the scenario.

How to run the test?
Open Appium Desktop Client.

Provide host address as “127.0.0.1” and port as “4723”.


Setting up Appium host and port

Click on “Start Server” button.

Now Appium server is up and running.

Now we need to check for the testng.xml file and click Run icon present at the top.                             Note: Before running test cases, please update testng.xml

We need to pass parameters for our test based on the device config.

If we need to run the test cases in different device we need to updated UDID , deviceName in the testng.xml file.

If we need to run the device in emulator we need to pass parameter for emulator as true.


 

Executing Appium tests

You will see tests are running on the mobile device.


 

Reporting:
Each test is reported using Extent Reports. This is a fully customisable HTML report that can be integrated with Selenium WebDriver. Extent Reports are very easy to implement and incorporate in our test cases. The information, test log for the test cases are well documented in Extent Report.


 

Feature Included in Appium
Using Appium Desktop To Locate Elements
Appium provides you with a neat tool that allows you to find the the elements you're looking for. With Appium Desktop you can find any element and its locators by either clicking the element on the screenshot image, or locating it in the source tree.

Appium Desktop has a simple layout, complete with a source tree, a screenshot, and record and refresh buttons, and interaction tools.

How to Inspect a session to get locators?
Open Appium Desktop Client.

Provide host address as “127.0.0.1” and port as “4723”.


Setting up Appium host and port

Click on “Start Server” button.

Now Appium server is up and running.

Click on “Start Inspector Session”


Inspecting an application — 1

Click on “Desired Capabilities”


Inspecting an application — 2

Add desired capabilities according to your application and mobile device.


Inspecting an application — 3

Click on “Start Session” button.

Once Inspect session is loaded, you will see the application on the mobile screen.


Inspecting an application — 4

10. When you click on an element, you can see the locator of that element.


Conclusion: Appium as a mobile automation testing tool
The most important advantages of Appium are undoubtedly its cross-platform and open-source nature. Carrying out automated tests of mobile applications as well as web applications significantly improves the software development process and enables the actual application of the CI/CD approach. For people starting to work with this tool, the large amount of training materials and tutorials available on the internet about this tool is a great advantage.
