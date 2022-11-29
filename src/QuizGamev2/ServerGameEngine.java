package QuizGamev2;

import java.util.ArrayList;
import java.util.List;

public class ServerGameEngine{

    ServerPlayer player1;
    ServerPlayer player2;
    List<Question> tempQuestionList = new ArrayList<>();

    QuestionDatabase2 questionDatabase2;
    boolean player1Ready = false;
    boolean player2Ready = false;






    public ServerGameEngine(QuestionDatabase2 questionDatabase2){
        this.questionDatabase2 = questionDatabase2;

    }
    public void addQuestionToList(Question question){
        this.tempQuestionList.add(question);
    }
    public void removeContentsFromQuestionList(){
        List<Question> toRemove = new ArrayList<>();

        for (Question q: tempQuestionList) {
            toRemove.add(q);
        }

        tempQuestionList.removeAll(toRemove);
    }
    public Question getFromQuestionList(int i){
        return tempQuestionList.get(i);
    }

    public List<Integer> addScoreToListAndReturnFullList(String pointString){

        String[] scoreString = pointString.split("," );
        String playerName = scoreString[0].trim();
        boolean isCorrectAnswer = Boolean.parseBoolean(scoreString[1]);

        if (playerName.equals("player 1") && (isCorrectAnswer == true)) {
            player1.currentPlayerScores.add(1);
            return player1.currentPlayerScores;
        }
        else if (playerName.equals("player 1") && (isCorrectAnswer == false)) {
            player1.currentPlayerScores.add(0);
            return player1.currentPlayerScores;
        }
        else if (playerName.equals("player 2") && (isCorrectAnswer == true)) {
            player2.currentPlayerScores.add(1);
            return player2.currentPlayerScores;
        }
        else if (playerName.equals("player 2") && (isCorrectAnswer == false)) {
            player2.currentPlayerScores.add(0);
            return player2.currentPlayerScores;
        }
        else {
            System.out.println("Det gick inte att lägga poäng i listan");
            return null;
        }

    }


    public String checkPlayer(String pointString){

        String[] scoreString = pointString.split("," );
        String playerName = scoreString[0].trim();
        if (playerName.equals("player 1")){
            return "ScoreList of player 1";
        }
        else if (playerName.equals("player 2")){
            return "ScoreList of player 2";
        }
        else{
            return "Något gick fel i checkPlayer()";
        }
    }

/*
    public void notifyWinner (List<Integer> player1Scores, List<Integer> player2Scores) {//todo behöver få info från PlayerClient


        if (sumOfScores(player1Scores) > sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("Player 1 wins"); //Ska man kunna välja användarnamn?

        } else if (sumOfScores(player1Scores) > sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("Player 2 wins");

        } else if (sumOfScores(player1Scores) == sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("TIE");

        } else {
            serverPlayer.outputwriter.println("Something went wrong in notifyWinner-method of Player");
        }
    }*/

    public int sumOfScores (List<Integer> scores){

        int sum = 0;
        for (int i : scores){
            sum += i;
        }
        return sum;
    }

}
