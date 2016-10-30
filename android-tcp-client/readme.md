# Example TCP client in Android

This folder contains a test TCP server-client project where the TCP server is a standalone Java application (see `JsonCommandTcpServer` folder) and the TCP client is an Android application (See `AndroidJsonCommandTcpClient` folder). 

Messages in `JSON` format are exchanged between the server and the client. Each message is separated by a newline character (`\n`). The common structures used by both applications are packed as a separate library: `tcp-server-client-tools`. Use the `Message` class inside the `tcp-server-client-tools` library to form outgoing messages and parse the incoming ones. This library depends on some other libraries, found in this same repository, outside `android-tcp-client`: `tools-lib` and `json-org-lib`. 

The easiest way to run these applications would be to use Android Studio and Netbeans:
* Open the `json-org-lib`, `tools-lib`, `tcp-server-client-tools` and `JsonCommandTcpServer` as projects in Netbeans
* Build them in Netbeans using "Clean and Build". Build them in the order as they appear here in the list
* Run the Tcp server directly from Netbeans
* Open the `AndroidJsonCommandTcpClient` project in Android Studio, build it and run it - on a real device or in a simulator.

It is advised to use [GenyMotion Android emulator](https://www.genymotion.com/) - it runs faster than the default one.