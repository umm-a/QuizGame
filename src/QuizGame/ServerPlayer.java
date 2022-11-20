package QuizGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

class ServerPlayer extends Thread implements ActionListener {
    PlayerHelpClass playerHelpClass;
    String playerName;
    ServerPlayer opponent;
    Socket socket;
    BufferedReader inputbuffer;
    PrintWriter outputwriter;
    PlayerGUI GUI;
    int points = 0;


    public ServerPlayer(Socket socket, String playerName, PlayerHelpClass playerHelpClass) {
        this.socket = socket;
        this.playerName = playerName;
        this.playerHelpClass = playerHelpClass;
    }

    public void setOpponent(ServerPlayer opponent) {
        this.opponent = opponent;
    }

    public ServerPlayer getOpponent() {
        return opponent;
    }

    @Override
    public void run() {
        try {
            inputbuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputwriter = new PrintWriter(socket.getOutputStream(), true);
            outputwriter.println("WELCOME " + playerName);

            GUI = new PlayerGUI(this, playerName);
            outputwriter.println(playerName + " START GAME?");

            String inputMessage = "";

            while (true) {
                inputMessage = inputbuffer.readLine();
                if (inputMessage.equals("Player 1 START")) {
                    outputwriter.println("Player 1: You want to start");
                } else if (inputMessage.equals("Player 2 START")) {
                    outputwriter.println("Player 2: You want to start");
                } else if (inputMessage.equals("ENDGAME")) {
                    outputwriter.println("GAME HAS ENDED");
                    inputbuffer.close();
                    outputwriter.close();
                    System.exit(1); //vet inte vad som krävs för att stänga programmet på bra sätt med sockets, preliminärt lämnar jag det såhär
                }       //if game is over notifyWinner(), from the GUI-side since that's where we get our questions

            }

        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    public void notifyWinner() {
        if (points > getOpponent().points) {
            outputwriter.println("Player 1 wins!"); //Ska man kunna välja användarnamn?
        } else if (points < getOpponent().points) {
            outputwriter.println("Player 2 wins!");
        } else if (points == getOpponent().points) {
            outputwriter.println("TIE");
        } else {
            outputwriter.println("Something went wrong in notifyWinner-method of Player");
        }
    }

    protected void addOnePoint() {
        points += 1;
    }

    protected int getPoints() {
        return points;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Don't know if needed, but we can reach our GUI-stuff from here if we want to
    }
}

