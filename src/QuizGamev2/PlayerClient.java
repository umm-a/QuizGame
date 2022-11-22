package QuizGamev2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerClient implements ActionListener {
    private final int PORT = 25252;
    private Socket socket;
    BufferedReader inbuf;
    PrintWriter outpw;
    ObjectInputStream inObj;

    PlayerGUI2 playerGUI2;
    String chosenCategory;


    public PlayerClient(PlayerGUI2 playerGUI2) throws Exception {
        this.playerGUI2 = playerGUI2;
        socket = new Socket("127.0.0.1", PORT);
        outpw = new PrintWriter(socket.getOutputStream(), true);
        inbuf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        inObj = new ObjectInputStream(socket.getInputStream());

        String welcomemessage = inbuf.readLine();
        System.out.println(welcomemessage);


        playerGUI2.setWelcomeLayout(this);


        Object obj;


        while (true) {

            //här hämtas lista med frågekategorier
            obj = inObj.readObject();
            if(obj instanceof List) {
                List<String> objList = new ArrayList<>((List<String>) obj);
            if(objList.contains("CATEGORIES")){
                objList.remove(objList.size() - 1);
                playerGUI2.setCategoryLayout(objList);
            }} else if ((obj instanceof Question)){
                System.out.println("The obj is not a list of categories, rather these are questions to be layed out in the GUI");
                playerGUI2.setQuestionLayout((Question) obj, this);
            } else {
                System.out.println("This is where things tend to go wrong");
            }



        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playerGUI2.startButton) {
            System.out.println("Test: Startbutton pressed for: " + playerGUI2.nickNametf.getText());
            outpw.println(playerGUI2.nickNametf.getText());
        }

        for (JButton jb : playerGUI2.catButtons) {
            if (e.getSource() == jb) {
                chosenCategory = jb.getText();
            }
            outpw.println(chosenCategory);
            System.out.println("Test från PlayerClient: " + chosenCategory);

        }
    }

}