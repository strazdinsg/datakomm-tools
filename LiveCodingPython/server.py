from socket import *
from socket_library import read_one_line
import threading


def handle_next_client(connection_socket, client_id):
    command = "Something"
    while command != "":
        command = read_one_line(connection_socket)
        print("Client #%i: %s" % (client_id, command))
        if command == "ping":
            response = "PONG"
        else:
            response = command.upper()
        response += "\n"  # Server must respond with a line of text
        connection_socket.send(response.encode())

    connection_socket.close()


def start_server():
    welcome_socket = socket(AF_INET, SOCK_STREAM)
    welcome_socket.bind(("", 5678))
    welcome_socket.listen(1)
    print("Server ready for client connections")
    need_to_run = True
    client_id = 1

    while need_to_run:
        connection_socket, client_address = welcome_socket.accept()
        print("Client #%i connected" % client_id)
        client_thread = threading.Thread(target=handle_next_client,
                                         args=(connection_socket, client_id))
        client_thread.start()
        client_id += 1

    welcome_socket.close()
    print("Server shutdown")


if __name__ == "__main__":
    start_server()
