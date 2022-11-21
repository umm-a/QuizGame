package QuizGame;

import java.io.*;
import java.net.Socket;

public class PlayerClient implements Runnable {
    private final int PORT = 25252;
    private Socket socket;
    BufferedReader inbuf;
    PrintWriter outpw;

    public PlayerClient() throws Exception {
        socket = new Socket("127.0.0.1", PORT);
        outpw = new PrintWriter(socket.getOutputStream(), true);
        inbuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {

        try {
            String inputMessage;
            String myPlayerID = "";
            while (true) {
                inputMessage = inbuf.readLine();
                if (inputMessage.startsWith("WELCOME Player")) {
                    System.out.println(inputMessage);
                    String[] playerID = inputMessage.split(" ", 2);
                    myPlayerID = playerID[1];
                    System.out.println("Player ID: " + myPlayerID);
                } else if (inputMessage.equals(myPlayerID + " START GAME?")) {
                    //rather these steps should come from the user in GUI and not be hard-coded in this class
                    sendMessage(myPlayerID + " START");
                } else {
                    System.out.println(inputMessage);
                    sendMessage("This is from PlayerClient else-statement");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        outpw.println(message);
    }

    public static void main(String[] args) throws Exception {
        PlayerClient client = new PlayerClient();
        Thread t = new Thread(client);
        t.start();
    }

}


