package QuizGamev2;



import java.io.*;
import java.net.Socket;

class ServerPlayer extends Thread {
    ServerGameEngine gameEngine;
    String playerName;
    String nickName;
    ServerPlayer opponent;
    ServerPlayer currentplayer;
    Socket socket;
    BufferedReader inputbuffer;
    PrintWriter outputwriter;
    ObjectOutputStream objectOut;

    static final int START = 1;
    static final int SETCATEGORY = 2;
    static final int PROCESSQUESTIONS = 3;
    static final int UPDATESETSCORE = 4;
    static final int UPDATEGAMESCORE = 4;

    int gameround;


    protected int state = 0;
    String chosenCategory;
    int points = 0;
    int numberOfQuestions = 2;
    int numberOfRounds;
    boolean isCorrectanswer;
    int[] setScore = new int[numberOfQuestions];
    int[] gameScore = new int[numberOfRounds];


    public ServerPlayer(Socket socket, String playerName, ServerGameEngine gameEngine) {
        this.socket = socket;
        this.playerName = playerName;
        this.gameEngine = gameEngine;
        if (this.playerName.equals("Player 1")) {
            currentplayer = this;
        }
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
            outputwriter = new PrintWriter(socket.getOutputStream(), true);
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            inputbuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            String inputMessage = "";

            outputwriter.println("Välkommen till spelet!");

                nickName = inputbuffer.readLine();
                state=2;



                Object question = null;


                while (true) {
                    if (state == 2) {
                        objectOut.writeObject(gameEngine.questionDatabase2.categoryList);

                        if (this.equals(currentplayer)) {
                            objectOut.writeObject(gameEngine.questionDatabase2.categoryList);
                            chosenCategory = inputbuffer.readLine();
                        }
                        state = 3;
                    } else if (state == 3) {
                        for (int i = 0; i < numberOfQuestions; i++) {
                         //   chosenCategory = "Djur & Natur"; //todo hårdkodad för testning
                            question = gameEngine.questionDatabase2.generateRandomQuestion(chosenCategory);
                            objectOut.writeObject(question);
                            isCorrectanswer = Boolean.parseBoolean(inputbuffer.readLine());
                            if (isCorrectanswer) {
                                setScore[i] = 1;
                            }
                        }
                        state = 4;
                    } else if (state == 4) {
                        //SKICKA POÄNG TILL CLIENTSIDAN
                    }
                }

                } catch(IOException e){
                    System.out.println("Player died: " + e);
                }
            }

            public void notifyWinner () {//todo behöver få info från PlayerClient
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

            protected void addOnePoint () {
                points += 1;
            }

            protected int getPoints () {
                return points;
            }
        }


