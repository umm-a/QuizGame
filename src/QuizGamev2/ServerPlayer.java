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
    Object stringOutObject;

    static final int START = 1;
    static final int SETCATEGORY = 2;
    static final int PROCESSQUESTIONS = 3;
    static final int UPDATESETSCORE = 4;
    static final int UPDATEGAMESCORE = 4;

    int gameround;
    String pointString;

    List<Integer> player1Scores = new ArrayList<>();
    List<Integer> player2Scores = new ArrayList<>();


    protected int state = 0;
    String chosenCategory;
    String chosenQuestion;
    int points = 0;
    int questionsPerRound = 0;
    int rounds;
    int numberOfRounds;
    boolean isCorrectanswer;
    int[] setScore = new int[questionsPerRound];
    int[] gameScore = new int[numberOfRounds];
    int turn = 1;
    boolean roundDone = false;
    boolean setCategory = true;

    int currentRound = 0;
    String readyToPlay = "";

    List<Question> tempQuestionList = new ArrayList<>();


    public ServerPlayer(Socket socket, String playerName, ServerGameEngine gameEngine) {
        this.socket = socket;
        this.playerName = playerName;
        this.gameEngine = gameEngine;
        if (this.playerName.equals("player 1")) {
            currentplayer = this;
            gameEngine.player1 = this;
        } else {
            gameEngine.player2 = this;
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
                properties.load(new FileInputStream("src\\QuizGamev2\\PropertiesFile.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            questionsPerRound = Integer.parseInt(properties.getProperty("questionsPerRound"));
            rounds = Integer.parseInt(properties.getProperty("rounds"));
            String inputMessage = "";

            outputwriter.println("Välkommen till spelet " + playerName + "!" + " Rounds are: " + rounds);

            nickName = inputbuffer.readLine();
            state = 2;

            Object question = null;


            this.readyToPlay = inputbuffer.readLine();
            System.out.println(readyToPlay);

            if (readyToPlay.contains("player 1")) {
                gameEngine.player1Ready = true;
            } else if (readyToPlay.contains("player 2")) {
                gameEngine.player2Ready = true;
            }

            System.out.println("MOTSTÅNDAREN: " + opponent.readyToPlay);//kladd

            while ((!gameEngine.player2Ready)) {//innan opponent anslutet så väntar man bara då man trycker "start game", här kan vi skicka in att vi väntar så att vi får en vänte-ruta
                Thread.sleep(1000);
            }

            while (true) {
                if (state == 2) {
                    currentRound = 0;
                    state = 3;
                } else if (state == 3) {
                    // ServerGameEngine
                    while (currentRound < rounds) {
                        if ((this.equals(currentplayer)) && (setCategory == true)) {
                            chooseCategory();
                            setCategory = false;
                            opponent.setCategory = false;
                        }
                        if (this.equals(currentplayer)) {
                            for (int i = 0; i < questionsPerRound; i++) {
                                if (turn == 1) {
                                    question = gameEngine.questionDatabase2.generateRandomQuestion(chosenCategory);//todo kontrollera att question inte redan använts, metod i ServerGameEngine
                                    objectOut.writeObject(question);
                                    gameEngine.addQuestionToList((Question) question);
                                } else {
                                    objectOut.writeObject(gameEngine.getFromQuestionList(i));
                                }
                                objectOut.flush();
                                calculateAndSendPoints();
                            }
                            if (turn == 2) {
                                gameEngine.removeContentsFromQuestionList();
                                setCategoryToTrue();
                                setCurrentRoundPlusOne();
                            }
                            changePlayerTurn();
                            opponent.changePlayerTurn();

                            // objectOut.writeObject("SET SCORE"); todo här skulle vi vilja sätta den ene spelarens board, sedan uppdatera den andres efter rundan
                            //   objectOut.flush();
                            //todo skicka meddelande om att byta layout
                            stringOutObject = "SET SCORE " + playerName;
                            objectOut.writeObject(stringOutObject);
                            objectOut.flush();
                        }

                        //   roundDone=false;
                    }
                    // currentRound=0; //ska enbart sättas om vi startar nytt spel

                    //todo de ska få se scoreboard mellan varven, om de klickar "fortsätt" ska vi fortsätta!
                    //  state = 4;
                } else if (state == 4) {
                    // objectOut.writeObject(gameEngine.countScore(state, true, this));
                    //SKICKA POÄNG TILL CLIENTSIDAN
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerName + " died: " + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    protected void setCategoryToTrue(){
        this.setCategory = true;
        opponent.setCategory = true;
    }


    protected void calculateAndSendPoints() throws IOException {
        pointString = inputbuffer.readLine();
        System.out.println(pointString + " är mottagen");

        objectOut.writeObject(gameEngine.checkPlayer(pointString));//metod som kollar vilken spelare det är
        objectOut.flush();
        objectOut.writeObject(gameEngine.addScoreToListAndReturnFullList(pointString)); //skickar lista med poäng
        for (Integer i: player1Scores) {
            System.out.println(i + " player1scores");
        }
        for (Integer i: player2Scores) {
            System.out.println(i + " player2scores");
        }
        objectOut.flush();
        System.out.println(gameEngine.checkPlayer(pointString) + " är skickat");
    }

    protected void chooseCategory() throws IOException {
        objectOut.writeObject(gameEngine.questionDatabase2.categoryList);
        objectOut.flush();
        this.chosenCategory = inputbuffer.readLine();
        opponent.chosenCategory = this.chosenCategory;
    }

    protected void setCurrentRoundPlusOne() {
        this.currentRound += 1;
        opponent.currentRound += 1;
    }

    protected void changePlayerTurn() {
        if (this.currentplayer == this) {
            currentplayer = getOpponent();
        } else {
            currentplayer = this;
        }

        if (this.turn == 1) {
            this.turn = 2;
        } else {
            if (this.turn == 2) {
                this.turn = 1;
            }
        }
    }
}

