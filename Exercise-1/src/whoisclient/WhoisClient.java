package whoisclient;

import java.net.InetAddress;
import java.net.*;
import java.io.*;
import static java.lang.Integer.parseInt;
import java.util.Scanner;

public class WhoisClient {
    private static InetAddress serverIP;
    private static int serverPort;
    private static String request, response;

    /**
     * @param args server ip, port
     */
    public static void main(String[] args) throws IOException
    {
        // Set up socket, using either the arguments data or default values
        if (args.length == 2)
        {
            serverIP = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);
        }
        else
        {
            System.err.println("Server data missing");
            serverIP = InetAddress.getByName("whois.dk-hostmaster.dk");
            serverPort = 43;
        }

        // Define three I/O streams:
        // - cin - to scan the terminal
        // - out - to send request to the server
        // - reader - to read the response of the server

        try (Socket serverSocket = new Socket(serverIP, serverPort);
             Scanner cin = new Scanner(System.in);
             PrintWriter out = new PrintWriter(serverSocket.getOutputStream(),true);
             InputStream in = serverSocket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in));)
        {
            System.out.println("Client up and running");
            System.out.println("Send a request to the server, domain name as parameter");

            // User types request on the console and the app sends it to the server
            request = cin.nextLine();
            out.println(request);

            // Get the response of the server and print it out to the client
            while ((response = reader.readLine()) != null)
                System.out.println(response);

            System.out.println("Client closing...");
        }
        catch (UnknownHostException e)
        {
            System.err.println("Couldn't find host " + serverIP);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O connection to " + serverIP);
            System.exit(1);
        }
    }
}
