package QuizGame;

import java.net.ServerSocket;

public class ServerListener {

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(25252);
        System.out.println("QuizGame's server is up and running");
        try {
            while (true) {
                PlayerHelpClass helpclass = new PlayerHelpClass();//Do we even need it? Do we want to break out our code, perhaps, from ServerPlayer?
                ServerPlayer playerOne = new ServerPlayer(listener.accept(), "Player 1", helpclass);
                playerOne.start();
                ServerPlayer playerTwo = new ServerPlayer(listener.accept(), "Player 2", helpclass);
                playerTwo.start();

                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);
                System.out.println("Loop in ServerListener has run");
            }
        } finally {
            listener.close();
        }
    }
}
