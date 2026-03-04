package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeagueManager {
    private final List<Team> teams = new ArrayList<Team>();

    public String getTableAsString() {
        StringBuilder sb = new StringBuilder();

        teams.sort(Comparator.comparingInt(Team::getPoints)
                .thenComparingInt(Team::getGoal_difference)
                .thenComparingInt(Team::getGoal_scored)
                .reversed());

        sb.append("<html><body style='background-color: #2b2b2b; color: #ffffff; font-family: sans-serif; padding: 15px; width: 250px;'>");
        sb.append("<h2 style='text-align: center; color: #f39c12; margin-top: 0;'>SUPER LEAGUE</h2>");
        sb.append("<table style='width: 100%; text-align: left; border-collapse: collapse;'>");
        sb.append("<tr style='border-bottom: 2px solid #ffffff;'>");
        sb.append("<th style='padding-bottom: 5px;'>Msc</th><th>Drużyna</th><th>MR</th><th>W</th><th>R</th><th>P</th><th>Pkt</th><th>Bilans</th>");        sb.append("</tr>");

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            String rowStyle = "";
            if (i >= teams.size() - 3) {
                rowStyle = "style='color: #e74c3c; font-weight: bold;'";
            }
            if (i == 0) {
                rowStyle = "style='color: #33cc33; font-weight: bold;'";
            }
            sb.append("<tr ").append(rowStyle).append(">");
            sb.append("<td style='padding-top: 3px;'>").append(i + 1).append(".</td>");
            sb.append("<td>").append(team.getName()).append("</td>");
            sb.append("<td>").append(team.getMatches_played()).append("</td>");
            sb.append("<td>").append(team.getWins()).append("</td>");
            sb.append("<td>").append(team.getDraws()).append("</td>");
            sb.append("<td>").append(team.getLosses()).append("</td>");
            sb.append("<td><b>").append(team.getPoints()).append("</b></td>");
            sb.append("<td>").append(team.getGoal_difference()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }

    private final java.util.List<java.util.List<Team[]>> fixtures = new java.util.ArrayList<>();
    private int currentRoundIndex = 0;

    public void generateFixtures() {
        int n = teams.size();
        int rounds = n - 1;
        int matchesPerRound = n / 2;

        java.util.List<java.util.List<Team[]>> firstHalf = new java.util.ArrayList<>();
        java.util.List<java.util.List<Team[]>> secondHalf = new java.util.ArrayList<>();

        java.util.List<Team> tempTeams = new java.util.ArrayList<>(teams);

        for (int i = 0; i < rounds; i++) {
            java.util.List<Team[]> roundFirstLeg = new java.util.ArrayList<>();
            java.util.List<Team[]> roundSecondLeg = new java.util.ArrayList<>();

            for (int j = 0; j < matchesPerRound; j++) {
                int homeIdx = (i + j) % (n - 1);
                int awayIdx = (n - 1 - j + i) % (n - 1);

                if (j == 0) {
                    awayIdx = n - 1;
                }

                Team homeTeam = tempTeams.get(homeIdx);
                Team awayTeam = tempTeams.get(awayIdx);

                roundFirstLeg.add(new Team[]{homeTeam, awayTeam});
                roundSecondLeg.add(new Team[]{awayTeam, homeTeam});
            }
            firstHalf.add(roundFirstLeg);
            secondHalf.add(roundSecondLeg);
        }

        fixtures.addAll(firstHalf);
        fixtures.addAll(secondHalf);
    }

    public void playNextRound(Engine engine) {
        if (currentRoundIndex >= fixtures.size()) {
            return;
        }

        java.util.List<Team[]> matchday = fixtures.get(currentRoundIndex);

        for (Team[] match : matchday) {
            Team home = match[0];
            Team away = match[1];
            engine.simulateMatch(home, away);
        }

        currentRoundIndex++;
    }




    public void loadfile(String filePath) {
        try{
            java.util.List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));

            for(String line : lines){
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");

                String teamName = parts[0];
                int attack = Integer.parseInt(parts[1]);
                int defense = Integer.parseInt(parts[2]);

                teams.add(new Team(teamName,attack,defense));

            }

            System.out.println("OK u doing well");
        }

        catch(Exception e){
            System.out.println("ERROR!! "+ e.getMessage());
        }
    }
    public int getTeamsCount() {
        return teams.size();
    }
}