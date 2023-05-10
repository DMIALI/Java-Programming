package Main;

import CommandData.ClientData;
import CommandData.InputCommandData;
import CommandData.ServerData;
import Utils.Client;
import Utils.Printer;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Base64;
import java.util.Scanner;

public class Server {

    private DatagramSocket datagramSocket;
    private byte[] buffer = new byte[256];
    private static int PORT = 1408;

    public Server (DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }

    public void receiveThenSend(ClientManager clientManager, CollectionManager collectionManager, Printer printer){
        while (true){
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                Client client = clientManager.getClient(inetAddress, port);
                ClientData clientData = handle(datagramPacket);
                checkClientData(clientData, client);
                InputCommandData inputCommandData = new InputCommandData(collectionManager,client, printer, clientData, );
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public ClientData handle(DatagramPacket datagramPacket) throws IOException, ClassNotFoundException {
        InputStream inputStream = new ByteArrayInputStream(datagramPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (ClientData) objectInputStream.readObject();
    }
    public void checkClientData(ClientData clientData, Client client) throws IOException {
        if (clientData.getCounter() == client.getDatagramCounter()){
            client.increaseCounter();
        }
        else{
            if (Objects.equals(clientData.getCounter(), client.getLatestServerData().counter())){
                send(client.getLatestServerData(), client);
            }
            throw new IOException();
        }
    }
    public void send(ServerData serverData, Client client) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(serverData);
        objectOutputStream.close();
        byte[] sendingDataBuffer  = Base64.getEncoder().encodeToString(outputStream.toByteArray()).getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, client.getInetAddress(), client.getPort());
        try {
            datagramSocket.send(datagramPacket);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] arg) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(PORT);
        Server server = new Server(datagramSocket);
        ClientManager clientManager = new ClientManager();
        Scanner scanner = new Scanner(System.in);
        CollectionManager collectionManager = new CollectionManager("data.json", scanner);
        Printer printer = new Printer();
        server.receiveThenSend(clientManager, collectionManager);
    }
}