package QuizGame;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {

    public static void main(String[] args) {

        try (ServerSocket listener = new ServerSocket(25252)) {
            System.out.println("QuizGame's server is up and running");
            while (true) {
                ServerGameEngine gameEngine = new ServerGameEngine(new QuestionDatabase());
                ServerPlayer playerOne = new ServerPlayer(listener.accept(), "player 1", gameEngine);
                playerOne.start();
                ServerPlayer playerTwo = new ServerPlayer(listener.accept(), "player 2", gameEngine);
                playerTwo.start();

                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}