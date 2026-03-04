package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        LeagueManager manager = new LeagueManager();
        Engine matchEngine = new Engine();

        manager.loadfile("teams.csv");
        manager.generateFixtures();

        javax.swing.UIManager.put("OptionPane.background", new java.awt.Color(30, 30, 30));
        javax.swing.UIManager.put("Panel.background", new java.awt.Color(30, 30, 30));

        boolean isRunning = true;
        int currentMatchday = 1;
        int maxMatchdays = (manager.getTeamsCount() - 1) * 2;

        while (isRunning) {
            int displayMatchday = Math.min(currentMatchday, maxMatchdays);
            String statusText = (currentMatchday > maxMatchdays) ? "<br><span style='color: #e74c3c; font-size: 12px;'>[ SEZON ZAKOŃCZONY ]</span>" : "";

            String newMenu = "<html><body style='color: #ffffff; text-align: center; font-family: sans-serif; padding: 10px;'>"
                    + "<h2 style='color: #ff9800; margin-bottom: 5px;'>SUPER LEAGUE</h2>"
                    + "<h3 style='margin-top: 0;'>Kolejka: <span style='color: #ff9800;'>" + displayMatchday + " / " + maxMatchdays + "</span>" + statusText + "</h3>"
                    + "<hr style='border: 1px solid #ff9800; width: 80%;'>"
                    + "<p style='color: #cccccc;'>Wybierz akcję:</p>"
                    + "</body></html>";

            javax.swing.JLabel menuLabel = new javax.swing.JLabel(newMenu);
            menuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            javax.swing.UIManager.put("OptionPane.messageForeground", new java.awt.Color(255, 255, 255));

            String[] options = {
                    "Zobacz Tabelę",
                    "Rozegraj Kolejkę",
                    "Symuluj do końca",
                    "Wyjdź z gry"
            };

            int choice = JOptionPane.showOptionDialog(
                    null,
                    menuLabel,
                    "Mini Football Manager",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == -1 || choice == 3) {
                JOptionPane.showMessageDialog(null, "Do zobaczenia na murawie!");
                isRunning = false;
                continue;
            }

            if (choice == 0) {
                String as_string = manager.getTableAsString();
                JOptionPane.showMessageDialog(null, as_string, "Tabela Ligowa", JOptionPane.PLAIN_MESSAGE);
            }

            else if (choice == 1) {
                if (currentMatchday > maxMatchdays) {
                    JOptionPane.showMessageDialog(null, "Sezon się już zakończył!", "Koniec", JOptionPane.WARNING_MESSAGE);
                } else {
                    manager.playNextRound(matchEngine);
                    JOptionPane.showMessageDialog(null, "Kolejka nr " + currentMatchday + " rozegrana!", "Wynik", JOptionPane.PLAIN_MESSAGE);
                    currentMatchday++;

                    if (currentMatchday > maxMatchdays) {
                        JOptionPane.showMessageDialog(null, "Gratulacje! Zakończyłeś sezon.", "Koniec Sezonu", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else if (choice == 2) {
                if (currentMatchday > maxMatchdays) {
                    JOptionPane.showMessageDialog(null, "Sezon się już zakończył!", "Koniec", JOptionPane.WARNING_MESSAGE);
                } else {
                    int symulowaneKolejki = 0;

                    while (currentMatchday <= maxMatchdays) {
                        manager.playNextRound(matchEngine);
                        currentMatchday++;
                        symulowaneKolejki++;
                    }

                    JOptionPane.showMessageDialog(null, "BŁYSKAWICZNA SYMULACJA!\nRozegrano " + symulowaneKolejki + " kolejek.\nSezon zakończony!", "Koniec Sezonu", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}