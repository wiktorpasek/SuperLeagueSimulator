package app;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class LeagueManager {
    private final List<Team> teams = new ArrayList<Team>();
    public List<Team> getTeams() { return teams; }
    public String getTableAsString() {
        StringBuilder sb = new StringBuilder();

        teams.sort(Comparator.comparingInt(Team::getPoints)
                .thenComparingInt(Team::getGoalDifference)
                .thenComparingInt(Team::getGoalScored)
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
            sb.append("<td>").append(team.getMatchesPlayed()).append("</td>");
            sb.append("<td>").append(team.getWins()).append("</td>");
            sb.append("<td>").append(team.getDraws()).append("</td>");
            sb.append("<td>").append(team.getLosses()).append("</td>");
            sb.append("<td><b>").append(team.getPoints()).append("</b></td>");
            sb.append("<td>").append(team.getGoalDifference()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }

    private final List<List<Team[]>> fixtures = new ArrayList<>();
    private int currentRoundIndex = 0;

    public void generateFixtures() {
        initHistoryFile();
        int n = teams.size();
        int rounds = n - 1;
        int matchesPerRound = n / 2;

        List<List<Team[]>> firstHalf = new ArrayList<>();
        List<List<Team[]>> secondHalf = new ArrayList<>();

        List<Team> tempTeams = new ArrayList<>(teams);

        for (int i = 0; i < rounds; i++) {
            List<Team[]> roundFirstLeg = new ArrayList<>();
            List<Team[]> roundSecondLeg = new ArrayList<>();

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

    public String playNextRound(Engine engine) {
        if (currentRoundIndex >= fixtures.size()) {
            return null;
        }

        List<Team[]> matchday = fixtures.get(currentRoundIndex);

        StringBuilder tableHtml = new StringBuilder();
        tableHtml.append("<h2 style='text-align: center; color: #f39c12; margin-top: 0;'>Wyniki Kolejki ").append(currentRoundIndex + 1).append("</h2>");
        tableHtml.append("<table style='width: 100%; border-collapse: collapse; margin: 0 auto; max-width: 400px;'>");

        for (Team[] match : matchday) {
            Team home = match[0];
            Team away = match[1];
            tableHtml.append(engine.simulateMatch(home, away));
        }

        tableHtml.append("</table>");
        tableHtml.append("<p style='text-align: center; font-size: 9px; color: #7f8c8d; margin-top: 15px;'>* Liczba w nawiasie oznacza aktualną formę zespołu</p>");

        saveRoundToFile(tableHtml.toString());
        String guiHtml = "<html><body style='background-color: #2b2b2b; color: #ffffff; font-family: sans-serif; padding: 10px; width: 350px;'>" + tableHtml +
                "</body></html>";

        currentRoundIndex++;
        return guiHtml;
    }




    public void loadfile(String filePath) {
        try{
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));

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

    private void initHistoryFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("historia_sezonu.html", false))) {
            writer.println("<html>"+"<head>"+"<meta charset=\"UTF-8\"><body style='background-color: #2b2b2b; color: #ffffff; font-family: sans-serif; padding: 20px;'>");
            writer.println("<h1 style='text-align: center; color: #ff9800;'>HISTORIA SEZONU</h1>");
        } catch (Exception e) {
            System.out.println("Błąd tworzenia pliku: " + e.getMessage());
        }
    }

    private void saveRoundToFile(String roundHtml) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("historia_sezonu.html", true))) {
            writer.println(roundHtml);
            writer.println();
            writer.println("<br><hr style='border: 1px solid #555; width: 50%;'><br>");
        } catch (Exception e) {
            System.out.println("Błąd zapisu kolejki: " + e.getMessage());
        }
    }

    public void saveGame(int currentMatchday){
        try (PrintWriter writer = new PrintWriter(new FileWriter("savegame.csv", false))) {
            writer.println(currentMatchday);
            teams.forEach((team) -> {
                writer.println(team.getName() +","+team.getAtackLvl()+","+team.getDefenseLvl()+","+team.getPoints()
                        +","+team.getMatchesPlayed()+","+team.getWins()+","+team.getDraws()+","+team.getLosses()+","+
                        team.getGoalScored()+"," +team.getGoalConceded()+","+team.getForm()+"," +team.getBudget());
            });
        } catch (Exception e) {
            System.out.println("Błąd zapisu kolejki: " + e.getMessage());
        }
    }

    public void setCurrentRoundIndex(int currentRoundIndex) {
        this.currentRoundIndex = currentRoundIndex;
    }

    //DO NAUKI
    public int loadGame() {
        int savedMatchday = 1;

        try {
            List<String> lines = Files.readAllLines(java.nio.file.Paths.get("savegame.csv"));

            if (lines.isEmpty()) {
                System.out.println("Plik zapisu jest pusty!");
                return savedMatchday;
            }
            savedMatchday = Integer.parseInt(lines.get(0));

            teams.clear();

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                String teamName = parts[0];
                int attack = Integer.parseInt(parts[1]);
                int defense = Integer.parseInt(parts[2]);
                Team loadedTeam = new Team(teamName, attack, defense);
                loadedTeam.setPoints(Integer.parseInt(parts[3]));
                loadedTeam.setMatchesPlayed(Integer.parseInt(parts[4]));
                loadedTeam.setWins(Integer.parseInt(parts[5]));
                loadedTeam.setDraws(Integer.parseInt(parts[6]));
                loadedTeam.setLosses(Integer.parseInt(parts[7]));
                loadedTeam.setGoalScored(Integer.parseInt(parts[8]));
                loadedTeam.setGoalConceded(Integer.parseInt(parts[9]));
                loadedTeam.setForm(Integer.parseInt(parts[10]));
                loadedTeam.setBudget(Integer.parseInt(parts[11]));

                teams.add(loadedTeam);
            }

            System.out.println("Gra załadowana pomyślnie! Wracamy do kolejki nr: " + savedMatchday);

        } catch (Exception e) {
            System.out.println("Błąd wczytywania gry z pliku: " + e.getMessage());
        }
        return savedMatchday;
    }
}