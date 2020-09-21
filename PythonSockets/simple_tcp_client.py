from socket import *

message = input("Enter a message to be sent: ")  # Read from keyboard
client_socket = socket(AF_INET, SOCK_STREAM)
client_socket.connect(("localhost", 5678))
client_socket.send(message.encode())
response = client_socket.recv(1000)
print("FROM SERVER: ", response.decode())
client_socket.close()

