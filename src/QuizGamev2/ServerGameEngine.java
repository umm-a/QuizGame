package QuizGamev2;

import QuizGame.PlayerClient;

import java.util.ArrayList;
import java.util.List;

public class ServerGameEngine{

    ServerPlayer player1;
    ServerPlayer player2;

    String nickName1;
    String nickName2;
    ServerPlayer serverPlayer;
    List<Question> tempQuestionList = new ArrayList<>();

    QuestionDatabase2 questionDatabase2;


    public ServerGameEngine(QuestionDatabase2 questionDatabase2){
        this.questionDatabase2 = questionDatabase2;

    }
    public void addQuestionToList(Question question){
        this.tempQuestionList.add(question);
    }
    public void removeContentsFromQuestionList(){
        for (Question q: tempQuestionList) {
            this.tempQuestionList.remove(q);
        }
    }
    public Question getFromQuestionList(int i){
        return tempQuestionList.get(i);
    }

    public void separateScoreString(String pointString){

        String[] scoreString = pointString.split("," );
        String playerName = scoreString[0].trim();
        boolean isCorrectAnswer = Boolean.parseBoolean(scoreString[1]);

        addScoreToList(playerName,isCorrectAnswer);

    }

    public List<Integer> addScoreToList(String playerName, Boolean isCorrectAnswer){
        if (playerName.equals("Player 1") && isCorrectAnswer == true){
            serverPlayer.player1Scores.add(1);
            return serverPlayer.player1Scores;
        }
        else if (playerName.equals("Player 1") && isCorrectAnswer == false){
            serverPlayer.player1Scores.add(0);
            return serverPlayer.player1Scores;
        }
        else if (playerName.equals("Player 2") && isCorrectAnswer == true) {
            serverPlayer.player2Scores.add(1);
            return serverPlayer.player2Scores;
        }
        else if (playerName.equals("player 2") && isCorrectAnswer == false){
            serverPlayer.player2Scores.add(0);
            return serverPlayer.player1Scores;
        }
        else {
            System.out.println("Det gick inte att lägga poäng i listan");
            return null;
        }

    }


   /* public int countScore(int state, boolean isCorrectAnswer, ServerPlayer player){
        if (state == 3  && isCorrectAnswer == true){
            player.points++;
            scoreToString(player.points);
        }
        else if (state == 4){
            scoreToString(player.points);
        }
        return player.points;
    }

    public String scoreToString(int points){

        String pointString = "Your score: " + points;

        return pointString;

    }

    */
    public void notifyWinner (List<Integer> player1Scores, List<Integer> player2Scores) {//todo behöver få info från PlayerClient


        if (sumOfScores(player1Scores) > sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("Player 1 wins"); //Ska man kunna välja användarnamn?

        } else if (sumOfScores(player1Scores) > sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("Palyer 2 wins");

        } else if (sumOfScores(player1Scores) == sumOfScores(player1Scores)) {

            serverPlayer.outputwriter.println("TIE");

        } else {
            serverPlayer.outputwriter.println("Something went wrong in notifyWinner-method of Player");
        }
    }

    public int sumOfScores (List<Integer> scores){

        int sum = 0;
        for (int i : scores){
            sum += i;
        }
        return sum;
    }

}
