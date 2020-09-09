package TCPSServer;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp extends Thread {
    public static final int PORT = 6666;
    public static ServerSocket serverSocket = null; // Server gets found
    public static Socket openSocket = null;         // Server communicates with the client

/*    public static Socket configureServer() throws UnknownHostException, IOException
    {
        // get server's own IP address
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Server ip: " + serverIP);

        // create a socket at the predefined port
        serverSocket = new ServerSocket(PORT);

        // Start listening and accepting requests on the serverSocket port, blocked until a request arrives
        openSocket = serverSocket.accept();
        System.out.println("Client connected: " + openSocket);

        return openSocket;
    }

    public static void connectClient(Socket openSocket) throws IOException
    {
        String request, response;

        // two I/O streams attached to the server socket:
        Scanner in;         // Scanner is the incoming stream (requests from a client)
        PrintWriter out;    // PrintWriter is the outcoming stream (the response of the server)
        in = new Scanner(openSocket.getInputStream());
        out = new PrintWriter(openSocket.getOutputStream(),true);
        // Parameter true ensures automatic flushing of the output buffer

        // Server keeps listening for request and reading data from the Client,
        // until the Client sends "stop" requests
        while(in.hasNextLine())
        {
            request = in.nextLine();

            if(request.equals("stop"))
            {
                out.println("Good bye, client!");
                System.out.println("Log: " + request + " client!");
                break;
            }
            else
            {
                // server responses
                response = new StringBuilder(request).reverse().toString(); //Reverse the response text to string and return it to the client
                out.println(response);
                // Log response on the server's console, too
                System.out.println("Log: " + response);
            }
        }
    }*/

    public static void main(String[] args) throws IOException
    {
        //openSocket = configureServer();

        // get server's own IP address
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Server ip: " + serverIP);

        // create a socket at the predefined port
        serverSocket = new ServerSocket(PORT);

        while(true) {
            try
            {
                openSocket = serverSocket.accept(); //allow client to connect
                System.out.print("Client connected: " + openSocket);

                DataInputStream dis = new DataInputStream((openSocket.getInputStream()));
                DataOutputStream dop = new DataOutputStream(openSocket.getOutputStream());

                System.out.println("Assigned thread for client: " + openSocket);
                Thread t = new ClientHandler(openSocket, dis, dop);
                t.start();

                System.out.println(java.lang.Thread.activeCount());
            }
            catch(Exception e)
            {
                System.out.println(" Connection fails: " + e);
                openSocket.close();
            }
        }
    }
}

class ClientHandler extends Thread {
    final Socket openSocket;
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;


    // Constructor
    public ClientHandler(Socket openSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.openSocket = openSocket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run()  {

            String request, response; //two I/O streams attached to the server socket, request and response (input and output):
            Scanner in = new Scanner(dataInputStream); //The scanner is the incoming stream (the requests from the client)
            PrintWriter out = new PrintWriter(dataOutputStream, true); // PrintWriter is the outcoming stream (the response the client get back from the server).
            //Autoflush: true automatically flush the output buffer.

            // Server keeps listening for request and reading data from the client untill the "stop" command is sent
            while (in.hasNextLine()) {
                request = in.nextLine();

                if (request.equals("stop")) {
                    out.println("See you later, client!!");
                    System.out.println("[Log]\t" + "Client: " + openSocket.getPort() + "\t\tMessage: " + request);
                    break;
                } else {
                    // server responses
                    response = new StringBuilder(request).reverse().toString(); //Reverse the response text to string and return it to the client
                    out.println(response);
                    // Log response on the server's console, too
                    System.out.println("[Log]\t" + "Client: " + openSocket.getPort() + "\t\tMessage: " + response);
                }
            }
        }

}
