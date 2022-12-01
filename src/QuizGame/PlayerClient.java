package QuizGame;

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

    PlayerGUI playerGUI;
    String chosenCategory;
    String chosenQuestion;
    String playerName;
    protected int state;
    static final int SETCATEGORY = 2;
    static final int QUESTIONSTATE = 3;
    static final int UPDATESETSCORE = 4;
    Question currentObject;
    boolean point;
    int questionsPerRound;
    int rounds;
    String fromPlayer;
    List<Integer> player1Scores = new ArrayList<>();
    List<Integer> player2Scores = new ArrayList<>();
    ObjectOutputStream objectOut;
    boolean roundIsDone;
    boolean gameIsDone;
    String nickname;
    String opponentNickname;


    public PlayerClient(PlayerGUI playerGUI) throws Exception {
        this.playerGUI = playerGUI;
        socket = new Socket("127.0.0.1", PORT);
        outpw = new PrintWriter(socket.getOutputStream(), true);
        inbuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        inObj = new ObjectInputStream(socket.getInputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());

        Properties properties = new Properties();
        try (final FileInputStream propertiesFile = new FileInputStream("src/QuizGame/PropertiesFile.properties")){
            properties.load(propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
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

        playerGUI.setWelcomeLayout(this);

        Object obj;


        while (true) {

            obj = inObj.readObject();

            if(obj instanceof List) {
                if (((List<?>) obj).contains("CATEGORIES")) {
                    List<String> objList = new ArrayList<>((List<String>) obj);
                    if (objList.contains("CATEGORIES")) {
                        objList.remove(objList.size() - 1);
                        playerGUI.setCategoryLayout(objList, this);
                        state = SETCATEGORY;
                    }
                }
                if (((List<String>) obj).contains("nicknames")) {
                    nickname = ((List<String>) obj).get(0);
                    opponentNickname = ((List<String>) obj).get(1);
                }
            } else if (obj.toString().contains("ScoreList of player")) {
                fromPlayer = obj.toString();
                obj = inObj.readObject();
                List<Integer> pointsList = new ArrayList<>((List<Integer>) obj);
                    if (fromPlayer.toLowerCase().contains("player 1")) {
                        removeContentsFromPlayer1List();
                        this.player1Scores = new ArrayList<>(pointsList);
                    } else if (fromPlayer.toLowerCase().contains("player 2")) {
                        removeContentsFromPlayer2List();
                        this.player2Scores = new ArrayList<>(pointsList);
                    }
            } else if (obj.toString().toLowerCase().contains("set score player 1")) {

                playerGUI.setScoreLayout(rounds, questionsPerRound, player1Scores, player2Scores,
                        "Player 1 Scoreboard", this, nickname, opponentNickname);
            } else if (obj.toString().toLowerCase().contains("set score player 2")) {

                playerGUI.setScoreLayout(rounds, questionsPerRound, player2Scores, player1Scores,
                        "Player 2 Scoreboard", this, nickname, opponentNickname);
            } else if ((obj instanceof Question)){
                state=QUESTIONSTATE;
                setCurrentObject((Question) obj);
                playerGUI.setQuestionLayout((Question) obj, this);
            } else if (obj.toString().equals("SET SCORE FOR BOTH PLAYERS")) {
                if(this.playerName.equals("player 1")){
                    playerGUI.setScoreLayout(rounds, questionsPerRound, player1Scores, player2Scores,
                            "Player 1 Scoreboard", this, nickname, opponentNickname);
                } else {
                    playerGUI.setScoreLayout(rounds, questionsPerRound, player2Scores, player1Scores,
                            "Player 2 Scoreboard", this, nickname, opponentNickname);
                }
            }else if (obj.toString().equals("SHUT DOWN")){
                playerGUI.setWaitingLayout("Motståndaren lämnade spelet... vilket betyder att DU vann!");

            } else if (obj.toString().equals("roundIsDone")) {
                roundIsDone = true;
            } else if (obj.toString().equals("gameIsDone")) {
                gameIsDone = true;
            } else {
                System.out.println(obj);
                System.out.println("This is where things tend to go wrong");
            }
        }

    }

    public void setCurrentObject(Question obj){
        this.currentObject=obj;
    }
    protected void sendPoint(boolean bool){
        outpw.println(playerName + "," + bool);
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
        if (e.getSource() == playerGUI.startButton) {
            outpw.println(playerGUI.nickNametf.getText());
            outpw.println(playerName + " is ready to play");
            if (playerName.equals("player 2")) {
                playerGUI.setWaitingLayout("Väntar på att motståndaren ska spela klart sin runda...");
            }
        } else if ((state==SETCATEGORY)) {
            chosenCategory = ((JButton) e.getSource()).getText();
            outpw.println(chosenCategory);
            state=QUESTIONSTATE;
        } else if (state==QUESTIONSTATE) {
            chosenQuestion = ((JButton) e.getSource()).getText();
            JButton button = (JButton) e.getSource();

            if (Objects.equals("<html><body style='text-align:center'>" + currentObject.answerCorrect +
                    "</body></html>", chosenQuestion)) {
                button.setBackground(new Color(0x9BC484));
                point=true;

            } else {
                button.setBackground(new Color(0xF83B3B));
                point=false;
            }
            playerGUI.questionPanel.repaint();
            playerGUI.questionPanel.revalidate();
            TimerTask sendQuestionTask = new TimerTask() {
                public void run() {
                    sendPoint(point);
                }
            };
            java.util.Timer timer = new Timer("Timer");
            int delay = 500;
            timer.schedule(sendQuestionTask, delay);

            state = UPDATESETSCORE;

        } else if ((state == UPDATESETSCORE) && (roundIsDone) && (!gameIsDone)) {
            if(((JButton) e.getSource()).getText().equals("Fortsätt")){
                playerGUI.setWaitingLayout("Väntar på att motståndaren ska spela klart sin runda...");

                outpw.println("NEXT ROUND");
                roundIsDone=false;
                state=QUESTIONSTATE;
            }
        } else if (gameIsDone) {
            if(((JButton) e.getSource()).getText().equals("Fortsätt")){
                playerGUI.setGameCompletedLayout(nickname, opponentNickname, this,
                        player1Scores, player2Scores);
            }
            if (((JButton) e.getSource()).getText().equals("Ja")){
                outpw.println("ja");
                System.out.println("Tryckt på JA");
            }
            if (((JButton) e.getSource()).getText().equals("Nej")){
                outpw.println("nej");
                System.out.println("Tryckt på NEJ");
            }
        }
    }
}