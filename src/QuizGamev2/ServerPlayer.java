package QuizGamev2;



import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    String pointString;


    protected int state = 0;
    String chosenCategory;
    String chosenQuestion;
    int points = 0;
    int numberOfQuestions = 2;
    int numberOfRounds;
    boolean isCorrectanswer;
    int[] setScore = new int[numberOfQuestions];
    int[] gameScore = new int[numberOfRounds];
    int turn = 1;
    boolean roundDone = false;

    List<Question> tempQuestionList = new ArrayList<>();


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

            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream("C:\\javamapp\\QuizGame\\src\\QuizGamev2\\PropertiesFile.properties"));
            } catch (Exception e){
                e.printStackTrace();
            }

            numberOfQuestions = Integer.parseInt(properties.getProperty("questionsPerRound"));
            String inputMessage = "";

            outputwriter.println("Välkommen till spelet " + playerName + "!");

                nickName = inputbuffer.readLine();
                state=2;

                Object question = null;

                while(opponent==null){//innan opponent anslutet så väntar man bara då man trycker "start game", här kan vi skicka in att vi väntar så att vi får en vänte-ruta
                    Thread.sleep(1000);
                }

                while (true) {
                    if (state == 2) {
                      //  objectOut.writeObject(gameEngine.questionDatabase2.categoryList);

                        if (this.equals(currentplayer)) {
                            objectOut.writeObject(gameEngine.questionDatabase2.categoryList);
                            chosenCategory = inputbuffer.readLine();
                        }
                        state = 3;
                    } else if (state == 3) {
                        if(this.equals(currentplayer) && (roundDone == false)) { //man kan även lägga till en int för att räkna antal varv, och få dessa via antal frågor från Propertis-fil
                            for (int i = 0; i < numberOfQuestions; i++) { //properties-filen väljer ju antal ronder samt frågor
                                if (turn==1) {
                                    question = gameEngine.questionDatabase2.generateRandomQuestion(chosenCategory);
                                    //todo kontrollera att question inte redan använts, metod i ServerGameEngine
                                    objectOut.writeObject(question);
                                    gameEngine.addQuestionToList((Question) question);
                                } else {
                                    objectOut.writeObject(gameEngine.getFromQuestionList(i));
                                }
                                pointString = inputbuffer.readLine(); //todo poäng
                            }
                            if(turn==2){
                                gameEngine.removeContentsFromQuestionList();
                                turn=1;
                                roundDone=true;
                                opponent.roundDone=true;
                            }
                            changePlayerTurn(); //här ändras både currentplayer och turn
                            opponent.changePlayerTurn();
                            //todo skicka meddelande om att byta layout
                        }
                        //  state = 4;
                    } else if (state == 4) {
                        //SKICKA POÄNG TILL CLIENTSIDAN
                    }
                }

                } catch(IOException e){
                    System.out.println("Player " + playerName + " died: " + e);
                } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
            public void changePlayerTurn(){
                if(currentplayer == this){
                    currentplayer = getOpponent();
                } else {
                    currentplayer = this;
                }

                if(turn==1){
                    turn=2;
                } else {
                    if(turn==2){
                        turn=1;
                    }
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


