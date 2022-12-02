package QuizGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

public class PlayerGUI extends JFrame {

    GraphicsChooser graphicsChooser;
    JFrame baseFrame = new JFrame("QuizGame");
    Canvas1 welcomePanel;
    Canvas1 categoryPanel;
    Canvas1 questionPanel;
    Canvas1 waitingPanel;
    Canvas1 scorePanel;
    Canvas1 gameCompletedPanel;
    JButton startButton;
    List<JButton> catButtons;

    Font myFont = new Font("Arial", Font.BOLD, 19);
    Font myFont2 = new Font("Arial", Font.BOLD, 15);
    Font myFont3 = new Font("Arial", Font.BOLD, 14);
    Font myFont4 = new Font("Arial", Font.BOLD, 22);
    Font myFont5 = new Font("Arial", Font.BOLD, 26);
    Font myFont6 = new Font("Arial", Font.BOLD, 42);

    MyJLabel playerGameScore;
    MyJLabel opponentGameScore;
    List<SmallCircle> playerDots;
    List<SmallCircle> opponentDots;
    JTextField nickNametf;
    PlayerClient playerClient;

    public PlayerGUI() throws Exception {
         graphicsChooser = new GraphicsChooser();
         this.playerClient = new PlayerClient(this);
    }

    int frameWidth;
    int frameHeight;
    public void setWelcomeLayout(PlayerClient playerClient) {

        baseFrame.setPreferredSize(new Dimension(360,520));
        baseFrame.pack();
        baseFrame.getContentPane().setBackground(new Color(40,40,40));

        //används till centrering av panel på frame
        frameWidth = baseFrame.getContentPane().getWidth();
        frameHeight = baseFrame.getContentPane().getHeight();

        welcomePanel = new Canvas1(graphicsChooser, frameWidth,frameHeight);

        MyJLabel welcomelb = new MyJLabel("Välkommen till QuizGame!", SwingConstants.CENTER,welcomePanel);
        welcomelb.setBounds(10, 10, 300, 60);
        welcomelb.setFont(myFont);
        welcomePanel.add(welcomelb);

        MyJLabel nickNamelb = new MyJLabel("Ange ett nickname:", SwingConstants.CENTER,welcomePanel);
        nickNamelb.setBounds(10, 100, 300, 40);
        nickNamelb.setFont(myFont2);
        welcomePanel.add(nickNamelb);

        nickNametf = new JTextField("myNickname", SwingConstants.CENTER);
        nickNametf.setBounds(40, 150, 240, 35);
        nickNametf.setFont(myFont2);
        welcomePanel.add(nickNametf);

        startButton = new JButton("START GAME");
        startButton.setBounds(60, 240, 200, 60);
        startButton.setFont(myFont);
        startButton.setBackground(welcomePanel.shapes.lightcolor);
        startButton.setOpaque(true);
        welcomePanel.add(startButton);
        startButton.addActionListener(playerClient);

        baseFrame.add(welcomePanel);

        baseFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        baseFrame.setLocationRelativeTo(null);
        baseFrame.setLayout(null);
        baseFrame.setVisible(true);
        baseFrame.pack();
    }


    public void setCategoryLayout(List<String> categorylist, PlayerClient playerClient) {
        catButtons = new ArrayList<>();
        int noOfCat = categorylist.size();
        baseFrame.getContentPane().removeAll();

        categoryPanel = new Canvas1(graphicsChooser,frameWidth,frameHeight);

        MyJLabel chooseCatlb = new MyJLabel("Välj en Kategori", SwingConstants.CENTER,categoryPanel);
        chooseCatlb.setBounds(10, 10, 300, 60);
        chooseCatlb.setFont(myFont);
        categoryPanel.add(chooseCatlb);

        JPanel buttonPanel = new JPanel(new GridLayout(noOfCat, 1, 5, 5));
        buttonPanel.setBounds(10, 100, 300, 320);
        buttonPanel.setBackground(categoryPanel.shapes.darkcolor);
        buttonPanel.setBorder(categoryPanel.shapes.border1);
        buttonPanel.setOpaque(true);
        categoryPanel.add(buttonPanel);

        for (int i = 0; i < categorylist.size(); i++) {
            catButtons.add(new JButton(categorylist.get(i)));
            buttonPanel.add(catButtons.get(i));
            catButtons.get(i).setFont(myFont3);
            catButtons.get(i).setBackground(categoryPanel.shapes.lightcolor);
            catButtons.get(i).setOpaque(true);
            catButtons.get(i).addActionListener(playerClient);
        }

        baseFrame.add(categoryPanel);
        baseFrame.revalidate();
        baseFrame.repaint();
        baseFrame.pack();
    }

