package QuizGamev2;

import java.util.ArrayList;
import java.util.List;

public class ServerGameEngine{

    ServerPlayer player1;
    ServerPlayer player2;

    String nickName1;
    String nickName2;
    ServerPlayer currentPlayer;
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
        boolean isCorrectAnswer = Boolean.parseBoolean(scoreString[1]);
        currentPlayer.playerName = scoreString[0]; // är det samma player??

        //countScore(3,isCorrectAnswer,+++)

        if (isCorrectAnswer == true){
            currentPlayer.points ++;
            scoreToString(currentPlayer.points);
        }
        else {
            scoreToString(currentPlayer.points);
        }
    }


    //
    public int countScore(int state, boolean isCorrectAnswer, ServerPlayer player){
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
    public void notifyWinner (ServerPlayer player) {//todo behöver få info från PlayerClient
        if (player.points > player.getOpponent().points) {

            player.outputwriter.println(player.playerName + " wins!"); //Ska man kunna välja användarnamn?

        } else if (player.points < player.getOpponent().points) {

            player.outputwriter.println(player.getOpponent().playerName + " wins");

        } else if (player.points == player.getOpponent().points) {

            player.outputwriter.println("TIE");

        } else {
            player.outputwriter.println("Something went wrong in notifyWinner-method of Player");
        }
    }

}
