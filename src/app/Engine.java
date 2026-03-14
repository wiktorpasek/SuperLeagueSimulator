package app;
import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public class Engine {

    public String simulateMatch(Team home, Team away) {
        String matchCommentary = "";
        int matchHattack = home.getForm()*5 + home.getAtackLvl();
        int matchAattack = away.getForm()*5 + away.getAtackLvl();
        int redProcent = ThreadLocalRandom.current().nextInt(1, 101);
        int injuryProcent = ThreadLocalRandom.current().nextInt(1, 101);
        int matchHdefense = home.getForm()*2 + home.getDefenseLvl();
        int matchAdefense = away.getForm()*2 + away.getDefenseLvl();

        if(redProcent <= 5){
            boolean coinFlip = ThreadLocalRandom.current().nextBoolean(); //true gospodarz // false gość
            if (coinFlip){
                matchHdefense -= 10;
                matchCommentary += "Brutalny faul! " + home.getName() + " kończy mecz w dziesiątkę! ";
            }else {
                matchAdefense -= 10;
                matchCommentary += "Brutalny faul! " + away.getName() + " kończy mecz w dziesiątkę! ";
            }
        }

        if (injuryProcent <= 5){
            boolean coinFlip = ThreadLocalRandom.current().nextBoolean();
            if (coinFlip){
                home.setForm(home.getForm() - 2);
                matchCommentary += "Ależ pech... Kontuzja gracza " + home.getName();
            }else {
                away.setForm(away.getForm() - 2);
                matchCommentary += "Ależ pech... Kontuzja gracza " + away.getName();
            }
        }

        int homeGoals = calculateGoalsPro(matchHattack + 2,matchAdefense);
        int awayGoals = calculateGoalsPro(matchAattack,matchHdefense);
        int homeStats = matchHdefense + matchHattack;
        int awayStats = matchAdefense + matchAattack;
        int bonusChances = ThreadLocalRandom.current().nextInt(1, 101);


        if (awayStats < homeStats) {
            if (bonusChances <= 5) {
                awayGoals += 1;
            }
        }else if (homeStats < awayStats) {
            if (bonusChances <= 5) {
                homeGoals += 1;
                if (homeGoals > awayGoals) {
                    matchCommentary +=  "Niesiony dopingiem kibiców " + home.getName() + " wyrywa zwycięstwo! ";                        }
            }
        }
        if (awayStats < homeStats && awayGoals > homeGoals) {
                matchCommentary += "Sensacja na stadionie! " + away.getName() + " ucieraja nosa faworytom! ";
        }

        home.MatchResult(homeGoals, awayGoals);
        away.MatchResult(awayGoals, homeGoals);


        String homeColor = home.getForm() > 0 ? "#27ae60" : (home.getForm() < 0 ? "#e74c3c" : "#95a5a6");
        String awayColor = away.getForm() > 0 ? "#27ae60" : (away.getForm() < 0 ? "#e74c3c" : "#95a5a6");

        String hForm = "<span style='color: " + homeColor + "; font-size: 10px;'>[" + (home.getForm() > 0 ? "+" : "") + home.getForm() + "]</span>";
        String aForm = "<span style='color: " + awayColor + "; font-size: 10px;'>[" + (away.getForm() > 0 ? "+" : "") + away.getForm() + "]</span>";

        String resultHtml;
        resultHtml =  "<tr>" +
                "<td style='padding: 4px; text-align: right; width: 40%;'>" + home.getName() + " " + hForm + "</td>" +
                "<td style='padding: 4px; text-align: center; font-weight: bold; width: 20%; color: #f39c12;'>" + homeGoals + " : " + awayGoals + "</td>" +
                "<td style='padding: 4px; text-align: left; width: 40%;'>" + aForm + " " + away.getName() + "</td>" +
                "</tr>";
        if (!matchCommentary.isEmpty()) {
            resultHtml += "<tr style='background-color: #222;'><td colspan='3' style='text-align: center; color:" +
                    "#bdc3c7; font-size: 10px; font-style: italic;'>"+ "\uD83C\uDFA4   " + matchCommentary +"</td></tr>";
        }
        return resultHtml;
    }

    private int calculateGoalsPro(int attack, int defense) {
        int diff = attack - defense;
        int chancePerGoal = 50 + (diff / 2);

        if (chancePerGoal < 3) chancePerGoal = 3;
        if (chancePerGoal > 90) chancePerGoal = 90;

        int goals = 0;

        for (int i = 0; i < 5; i++) {
            int roll = ThreadLocalRandom.current().nextInt(1, 101);

            if (roll <= chancePerGoal) {
                goals++;
            }
        }

        if (ThreadLocalRandom.current().nextInt(1, 101) <= 3) {
            goals++;
        }
        return goals;
    }

}