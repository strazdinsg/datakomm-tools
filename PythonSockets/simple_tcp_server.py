from socket import *

welcome_socket = socket(AF_INET, SOCK_STREAM)
welcome_socket.bind(("", 5678))
welcome_socket.listen(1)
connection_socket, addr = welcome_socket.accept()
client_message = connection_socket.recv(1000).decode()
print("CLIENT: ", client_message)
response = client_message.upper()
connection_socket.send(response.encode())
connection_socket.close()
welcome_socket.close()

