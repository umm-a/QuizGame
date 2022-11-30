package QuizGamev2;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    String scoreListMessage;
    List<Integer> tempList = new ArrayList<>();

    static final int START = 1;
    static final int SETCATEGORY = 2;
    static final int PROCESSQUESTIONS = 3;
    static final int UPDATESETSCORE = 4;
    static final int UPDATEGAMESCORE = 4;

    int gameround;
    String pointString;

    List<Integer> currentPlayerScores = new ArrayList<>();
   // List<Integer> player2Scores = new ArrayList<>();
    List<Integer> opponentScores;


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
    String setScoreForBothPlayers = "SET SCORE FOR BOTH PLAYERS";

    List<Question> tempQuestionList = new ArrayList<>();
    ObjectInputStream inObj;
    String nextRoundMessage;
    String roundIsDone = "roundIsDone";
    String gameIsDone = "gameIsDone";


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
            inObj = new ObjectInputStream(socket.getInputStream());

            Properties properties = new Properties();
            try (final FileInputStream propertiesFile = new FileInputStream("src/QuizGamev2/PropertiesFile.properties")) {
                properties.load(propertiesFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nextRoundMessage = (String) inObj.readObject();
            System.out.println(nextRoundMessage);

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

            while ((!gameEngine.player2Ready)) {//innan opponent anslutit så väntar man bara då man trycker "start game", här kan vi skicka in att vi väntar så att vi får en vänte-ruta
                Thread.sleep(1000);
            }

            List<String> nicknameList = Arrays.asList(nickName, opponent.nickName, "nicknames");
            objectOut.writeObject(nicknameList);

            while (true) {
                if (state == 2) {
                    currentRound = 0;
                    state = 3;
                }
                else if (state == 3) {
                    while (currentRound < rounds) {

                        while (!nextRoundMessage.equals("NEXT ROUND")) {//todo här
                            Thread.sleep(100);
                            nextRoundMessage = inputbuffer.readLine();
                            System.out.println(nextRoundMessage);
                        }
                        if ((this.equals(currentplayer)) && (setCategory)) {
                            chooseCategory();
                            setCategory = false;
                            opponent.setCategory = false;
                        }
                        if (this.equals(currentplayer)) {
                            for (int i = 0; i < questionsPerRound; i++) {
                                if (turn == 1) {
                                    question = gameEngine.questionDatabase2.generateRandomQuestion(chosenCategory);
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
                                setRoundDoneTrue();
                            }
                            changePlayerTurnWithinRound();
                            opponent.changePlayerTurnWithinRound();
                            nextRoundMessage = "WAITING FOR NEXT ROUND";


                            stringOutObject = "SET SCORE " + playerName;
                            objectOut.writeObject(stringOutObject);
                            objectOut.flush();

                            if (roundDone) {
                                setScoreForBothPlayers();
                                changePlayerTurnWithinRound();
                                opponent.changePlayerTurnWithinRound();
                                changePlayerTurnAfterEachRound();
                                opponent.changePlayerTurnAfterEachRound();
                                tellPlayerClientRoundIsDone();
                                opponent.tellPlayerClientRoundIsDone();
                                setRoundDoneFalse();
                            }
                        }
                    }
                    tellPlayerClientGameIsDone();
                    opponent.tellPlayerClientGameIsDone();
                    state = 4;
                    // currentRound=0; //ska enbart sättas om vi startar nytt spel

                }
                else if (state == 4) {

                    if (nextRoundMessage.equals("ja")){
                        System.out.println(nextRoundMessage + " in state 4");

                        removElementsInScoreList();
                        state = 2;
                    }
                    else if (nextRoundMessage.equals("nej")){
                        objectOut.writeObject(playerName + " left");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerName + " died: " + e);
            opponent.opponentHasLeft();
        } catch (InterruptedException e) {
            opponent.opponentHasLeft();
            throw new RuntimeException(e);
        }
        catch (NullPointerException e) {
            System.out.println("Waiting for an another client to connect...");

        }catch(Exception e) {
            opponent.opponentHasLeft();
            throw new RuntimeException(e);
        }
    }

    protected void setCategoryToTrue() {
        this.setCategory = true;
        opponent.setCategory = true;
    }

    protected void setRoundDoneTrue() {
        this.roundDone = true;
        opponent.roundDone = true;
    }

    protected void setRoundDoneFalse() {
        this.roundDone = false;
        opponent.roundDone = false;
    }

    protected void sendOpponentScore() throws IOException {
        opponentScores = new ArrayList<>(opponent.currentPlayerScores);

        if (this.playerName.equals("player 1")) {
            scoreListMessage = "ScoreList of player 2";
        } else {
            scoreListMessage = "ScoreList of player 1";
        }
        objectOut.writeObject(scoreListMessage);
        objectOut.flush();

        List<Integer> tempList = new ArrayList<>(opponent.currentPlayerScores);
        objectOut.writeObject(tempList);
        objectOut.flush();
    }

    protected void setScoreForBothPlayers() throws IOException {
        sendOpponentScore();
        opponent.sendOpponentScore();

        objectOut.writeObject(setScoreForBothPlayers);
        objectOut.flush();
        opponent.objectOut.writeObject(setScoreForBothPlayers);
        objectOut.flush();
    }

    protected void tellPlayerClientRoundIsDone() throws IOException {
        objectOut.writeObject(roundIsDone);
        objectOut.flush();
    }

    protected void tellPlayerClientGameIsDone() throws IOException {
        objectOut.writeObject(gameIsDone);
        objectOut.flush();
        ;
    }


    protected void calculateAndSendPoints() throws IOException {
        pointString = inputbuffer.readLine();
        System.out.println(pointString + " är mottagen");

        objectOut.writeObject(gameEngine.checkPlayer(pointString));//metod som kollar vilken spelare det är
        objectOut.flush();
        tempList = new ArrayList<>(gameEngine.addScoreToListAndReturnFullList(pointString)); //skickar lista med poäng
        objectOut.writeObject(tempList);
        for (Integer i : currentPlayerScores) {
            System.out.println(i + " currentPlayerScores");
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

    protected void changePlayerTurnWithinRound() {
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

    protected void changePlayerTurnAfterEachRound() {
        if (this.turn == 1) {
            this.turn = 2;
        } else {
            if (this.turn == 2) {
                this.turn = 1;
            }
        }
    }


    protected void opponentHasLeft() {
        String shutdownString = "SHUT DOWN";
        try {
            objectOut.writeObject(shutdownString);
            objectOut.flush();
            objectOut.reset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("endGame has run");
        try {
            this.socket.close();
            System.out.println("this socket is closed");
        } catch (IOException e) {
            System.out.println("this socket is closed.");
        }
        //System.exit(1);
    }
    protected List<Integer> removElementsInScoreList(){
        for (int i : currentPlayerScores){
            if (currentPlayerScores != null){
                currentPlayerScores.remove(i);
            }
        }
        return currentPlayerScores;
    }
}
