package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author Dora Di
 */
public class UDPClient
{
    // Client needs to know server identification, <IP:port>
    private static final int serverPort = 7777;

    // buffers for the messages
    public static String message;
    private static byte[] dataIn = new byte[256];
    private static byte[] dataOut = new byte[256];

    // In UDP messages are encapsulated in packages and sent over sockets
    private static DatagramPacket requestPacket;
    private static DatagramPacket responsePacket;
    private static DatagramSocket clientSocket;

    public static void main(String[] args) throws IOException
    {
        clientSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName("192.168.1.26");
        System.out.println(serverIP);

        Scanner scan = new Scanner(System.in);
        System.out.println("Type message: ");

        while((message = scan.nextLine()) != null)
        {
            sendRequest(serverIP);
            sendImage(serverIP);

            receiveResponse();
        }
        clientSocket.close();
    }

    public static void sendImage(InetAddress serverIP) throws IOException {
        BufferedImage img = ImageIO.read(new File("src/cat.jpg"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        byte[] buffer = baos.toByteArray();

        dataOut = message.getBytes();
        requestPacket = new DatagramPacket(buffer, buffer.length, serverIP, serverPort);
        clientSocket.send(requestPacket);
    }

    public static void sendRequest(InetAddress serverIP) throws IOException
    {
        //clientSocket = new DatagramSocket();
        dataOut = message.getBytes();
        requestPacket = new DatagramPacket(dataOut, dataOut.length, serverIP, serverPort);
        clientSocket.send(requestPacket);
    }

    public static void receiveResponse() throws IOException
    {
        //clientSocket = new DatagramSocket();
        responsePacket = new DatagramPacket(dataIn, dataIn.length);
        clientSocket.receive(responsePacket);
        String message = new String(responsePacket.getData(), 0, responsePacket.getLength());
        System.out.println("Response from Server: " + message);
    }
}
