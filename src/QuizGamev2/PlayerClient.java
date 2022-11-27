package QuizGamev2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class PlayerClient implements ActionListener {
    private final int PORT = 25252;
    private Socket socket;
    BufferedReader inbuf;
    PrintWriter outpw;
    ObjectInputStream inObj;

    PlayerGUI2 playerGUI2;
    String chosenCategory;
    String chosenQuestion;
    String playerName;
    protected int state = 0;
    static final int START = 1;
    static final int SETCATEGORY = 2;
    static final int QUESTIONSTATE = 3;
    static final int UPDATESETSCORE = 4;
    Question currentObject;
    boolean point=false;
    int questionsPerRound;
    int rounds;
    String fromPlayer = "";
    List<Integer> player1Scores = new ArrayList<>();
    List<Integer> player2Scores = new ArrayList<>();
    ObjectOutputStream objectOut;
    boolean roundIsDone = false;
    boolean gameIsDone = false;


    public PlayerClient(PlayerGUI2 playerGUI2) throws Exception {
        this.playerGUI2 = playerGUI2;
        socket = new Socket("127.0.0.1", PORT);
        outpw = new PrintWriter(socket.getOutputStream(), true);
        inbuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        inObj = new ObjectInputStream(socket.getInputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src\\QuizGamev2\\PropertiesFile.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nextRound = "NEXT ROUND";
        objectOut.writeObject(nextRound);
        objectOut.flush();

        questionsPerRound = Integer.parseInt(properties.getProperty("questionsPerRound"));
        rounds = Integer.parseInt(properties.getProperty("rounds"));
        String welcomemessage = inbuf.readLine();
        System.out.println(welcomemessage);
        if(welcomemessage.contains("player 1")){
            this.playerName = "player 1";
        } else {
            this.playerName = "player 2";
        }


        playerGUI2.setWelcomeLayout(this);

        Object obj;


        while (true) {
//todo skicka in nickname från ServerPlayer för att sätta in i GUI
            obj = inObj.readObject();
            if(obj instanceof List) {
                if (((List<?>) obj).contains("CATEGORIES")) {
                    List<String> objList = new ArrayList<>((List<String>) obj);
                    if (objList.contains("CATEGORIES")) {
                        objList.remove(objList.size() - 1);
                        playerGUI2.setCategoryLayout(objList, this);
                        state = SETCATEGORY;
                        System.out.println(" IN CATEGORIES ");
                    }
                }
            } else if (obj.toString().contains("ScoreList of player")) {
                fromPlayer = obj.toString();
                obj = inObj.readObject();
                System.out.println(obj.toString());
                List<Integer> pointsList = new ArrayList<>((List<Integer>) obj);
                if (fromPlayer.toLowerCase().contains("player 1")) {
                    removeContentsFromPlayer1List();
                    this.player1Scores = new ArrayList<>(pointsList);
                    //    setPointListToPlayer(pointsList, player1Scores);
                } else if (fromPlayer.toLowerCase().contains("player 2")) {
                    removeContentsFromPlayer2List();
                    this.player2Scores = new ArrayList<>(pointsList);
                    // setPointListToPlayer(pointsList, player2Scores);
                }
                System.out.println("ScoreList of player in PlayerClient has run");
            } else if (obj.toString().toLowerCase().contains("set score player 1")) {
                //   state=UPDATESETSCORE;
                playerGUI2.setScoreLayout(rounds, questionsPerRound, player1Scores, player2Scores, "Player 1 Scoreboard", this);
            } else if (obj.toString().toLowerCase().contains("set score player 2")) {
                //  state=UPDATESETSCORE;
                playerGUI2.setScoreLayout(rounds, questionsPerRound, player2Scores, player1Scores, "Player 2 Scoreboard", this);
            } else if ((obj instanceof Question)){
                state=QUESTIONSTATE;
                setCurrentObject((Question) obj);
                System.out.println("The obj is not a list of categories, rather these are questions to be layed out in the GUI");
                playerGUI2.setQuestionLayout((Question) obj, this);
            } else if (obj.toString().equals("SET SCORE FOR BOTH PLAYERS")) {
                if(this.playerName.equals("player 1")){
                    playerGUI2.setScoreLayout(rounds, questionsPerRound, player1Scores, player2Scores, "Player 1 Scoreboard", this);
                } else {
                    playerGUI2.setScoreLayout(rounds, questionsPerRound, player2Scores, player1Scores, "Player 2 Scoreboard", this);
                }
            } else if (obj.toString().equals("roundIsDone")) {
                System.out.println("roundIsDone is recieved");
                roundIsDone = true;
            } else if (obj.toString().equals("gameIsDone")) {
                System.out.println("gameIsDone is recieved");
                gameIsDone = true;
            } else {
                System.out.println(obj.toString());
                System.out.println("This is where things tend to go wrong");
            }
        }
        //ta emot meddelande om att rundan är klar, låt spelare2 få upp sina frågor

    }

    public Object getCurrentObject(){
        return this.currentObject;
    }
    public void setCurrentObject(Question obj){
        this.currentObject=obj;
    }
    protected void sendPoint(boolean bool){
        outpw.println(playerName + "," + bool);
        System.out.println(playerName + "," + bool + " skickades till ServerPlayer");
    }
    protected void setPointListToPlayer(List<Integer> theListOfPoints, List<Integer> playerXList){

    }
    public void removeContentsFromPlayer1List(){
        List<Integer> toRemove = new ArrayList<>();

        for (Integer q: player1Scores) {
            toRemove.add(q);
        }

        player1Scores.removeAll(toRemove);
    }
    public void removeContentsFromPlayer2List(){
        List<Integer> toRemove = new ArrayList<>();

        for (Integer q: player2Scores) {
            toRemove.add(q);
        }

        player2Scores.removeAll(toRemove);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playerGUI2.startButton) {
            System.out.println("Test: Startbutton pressed for: " + playerGUI2.nickNametf.getText());
            outpw.println(playerGUI2.nickNametf.getText());
            outpw.println(playerName + " is ready to play");
            if (playerName.equals("player 2")) {
                playerGUI2.setWaitingLayout("Waiting for opponent to finish their turn...");
            }
        } else if ((state==SETCATEGORY)) {
            chosenCategory = ((JButton) e.getSource()).getText();
            outpw.println(chosenCategory);
            System.out.println("Test från PlayerClient: " + chosenCategory);
            state=QUESTIONSTATE;
        } else if (state==QUESTIONSTATE) {//todo OBS man ska inte kunna trycka på fler knappar när man svarat på en specifik fråga
            chosenQuestion = ((JButton) e.getSource()).getText();
            JButton button = (JButton) e.getSource();

            if ((currentObject.answerCorrect) == chosenQuestion) {
                button.setBackground(new Color(0x9BC484));
                point=true;

            } else {
                button.setBackground(new Color(0xF83B3B));
                point=false;
            }
            playerGUI2.questionPanel.repaint();
            playerGUI2.questionPanel.revalidate();
            TimerTask sendQuestionTask = new TimerTask() {
                public void run() {
                    sendPoint(point);
                }
            };
            java.util.Timer timer = new Timer("Timer");
            int delay = 500;
            timer.schedule(sendQuestionTask, delay);

            state = UPDATESETSCORE;

        } else if ((state == UPDATESETSCORE) && (roundIsDone) && (gameIsDone==false)) { //todo ska bara gå om båda spelarna spelat en runda
            if(((JButton) e.getSource()).getText().equals("Fortsätt")){
                playerGUI2.setWaitingLayout("Waiting for opponent to finish their turn...");
                //fortsätt-knappen ska ej gå att klicka på förrän roundDone=true
                outpw.println("NEXT ROUND");
                roundIsDone=false;
                state=QUESTIONSTATE; //todo i sista rundan måste man ändra detta, kolla om det finns ett meddelande från ServerPlayer som meddelar att det är slut. Skicka detta till while-loopen här i PlayerClient, ta boolean och kolla av!
            }
        } else if (gameIsDone == true) {
            if(((JButton) e.getSource()).getText().equals("Fortsätt")){
                //spela igen-ruta
            }
        }
    }
}