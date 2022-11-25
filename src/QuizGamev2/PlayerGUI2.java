package QuizGamev2;

//Ny version

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerGUI2 extends JFrame{

    JFrame baseFrame = new JFrame("QuizGame");
    JPanel welcomePanel;
    JPanel categoryPanel;
    JPanel questionPanel;
    JPanel scorePanel;
    JButton startButton;
    List<JButton> catButtons;
    Font myFont = new Font("Ink Free", Font.BOLD, 19);
    Font myFont2 = new Font("Ink Free", Font.BOLD, 15);
    Font myFont3 = new Font("Ink Free", Font.BOLD, 12);
   PlayerClient playerClient;
    JTextField nickNametf;
    String opponentNickname;


    //bara för test:
  //  String[] cata = {"Djur & Natur", "Religion", "Musik", "Teknik", "Geografi"};
  //  List<String> catlist = new ArrayList<String>(Arrays.asList(cata));
  //  Question qtest = new Question("Musik & Kultur", "Från vilket land kommer Adele?", "Storbritannien", "Frankrike", "USA", "Kanada");
// test slut

    public PlayerGUI2() throws Exception {
        this.playerClient = new PlayerClient(this);


//bara för test
       /* setWelcomeLayout(playerClient);
        Scanner sc = new Scanner(System.in);
        Thread.sleep(10000);
        setCategoryLayout(catlist);
        Thread.sleep(10000);
        setQuestionLayout(qtest,playerClient);*/

    }


    public void setWelcomeLayout(PlayerClient playerClient) {
        baseFrame.setSize(340, 500);
        baseFrame.setLayout(null);

        welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBounds(10, 10, 320, 450);
        welcomePanel.setBorder(new EtchedBorder());

        JLabel welcomelb = new JLabel("Välkommen till QuizGame!", SwingConstants.CENTER);
        welcomelb.setBounds(10, 10, 300, 60);
        welcomelb.setFont(myFont);
        welcomelb.setBorder(new EtchedBorder());
        welcomePanel.add(welcomelb);

        JLabel nickNamelb = new JLabel("Ange ett nickname:", SwingConstants.CENTER);
        nickNamelb.setBounds(10, 100, 300, 40);
        nickNamelb.setFont(myFont2);
        nickNamelb.setBorder(new EtchedBorder());
        welcomePanel.add(nickNamelb);

        nickNametf = new JTextField("myNickname", SwingConstants.CENTER);
        nickNametf.setBounds(40, 150, 240, 35);
        nickNametf.setFont(myFont2);
        welcomePanel.add(nickNametf);

        startButton = new JButton("START GAME");
        startButton.setBounds(60,240,200,60);
        startButton.setFont(myFont);
        welcomePanel.add(startButton);
        startButton.addActionListener(playerClient);


        baseFrame.add(welcomePanel);


        baseFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        baseFrame.setLocationRelativeTo(null);
        baseFrame.setLayout(null);
        baseFrame.setVisible(true);

    }


    public void setCategoryLayout(List<String> categorylist ,PlayerClient playerClient) {
        catButtons = new ArrayList<>();
        int noOfCat = categorylist.size();
        baseFrame.getContentPane().removeAll();

        categoryPanel = new JPanel();
        categoryPanel.setLayout(null);
        categoryPanel.setBounds(10, 10, 320, 450);
        categoryPanel.setBorder(new EtchedBorder());

        JLabel chooseCatlb = new JLabel("Välj en Kategori", SwingConstants.CENTER);
        chooseCatlb.setBounds(10, 10, 300, 60);
        chooseCatlb.setFont(myFont);
        chooseCatlb.setBorder(new EtchedBorder());
        categoryPanel.add(chooseCatlb);


        JPanel buttonPanel = new JPanel(new GridLayout(noOfCat, 1, 5, 5));
        buttonPanel.setBounds(10, 100, 300, 320);
        buttonPanel.setFont(myFont3);
        buttonPanel.setBorder(new EtchedBorder());
        categoryPanel.add(buttonPanel);

        for (int i = 0; i < categorylist.size(); i++) {
            catButtons.add(new JButton(categorylist.get(i)));
            buttonPanel.add(catButtons.get(i));
            catButtons.get(i).addActionListener(playerClient);
        }

        baseFrame.add(categoryPanel);
        baseFrame.revalidate();
        baseFrame.repaint();

    }


    public void setQuestionLayout(Question qObj, PlayerClient playerClient) {
        baseFrame.getContentPane().removeAll();
        questionPanel = new JPanel();
        questionPanel.setLayout(null);
        questionPanel.setBounds(10, 10, 320, 450);
        questionPanel.setBorder(new EtchedBorder());

        JLabel categorylb = new JLabel(qObj.category, SwingConstants.CENTER);
        categorylb.setBounds(10, 10, 300, 50);
        categorylb.setFont(myFont);
        categorylb.setBorder(new EtchedBorder());
        questionPanel.add(categorylb);

        JLabel questionlb = new JLabel("<html><body style='text-align:center'>" + qObj.question + "</body></html>", SwingConstants.CENTER);
        questionlb.setBounds(10, 80, 300, 100);
        questionlb.setFont(myFont);
        questionlb.setBorder(new EtchedBorder());
        questionPanel.add(questionlb);

        JPanel qButtonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        qButtonPanel.setBounds(10, 200, 300, 230);
        qButtonPanel.setFont(myFont2);
        qButtonPanel.setBorder(new EtchedBorder());
        questionPanel.add(qButtonPanel);

        List<JButton> qbuttons = new ArrayList<>();
        qbuttons.add(new JButton(qObj.answerCorrect));
        qbuttons.add(new JButton(qObj.answerOption2));
        qbuttons.add(new JButton(qObj.answerOption3));
        qbuttons.add(new JButton(qObj.answerOption4));

        Collections.shuffle(qbuttons);
        for (JButton jb : qbuttons) {
            qButtonPanel.add(jb);
            jb.addActionListener(playerClient);

            baseFrame.add(questionPanel);
            baseFrame.revalidate();
            baseFrame.repaint();
        }
    }

        public void setScoreLayout() {
            baseFrame.getContentPane().removeAll();
            scorePanel = new JPanel();
            scorePanel.setLayout(null);
            scorePanel.setBounds(10, 10, 320, 450);
            scorePanel.setBorder(new EtchedBorder());






             }

    public void setScoreLayout(int questions, int rounds) {

        baseFrame.getContentPane().removeAll();
        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setBounds(10, 10, 320, 450);
        scorePanel.setBorder(new EtchedBorder());




        JPanel playerScorePanel = new JPanel();
        playerScorePanel.setLayout(new GridLayout(4,2));
        playerScorePanel.setBounds(0,120,120,240);
        playerScorePanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        playerScorePanel.setBorder(new EtchedBorder());

        scorePanel.add(playerScorePanel);

        JPanel opponentScorePanel = new JPanel();
        opponentScorePanel.setLayout(new GridLayout(rounds,questions));
        opponentScorePanel.setBounds(200,120,120,240);
        opponentScorePanel.setBorder(new EtchedBorder());
        scorePanel.add(opponentScorePanel);


        List<SmallCircle>circles = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            circles.add(new SmallCircle(Color.RED));
            circles.get(i).setBorder(new EtchedBorder());
            playerScorePanel.add(circles.get(i));
        }

        JButton fortsättButton = new JButton("Fortsätt");
        fortsättButton.setBounds(110,380,100,50);
        scorePanel.add(fortsättButton);


        baseFrame.add(scorePanel);
        baseFrame.revalidate();
        baseFrame.repaint();


    }

    //Denna klass ritar en cirkel, används i ScoreLayout
    class SmallCircle extends JPanel {
        int radie, x, y;
        Color color;
        public SmallCircle(Color color){
            super();
            radie=15;
            x=0;
            y=0;
            this.color=color;
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




        public static void main (String[]args) throws Exception {
            PlayerGUI2 g2 = new PlayerGUI2();
        }


}
