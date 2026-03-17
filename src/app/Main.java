package app;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Nie udało się załadować stylu okien: " + e.getMessage());
		}
		UIManager.put("OptionPane.background", new Color(30, 30, 30));
		UIManager.put("Panel.background", new Color(30, 30, 30));
		UIManager.put("OptionPane.messageForeground", new Color(255, 255, 255));

		LeagueManager manager = new LeagueManager();
		int currentMatchday = 1;
		Engine matchEngine = new Engine();
		File saveFile = new File("savegame.csv");

		if (saveFile.exists()) {
			String loadMessage = "<html><body style='color: #ffffff; text-align: center; font-family: sans-serif; padding: 10px;'>"
					+ "<h3 style='color: #ff9800; margin-top: 0;'>Wykryto plik zapisu!</h3>"
					+ "<p style='color: #cccccc;'>Znaleziono zapisany stan ligi z poprzedniej sesji.<br>Czy chcesz kontynuować ten sezon?</p>"
					+ "</body></html>";

			int response = JOptionPane.showConfirmDialog(null, loadMessage, "Menedżer Zapisów", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				currentMatchday = manager.loadGame();
				manager.setCurrentRoundIndex(currentMatchday - 1);
			} else {
				manager.loadfile("teams.csv");
			}
		} else {
			manager.loadfile("teams.csv");
		}

		manager.generateFixtures();
		boolean isRunning = true;

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

			final int[] userChoice = {-1};

			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.setBackground(new Color(30, 30, 30));

			JLabel menuLabel = new JLabel(newMenu);
			menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
			menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			mainPanel.add(menuLabel);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

			JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
			buttonPanel.setBackground(new Color(30, 30, 30));
			buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

			JPanel wrapperPanel = new JPanel(new FlowLayout());
			wrapperPanel.setBackground(new Color(30, 30, 30));
			wrapperPanel.add(buttonPanel);

			String[] buttonNames = {
					"Zobacz Tabelę",
					"Rozegraj Kolejkę",
					"Centrum Treningowe - Sklep",
					"Symuluj do końca",
					"Historia Wyników (Przeglądarka)",
					"Zapisz i Wyjdź z gry"
			};

			for (int i = 0; i < buttonNames.length; i++) {
				JButton btn = new JButton(buttonNames[i]);
				btn.setBackground(new Color(70, 70, 70));
				btn.setForeground(Color.WHITE);
				btn.setFocusPainted(false);
				btn.setFont(new Font("SansSerif", Font.BOLD, 14));
				btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
				final int index = i;
				btn.addActionListener(e -> {
					userChoice[0] = index;
					Window window = SwingUtilities.getWindowAncestor(btn);
					if (window != null) {
						window.dispose();
					}
				});
				buttonPanel.add(btn);
			}

			mainPanel.add(wrapperPanel);

			JOptionPane.showOptionDialog(null,
					mainPanel,
					"Mini Football Manager",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					new Object[]{},
					null
			);

			int choice = userChoice[0];

			if (choice == -1 || choice == 5) {
				JOptionPane.showMessageDialog(null, "Do zobaczenia na murawie!");
				manager.saveGame(currentMatchday);
				isRunning = false;
				continue;
			}

			if (choice == 0) {
				String as_string = manager.getTableAsString();
				JOptionPane.showMessageDialog(null, as_string, "Tabela Ligowa", JOptionPane.PLAIN_MESSAGE);
			} else if (choice == 1) { // Rozegraj Kolejkę
				if (currentMatchday > maxMatchdays) {
					JOptionPane.showMessageDialog(null, "Sezon się już zakończył!", "Koniec", JOptionPane.WARNING_MESSAGE);
				} else {
					String wynikiKolejki = manager.playNextRound(matchEngine);
					JOptionPane.showMessageDialog(null, wynikiKolejki, "Wyniki Kolejki nr " + currentMatchday, JOptionPane.PLAIN_MESSAGE);
					currentMatchday++;

					if (currentMatchday > maxMatchdays) {
						JOptionPane.showMessageDialog(null, "Gratulacje! Zakończyłeś sezon.", "Koniec Sezonu", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} else if (choice == 2) { // Centrum Treningowe - Sklep
				manager.getTeams();
				String[] teamNames = new String[manager.getTeamsCount()];
				for (int i = 0; i < manager.getTeamsCount(); i++) {
					teamNames[i] = manager.getTeams().get(i).getName();
				}
				Object selectedValue = JOptionPane.showInputDialog(null,
						"Wybierz drużynę do treningu:", "Centrum Treningowe",
						JOptionPane.QUESTION_MESSAGE, null, teamNames, teamNames[0]);
				if (selectedValue != null) {
					String chosenTeamName = (String) selectedValue;
					for (Team team : manager.getTeams()) {
						if (team.getName().equals(chosenTeamName)) {
							int attackCost = team.getAtackLvl() * 5000;
							int defenseCost = team.getDefenseLvl() * 5000;
							JOptionPane.showMessageDialog(null, "Wybrano: " + team.getName() + " | Budżet: " + team.getBudget());
							Object[] options = {"Trening Ataku (+1) - " + attackCost + "$", "Trening Obrony (+1) - " + defenseCost + "$", "Wyjście"};
							int action = JOptionPane.showOptionDialog(null,
									"Budżet: " + team.getBudget() + "$\nWybierz trening dla " + team.getName() + ":",
									"Centrum Treningowe",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,
									options,
									options[2]);

							if (action == 0) {
								if (team.getAtackLvl() >= 99) {
									JOptionPane.showMessageDialog(null, "Maksymalny poziom ataku (99) osiągnięty!");
								} else if (team.getBudget() >= attackCost) {
									team.setBudget(team.getBudget() - attackCost);
									team.setAttackLvl(team.getAtackLvl() + 1);
									JOptionPane.showMessageDialog(null, "Transakcja udana! Nowy atak: " + team.getAtackLvl() + " | Zostało: " + team.getBudget() + "$");
								} else {
									JOptionPane.showMessageDialog(null, "Brak środków! Potrzebujesz: " + attackCost + "$");
								}
							}

							if (action == 1) {
								if (team.getDefenseLvl() >= 99) {
									JOptionPane.showMessageDialog(null, "Maksymalny poziom obrony (99) osiągnięty!");
								} else if (team.getBudget() >= defenseCost) {
									team.setBudget(team.getBudget() - defenseCost);
									team.setDefenseLvl(team.getDefenseLvl() + 1);
									JOptionPane.showMessageDialog(null, "Transakcja udana! Nowa obrona: " + team.getDefenseLvl() + " | Zostało: " + team.getBudget() + "$");
								} else {
									JOptionPane.showMessageDialog(null, "Brak środków! Potrzebujesz: " + defenseCost + "$");
								}
							}
						}
					}
				}
			} else if (choice == 3) { // Symuluj do końca
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
			} else if (choice == 4) { // Historia Wyników (Przeglądarka)
				if (currentMatchday == 1) {
					JOptionPane.showMessageDialog(null, "Historia jest pusta. Rozegraj najpierw kolejkę!", "Błąd", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						if (Desktop.isDesktopSupported()) {
							Desktop desktop = Desktop.getDesktop();
							File file = new File("historia_sezonu.html");
							if (file.exists()) {
								desktop.browse(file.toURI());
							} else {
								JOptionPane.showMessageDialog(null, "Nie znaleziono pliku historii!", "Błąd", JOptionPane.WARNING_MESSAGE);
							}
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Nie udało się otworzyć przeglądarki: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
}
