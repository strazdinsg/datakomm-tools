# Protocol

Message are separated by newline.

The client always sends a request first, the server responds.
There can be several requests/responses during one connection.
However, the communication is asynchronous: the server can send a message (response) to the
client without receiving a request from the client in the first place. For example, when
multiple clients are connected and one of the clients request a channel change, all other
clients will be notified.

## The allowed operations

There are several supported operations.

### Turn on the TV

Turning on the TV happens as follows:

1. The client sends a "Turn ON" command, which is encoded as `1` in the socket.
2. The server sends a "TV ON" message to all the connected clients, which is encoded as `TVON`.

### Turn off the TV

Turning off the TV happens similar to turning the TV on:

1. The client sends a "Turn OFF" command, which is encoded as `0` in the socket.
2. The server sends a "TV OFF" message to all the connected clients, which is encoded as `TVoff`.

### Get the number of channels

Getting the number of channels happens as follows:

1. The client sends a "Get channel count" command, which is encoded as `c`.
2. The server responds with "Channel count message", which is encoded as `N###`, where ### is the
   number of channels, as a string, can be several bytes, until the newline.

### Get the current channel

Getting the current channel happens as follows:

1. The client sends a "Get channel" command, which is encoded as `g`.
2. The server sends a "Current channel" message to only this one client,
   which is encoded as `Current###`, where ### is the current channel encoded as a string.

### Set the current channel

Setting the current channel happens as follows:

1. The client sends a "Set channel" command, which is encoded as `s###`, where ### is the
   desired channel, encoded as a string.
2. The server sends a "Current channel" message to ALL the connected clients, which is encoded as
   `Current###`, where ### is the current channel encoded as a string.

## Error handling

If the request could not be processed (for example, the client asks to set a channel while the
TV is off), the server responds with an error message in the format `eM`, where M is an error
message - string until the newline. For example, if the error message is "Invalid channel number",
the response will be "eInvalid channel number"