    public void setQuestionLayout(Question qObj, PlayerClient playerClient) {
        baseFrame.getContentPane().removeAll();

        questionPanel = new Canvas1(graphicsChooser,frameWidth,frameHeight);

        MyJLabel categorylb = new MyJLabel(qObj.category, SwingConstants.CENTER,questionPanel);
        categorylb.setBounds(10, 10, 300, 50);
        categorylb.setFont(myFont);
        questionPanel.add(categorylb);

        MyJLabel questionlb = new MyJLabel("<html><body style='text-align:center'>" + qObj.question + "</body></html>", SwingConstants.CENTER,questionPanel);
        questionlb.setBounds(10, 80, 300, 100);
        questionlb.setFont(myFont);
        questionPanel.add(questionlb);

        MyJPanel qButtonPanel = new MyJPanel(new GridLayout(2, 2, 5, 5),questionPanel);
        qButtonPanel.setBounds(10, 200, 300, 230);
        questionPanel.add(qButtonPanel);

        List<JButton> qbuttons = new ArrayList<>();
        qbuttons.add(new JButton("<html><body style='text-align:center'>" + qObj.answerCorrect + "</body></html>"));
        qbuttons.add(new JButton("<html><body style='text-align:center'>" + qObj.answerOption2 + "</body></html>"));
        qbuttons.add(new JButton("<html><body style='text-align:center'>" + qObj.answerOption3 + "</body></html>"));
        qbuttons.add(new JButton("<html><body style='text-align:center'>" + qObj.answerOption4 + "</body></html>"));

        Collections.shuffle(qbuttons);
        for (JButton jb : qbuttons) {
            qButtonPanel.add(jb);
            jb.setBackground(questionPanel.shapes.lightcolor);
            jb.setOpaque(true);
            jb.addActionListener(playerClient);
        }
            baseFrame.add(questionPanel);
            baseFrame.revalidate();
            baseFrame.repaint();
    }

    public void setWaitingLayout(String message){
        baseFrame.getContentPane().removeAll();

        waitingPanel = new Canvas1(graphicsChooser,frameWidth,frameHeight);

        MyJLabel waitingMessagelb = new MyJLabel("<html><body style='text-align:center'>" + message, SwingConstants.CENTER,waitingPanel);
        waitingMessagelb.setBounds(30, 100, 260, 120);
        waitingMessagelb.setFont(myFont);
        waitingPanel.add(waitingMessagelb);

        baseFrame.add(waitingPanel);
        baseFrame.revalidate();
        baseFrame.repaint();
    }

