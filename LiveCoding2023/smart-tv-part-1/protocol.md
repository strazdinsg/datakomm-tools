# Protocol

Message are separated by newline.

The client always sends a request first, the server responds.
There can be several requests/responses during one connection.

The following requests could be sent from client to server:

* "1" turn on the TV
* "0" turn off the TV
* "c" get the number of channels
* "s###" set current channel, where ### is the desired channel number, as a string, can be one
  or several bytes, up to the newline ("s1", "s25", ...)

Server can respond with:

* "o"  - performed the necessary operation
* "c###"  - report the number of channels, where ### is the number of channels, as a string, 
  can be several bytes, until the newline
* "eM"  - if the operation failed, where M is an error message - string until the newline. For
  example, if the error message is "Invalid channel number", the response will be
  "eInvalid channel number"