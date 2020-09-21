from random import randint
from socket import *
from time import sleep

from socket_library import read_one_line

messages = ["DataKomm rulz!", "Hello from TCP", "Chuck is here", "...", "ABC", ""]


def choose_random_message():
    i = randint(0, len(messages) - 1)
    return messages[i]


def start_client():
    client_socket = socket(AF_INET, SOCK_STREAM)
    client_socket.connect(("localhost", 5678))
    need_to_run = True
    while need_to_run:
        command_to_send = choose_random_message()
        command_to_send += "\n"
        client_socket.send(command_to_send.encode())
        server_response = read_one_line(client_socket)
        print("Server's response: ", server_response)
        sleep(1)
        # Stop the client when server responds with an empty line
        if server_response == "":
            need_to_run = False

    client_socket.close()


def process_response(one_line):
    # Business logic for processing of one server response
    print("Response from the server:")
    print(one_line)


if __name__ == '__main__':
    start_client()