    public void setScoreLayout(int rounds,int questionPerRound, List<Integer> playerScore, List<Integer> opponentScore,
                               String statusMessage,PlayerClient playerClient, String nickname, String opponentNickname) {

        baseFrame.getContentPane().removeAll();

        int playerGamescore = playerScore.stream().mapToInt(Integer::intValue).sum();
        int opponentGamescore = opponentScore.stream().mapToInt(Integer::intValue).sum();

        JButton fortsättButton;

        scorePanel = new Canvas1(graphicsChooser,frameWidth,frameHeight);

        MyJLabel gameInfoLabel = new MyJLabel(statusMessage, SwingConstants.CENTER,scorePanel);
        gameInfoLabel.setBounds(80, 70, 160, 30);
        gameInfoLabel.setFont(myFont2);
        scorePanel.add(gameInfoLabel);

        MyJLabel playerNameLabel = new MyJLabel(nickname, SwingConstants.CENTER,scorePanel);
        playerNameLabel.setBounds(4, 20, 140, 30);

        playerNameLabel.setFont(myFont2);
        scorePanel.add(playerNameLabel);

        MyJLabel opponentNameLabel = new MyJLabel(opponentNickname, SwingConstants.CENTER,scorePanel);
        opponentNameLabel.setBounds(176, 20, 140, 30);
        opponentNameLabel.setFont(myFont2);
        scorePanel.add(opponentNameLabel);

        playerGameScore = new MyJLabel(String.valueOf(playerGamescore), SwingConstants.CENTER, scorePanel);
        playerGameScore.setBounds(40, 70, 40, 40);
        playerGameScore.setFont(myFont4);
        scorePanel.add(playerGameScore);

        opponentGameScore = new MyJLabel(String.valueOf(opponentGamescore), SwingConstants.CENTER,scorePanel);
        opponentGameScore.setBounds(240, 70, 40, 40);
        opponentGameScore.setFont(myFont4);
        scorePanel.add(opponentGameScore);

        MyJPanel playerScorePanel = new MyJPanel(new GridLayout(0, questionPerRound),scorePanel);
        playerScorePanel.setBounds(4, 120, 120, 240);
        scorePanel.add(playerScorePanel);

        MyJPanel opponentScorePanel = new MyJPanel(new GridLayout(0, questionPerRound),scorePanel);
        opponentScorePanel.setBounds(196, 120, 120, 240);
        scorePanel.add(opponentScorePanel);

        int panelwidth = playerScorePanel.getSize().width;
        int panelheight = playerScorePanel.getSize().height;
        gridwidth = (panelwidth / questionPerRound) + 6; //adderar 6 för att kompensera för borderbortfall
        gridheight = (panelheight / rounds) + 6;  //adderar 6 för att kompensera för borderbortfall

        playerDots = new ArrayList<>();
        for (int i = 0; i < (questionPerRound * rounds); i++) {
            playerDots.add(new SmallCircle(Color.white));
            playerDots.get(i).setBorder(scorePanel.shapes.border2);
            playerScorePanel.add(playerDots.get(i));
        }

        Color myRed = new Color(229, 57, 53);
        Color myGreen = new Color(67, 160, 71);

        //update thisPlayer scores
        for (int i = 0; i < playerScore.size(); i++) {
            if (playerScore.get(i) == 1)
                playerDots.get(i).color = myGreen;
            else playerDots.get(i).color = myRed;
        }

        playerGameScore.setText(String.valueOf(playerGamescore));

        opponentDots = new ArrayList<>();
        for (int i = 0; i < (questionPerRound * rounds); i++) {
            opponentDots.add(new SmallCircle(Color.white));
            opponentDots.get(i).setBorder(scorePanel.shapes.border2);
            opponentScorePanel.add(opponentDots.get(i));
        }

        for (int i = 0; i < opponentScore.size(); i++) {
            if (opponentScore.get(i) == 1)
                opponentDots.get(i).color = myGreen;
            else opponentDots.get(i).color = myRed;
        }

        opponentGameScore.setText(String.valueOf(opponentGamescore));

        fortsättButton = new JButton("Fortsätt");
        fortsättButton.setBounds(110, 380, 100, 50);
        fortsättButton.setFont(myFont2);
        fortsättButton.setBackground(scorePanel.shapes.lightcolor);
        fortsättButton.setOpaque(true);
        scorePanel.add(fortsättButton);
        fortsättButton.addActionListener(playerClient);

        baseFrame.add(scorePanel);
        baseFrame.revalidate();
        baseFrame.repaint();
    }

