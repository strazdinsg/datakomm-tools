from socket import *

server_socket = socket(AF_INET, SOCK_DGRAM)
server_socket.bind(("", 9876))

msg_bytes, client_address = server_socket.recvfrom(1000)
client_message = msg_bytes.decode()
print("CLIENT: ", client_message)
response = client_message.upper()
server_socket.sendto(response.encode(), client_address)
server_socket.close()
