package QuizGamev2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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


    public PlayerClient(PlayerGUI2 playerGUI2) throws Exception {
        this.playerGUI2 = playerGUI2;
        socket = new Socket("127.0.0.1", PORT);
        outpw = new PrintWriter(socket.getOutputStream(), true);
        inbuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        inObj = new ObjectInputStream(socket.getInputStream());

        String welcomemessage = inbuf.readLine();
        System.out.println(welcomemessage);
        if(welcomemessage.contains("Player 1")){
            this.playerName = "Player 1";
        } else {
            this.playerName = "player 2";
        }


        playerGUI2.setWelcomeLayout(this);


        Object obj;


        while (true) {

            //här hämtas lista med frågekategorier
            obj = inObj.readObject();
            if(obj instanceof List) {
                List<String> objList = new ArrayList<>((List<String>) obj);
            if(objList.contains("CATEGORIES")){
                objList.remove(objList.size() - 1);
                playerGUI2.setCategoryLayout(objList, this);
                state=SETCATEGORY;
            }
            } else if ((obj instanceof Question)){
                state=QUESTIONSTATE;
                setCurrentObject((Question) obj);
                System.out.println("The obj is not a list of categories, rather these are questions to be layed out in the GUI");
                playerGUI2.setQuestionLayout((Question) obj, this);
            } else {
                System.out.println("This is where things tend to go wrong");
            }
            //ta emot meddelande om att rundan är klar, låt spelare2 få upp sina frågor



        }
    }
    public Object getCurrentObject(){
        return this.currentObject;
    }
    public void setCurrentObject(Question obj){
        this.currentObject=obj;
    }
    protected void sendPoint(boolean bool){//todo poäng
        outpw.println(playerName + "," + bool);
        System.out.println(playerName + "," + bool + " skickades till ServerPlayer");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playerGUI2.startButton) {
            System.out.println("Test: Startbutton pressed for: " + playerGUI2.nickNametf.getText());
            outpw.println(playerGUI2.nickNametf.getText());
            if(playerName=="player 2"){
                //Watiting for opponent-ruta
          //      playerGUI2.setScoreLayout(1, 1);
            }
        } else if ((state==SETCATEGORY)) {
            chosenCategory = ((JButton) e.getSource()).getText();
                outpw.println(chosenCategory);
                System.out.println("Test från PlayerClient: " + chosenCategory);
            state=QUESTIONSTATE;
            } else if (state==QUESTIONSTATE) {//todo OBS man ska inte kunna trycka på fler knappar när man svarat på en specifik fråga
            chosenQuestion = ((JButton) e.getSource()).getText();
            JButton button = (JButton) e.getSource();
            if((currentObject.answerCorrect)== chosenQuestion){
                button.setBackground(new Color(0x9BC484));
                point=true; //todo poäng
            } else {
                button.setBackground(new Color(0xF83B3B));
                point=false; //todo poäng
            }
            playerGUI2.questionPanel.repaint();
            playerGUI2.questionPanel.revalidate();
            TimerTask sendQuestionTask = new TimerTask() {
                public void run() {
                    sendPoint(point);//todo poäng
                }
            };
            java.util.Timer timer = new Timer("Timer");
            int delay = 1500;
            timer.schedule(sendQuestionTask, delay);

        }
        }
}