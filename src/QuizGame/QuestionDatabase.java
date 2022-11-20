package QuizGame;

import java.util.ArrayList;

public class QuestionDatabase {
    //needs some cleaning
    protected ArrayList<String[]> categoriesQuestions = new ArrayList<>(); //sedan kan man ju välja antal kategorier och frågor genom att ställa in vilka index som ska användas i vardera kategori
    protected ArrayList<String[][]> categoriesAnswers = new ArrayList<>();
    protected String[] animalQuestions = {"What do koalas typically eat?", "What color are flamingos when they are born?"};
    protected ArrayList<String> animalStringList;
    protected String[] religionQuestions = {"Which prophet came with the Torah?", "How many times a day does a muslim pray?"};
    protected String[] historyQuestions = {"Fill in the blank: The 19th Amendment guarantees ____ the right to vote", "Where was the Titanic headed to before it sank?"};

    protected String[][] animalAnswers = {{"Eucalyptus leaves", "Wild berries", "Bugs", "Branches of the pine tree"}, {"Grey/white", "Black", "Pink", "Green"}};//platserna för dessa kan switchas upp i GUI, men index plats 0 = rätt
    protected String[][] religionAnswers = {{"Moses", "Abraham", "Jesus", "Muhammed"}, {"5", "4", "3", "2"}};
    protected String[][] historyAnswers = {{"Women", "Men", "A guy named Travis", "No-one"}, {"New York City", "Mexico", "North Africa", "Antarctica"}};

    QuestionDatabase() {
        categoriesQuestions.add(animalQuestions);
        categoriesQuestions.add(religionQuestions);
        //   categoriesQuestions.add(historyQuestions);
        //  animalStringList.add("What do koalas typically eat?"); <------------------testade lite, massvis med kladd i denna klass!
        // animalStringList.add("What color are flamingos when they are born?");

        categoriesAnswers.add(animalAnswers);
        categoriesAnswers.add(religionAnswers);
        //  categoriesAnswers.add(historyAnswers);

    }

    protected String[] getAnimalQuestions() {
        return animalQuestions;
    }

    protected String[] getReligionQuestions() {
        return religionQuestions;
    }

    protected String[] getHistoryQuestions() {
        return historyQuestions;
    }

    protected String[][] getAnimalAnswers() {
        return animalAnswers;
    }

    protected String[][] getReligionAnswers() {
        return religionAnswers;
    }

    protected String[][] getHistoryAnswers() {
        return historyAnswers;
    }

    protected String getCategories() {
        return "Animals, Religion, History";
    }

    protected ArrayList<String[]> getCategoriesQuestionsArrayList() {
        return categoriesQuestions;
    }

    protected ArrayList<String[][]> getCategoriesAnswersArrayList() {
        return categoriesAnswers;
    }

}
