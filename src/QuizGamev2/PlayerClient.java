package QuizGamev2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
            List<String> categories = new ArrayList<>((List<String>) obj);
            playerGUI2.setCategoryLayout(categories);




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