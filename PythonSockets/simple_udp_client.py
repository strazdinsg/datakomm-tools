from socket import *

message = input("Enter a message to be sent: ")  # Read from keyboard
client_socket = socket(AF_INET, SOCK_DGRAM)
client_socket.sendto(message.encode(), ("localhost", 9876))
response, server_addr = client_socket.recvfrom(100)
print("FROM SERVER:", response.decode())
client_socket.close()
