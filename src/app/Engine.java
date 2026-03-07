package app;
import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public class Engine {

    public String simulateMatch(Team home, Team away) {
        int matchHattack = home.getForm()*5 + home.getAtack_lvl();
        int matchAattack = away.getForm()*5 + away.getAtack_lvl();
        int matchHdefense = home.getForm()*2 + home.getDefense_lvl();
        int matchAdefense = away.getForm()*2 + away.getDefense_lvl();
        int homeGoals = calculateGoalsPro(matchHattack + 2,matchAdefense);
        int awayGoals = calculateGoalsPro(matchAattack,matchHdefense);
        int home_stats = matchHdefense + matchHattack;
        int away_stats = matchAdefense + matchAattack;
        int bonus_chances = ThreadLocalRandom.current().nextInt(1, 101);


        if (away_stats < home_stats) {
            if (bonus_chances <= 10) {
                awayGoals += 1;
            }
        }else if (home_stats < away_stats) {
            if (bonus_chances <= 10) {
                homeGoals += 1;
                if (homeGoals > awayGoals) {
                    System.out.println("Carried by their fans, " + home.getName() + "... win a match");
                }
            }
        }

        if (awayGoals > homeGoals) {
            System.out.println("Surprise!!! " + away.getName() + "... What a game!!");
        }

        home.M_Result(homeGoals, awayGoals);
        away.M_Result(awayGoals, homeGoals);
        // Logika kolorowania formy
        String homeColor = home.getForm() > 0 ? "#27ae60" : (home.getForm() < 0 ? "#e74c3c" : "#95a5a6");
        String awayColor = away.getForm() > 0 ? "#27ae60" : (away.getForm() < 0 ? "#e74c3c" : "#95a5a6");

        String hForm = "<span style='color: " + homeColor + "; font-size: 10px;'>[" + (home.getForm() > 0 ? "+" : "") + home.getForm() + "]</span>";
        String aForm = "<span style='color: " + awayColor + "; font-size: 10px;'>[" + (away.getForm() > 0 ? "+" : "") + away.getForm() + "]</span>";

        return "<tr>" +
                "<td style='padding: 4px; text-align: right; width: 40%;'>" + home.getName() + " " + hForm + "</td>" +
                "<td style='padding: 4px; text-align: center; font-weight: bold; width: 20%; color: #f39c12;'>" + homeGoals + " : " + awayGoals + "</td>" +
                "<td style='padding: 4px; text-align: left; width: 40%;'>" + aForm + " " + away.getName() + "</td>" +
                "</tr>";
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