    public void setGameCompletedLayout(String nickname, String opponentNickname, PlayerClient playerClient,
                                       List<Integer> player1Score, List<Integer> player2Score) {

        baseFrame.getContentPane().removeAll();
        gameCompletedPanel = new Canvas1(graphicsChooser,frameWidth,frameHeight);

        int player1ScoreInt = player1Score.stream().mapToInt(Integer::intValue).sum();
        int player2ScoreInt = player2Score.stream().mapToInt(Integer::intValue).sum();
        String gameResultMessage = getGameResultMessage(playerClient.playerName, player1ScoreInt, player2ScoreInt);

        MyJLabel gameResultLabel = new MyJLabel(gameResultMessage, SwingConstants.CENTER,gameCompletedPanel);
        gameResultLabel.setBounds(10, 30, 300, 60);
        gameResultLabel.setFont(myFont5);
        gameCompletedPanel.add(gameResultLabel);

        MyJLabel playerNickLabel = new MyJLabel(nickname, SwingConstants.CENTER,gameCompletedPanel);
        playerNickLabel.setBounds(4, 140, 136, 50);
        playerNickLabel.setFont(myFont2);
        playerNickLabel.setBorder(gameCompletedPanel.shapes.border1UpLeft);
        gameCompletedPanel.add(playerNickLabel);

        MyJLabel opponentNickLabel = new MyJLabel(opponentNickname, SwingConstants.CENTER,gameCompletedPanel);
        opponentNickLabel.setBounds(180, 140, 136, 50);
        opponentNickLabel.setFont(myFont2);
        opponentNickLabel.setBorder(gameCompletedPanel.shapes.border1UpRight);
        gameCompletedPanel.add(opponentNickLabel);

        MyJLabel vsLabel = new MyJLabel("VS", SwingConstants.CENTER,gameCompletedPanel);
        vsLabel.setBounds(140, 140, 40, 50);
        vsLabel.setFont(myFont2);
        vsLabel.setBorder(gameCompletedPanel.shapes.border1Up);
        gameCompletedPanel.add(vsLabel);

        String playerScoreString;
        String opponentScoreString;

        if (playerClient.playerName.equals("player 1")) {
            playerScoreString = String.valueOf(player1ScoreInt);
            opponentScoreString = String.valueOf(player2ScoreInt);
        }
        else {
            playerScoreString = String.valueOf(player2ScoreInt);
            opponentScoreString = String.valueOf(player1ScoreInt);
        }

        MyJLabel playerScoreLabel = new MyJLabel(playerScoreString, SwingConstants.CENTER,gameCompletedPanel);
        playerScoreLabel.setBounds(4, 190, 136, 60);
        playerScoreLabel.setFont(myFont6);
        playerScoreLabel.setBorder(gameCompletedPanel.shapes.border1DownLeft);
        gameCompletedPanel.add(playerScoreLabel);

        MyJLabel opponentScoreLabel = new MyJLabel(opponentScoreString, SwingConstants.CENTER,gameCompletedPanel);
        opponentScoreLabel.setBounds(180, 190, 136, 60);
        opponentScoreLabel.setFont(myFont6);
        opponentScoreLabel.setBorder(gameCompletedPanel.shapes.border1DownRight);
        gameCompletedPanel.add(opponentScoreLabel);

        MyJLabel hyphenLabel = new MyJLabel("-", SwingConstants.CENTER,gameCompletedPanel);
        hyphenLabel.setBounds(140, 190, 40, 60);
        hyphenLabel.setFont(myFont6);
        hyphenLabel.setBorder(gameCompletedPanel.shapes.border1Down);
        gameCompletedPanel.add(hyphenLabel);

        MyJLabel newGameLabel = new MyJLabel("Spela igen?", SwingConstants.CENTER,gameCompletedPanel);
        newGameLabel.setBounds(30, 290, 260, 60);
        newGameLabel.setFont(myFont5);
        gameCompletedPanel.add(newGameLabel);

        JButton yesButton = new JButton("Ja");
        yesButton.setBounds(60, 360, 70, 50);
        yesButton.setFont(myFont5);
        yesButton.setBorder(new EtchedBorder());
        yesButton.setBackground(Color.GREEN);
        yesButton.setOpaque(true);
        yesButton.addActionListener(playerClient);
        gameCompletedPanel.add(yesButton);

        JButton noButton = new JButton("Nej");
        noButton.setBounds(180, 360, 70, 50);
        noButton.setFont(myFont5);
        noButton.setBorder(new EtchedBorder());
        noButton.setBackground(Color.RED);
        noButton.setOpaque(true);
        noButton.addActionListener(playerClient);
        gameCompletedPanel.add(noButton);

        baseFrame.add(gameCompletedPanel);
        baseFrame.revalidate();
        baseFrame.repaint();
    }


    public String getGameResultMessage(String playerName, int player1ScoreInt, int player2ScoreInt) {
        if (playerName.equals("player 1")) {
            if (player1ScoreInt < player2ScoreInt)
                return "Du förlorade!";
            else if (player1ScoreInt > player2ScoreInt)
                return "Du vann!";
            else
                return "Oavgjort!";
        }
        else {
            if (player2ScoreInt < player1ScoreInt)
                return "Du förlorade!";
            else if (player2ScoreInt > player1ScoreInt)
                return "Du vann!";
            else
                return "Oavgjort!";
        }
    }

    int gridwidth;
    int gridheight;

    //Denna klass ritar en cirkel, används i ScoreLayout
    class SmallCircle extends JPanel {
        int radie, x, y;
        Color color;

        public SmallCircle(Color color) {
            super();
            radie = 15;
            x = (gridwidth / 2) - radie;
          //  System.out.println(gridwidth);
            y = (gridheight / 2) - radie;


            this.color = color;
        }

        public void paintComponent(Graphics comp) {
            Graphics2D comp2D = (Graphics2D) comp;
            Color bgcolor = scorePanel.getBackground();
            comp2D.setColor(bgcolor);
            comp2D.fillRect(0, 0, getSize().width, getSize().height);
            comp2D.setColor(color);
            Ellipse2D.Float circle = new Ellipse2D.Float(x, y, radie, radie);
            comp2D.fill(circle);
        }
    }
    public static void main(String[] args) throws Exception {
        PlayerGUI g2 = new PlayerGUI();
    }
}