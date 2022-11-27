package QuizGamev2;

import java.net.ServerSocket;

public class ServerListener {

    public static void main(String[] args) throws Exception {

        ServerSocket listener = new ServerSocket(25252);
        System.out.println("QuizGame's server is up and running");
        try {
            while (true) {
                ServerGameEngine gameEngine = new ServerGameEngine(new QuestionDatabase2());
                ServerPlayer playerOne = new ServerPlayer(listener.accept(), "player 1", gameEngine);
                playerOne.start();
                ServerPlayer playerTwo = new ServerPlayer(listener.accept(), "player 2", gameEngine);
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
