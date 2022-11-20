package QuizGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class PlayerGUI extends JFrame implements ActionListener {
    QuestionDatabase questionDatabase = new QuestionDatabase();
    int numberOfQuestions = 4; //this should not be hard-coded, but we will need to get values from Properties-file
    int currentQuestion = 0;
    int currentAnswer = 0;
    String[] questions = new String[2];
    String[][] answers = new String[0][4];
    ServerPlayer serverPlayer;
    String playerID;
    String playerNick;
    JPanel mainPanel = new JPanel();
    JPanel insideMainPanel = new JPanel();
    JPanel questionsPanel = new JPanel();
    JTextField nickTextField = new JTextField();
    JTextArea pointsTextArea = new JTextArea("Points: ");
    JButton setNickButton = new JButton("Set nickname");
    JPanel nickPanel = new JPanel(new GridLayout(1, 2));
    JTextArea quizQuestion = new JTextArea("Here will be a question");
    JButton[] answerButtons = new JButton[4];

    PlayerGUI(ServerPlayer serverPlayer, String playerID) {
        this.serverPlayer = serverPlayer;
        this.playerID = playerID;

        setSize(500, 500);
        setTitle(playerID);
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(nickPanel);
        nickPanel.add(nickTextField);
        nickPanel.add(setNickButton);
        mainPanel.add(insideMainPanel);
        insideMainPanel.setLayout(new GridLayout(2, 1));
        questionsPanel.setLayout(new GridLayout(2, 2));
        insideMainPanel.add(quizQuestion);
        quizQuestion.setEditable(false);
        insideMainPanel.add(questionsPanel);

        add(mainPanel);
        setNickButton.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //let's have some stages before we start with the questions! Such as, asking if a player wishes to play
        newQuestions();
    }

    public void newQuestions() { //We want to have several types of questions, use the database in appropriate way + Properties file
        questionsPanel.removeAll();
        questions = questionDatabase.getAnimalQuestions();
        answers = questionDatabase.getAnimalAnswers();
        int i = 0;
        quizQuestion.setText(questions[currentQuestion]);
        Integer[] counter = {0, 1, 2, 3};
        List<Integer> counterList = Arrays.asList(counter);
        Collections.shuffle(counterList);
        for (JButton b : answerButtons) {
            b = new JButton(answers[currentQuestion][counterList.get(i)]);
            questionsPanel.add(b);
            b.addActionListener(this);
            //     b.addActionListener(ServerPlayer); <- perhaps useful?
            ++i;

            this.answerButtons[i] = b;
        }
        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == setNickButton) && ((nickTextField != null))) {
            playerNick = nickTextField.getText();
            JTextArea playerNickname = new JTextArea(playerNick);
            nickPanel.removeAll();
            nickPanel.add(playerNickname);
            playerNickname.setEditable(false);
            nickPanel.add(pointsTextArea);
            pointsTextArea.setEditable(false);
            nickPanel.revalidate();
            nickPanel.repaint();
        } else if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            if (buttonText.equals((answers[currentQuestion][0]))) {
                quizQuestion.setText("Correct!");
                button.setBackground(new Color(0x9BC484));

                serverPlayer.addOnePoint();
                pointsTextArea.setText("Points: " + serverPlayer.getPoints());
                nickPanel.repaint();
                nickPanel.revalidate();
            } else { //we'd probably want to make all of the wrong JButtons red if wrong
                button.setBackground(new Color(0xF83B3B));
                quizQuestion.setText("Wrong answer");
            }
            currentAnswer += 1;
            currentQuestion += 1;
            if (currentAnswer == 3) {
                currentAnswer = 0;
                currentQuestion += 1;
            }
            TimerTask loadNewQuestions = new TimerTask() {
                public void run() {//spelet har en tendens att frysa IBLAND, inte alltid. Kanske har något att göra med detta!
                    newQuestions();
                }
            };
            Timer timer = new Timer("Timer");
            int delay = 1500;
            timer.schedule(loadNewQuestions, delay);

        }
    }
}

