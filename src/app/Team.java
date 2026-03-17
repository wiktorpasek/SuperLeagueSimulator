package app;

public class Team {
	private String name;
	private int attackLvl;
	private int defenseLvl;
	private int wins = 0;
	private int draws = 0;
	private int losses = 0;
	private int points;
	private int goalScored;
	private int goalConceded;
	private int matchesPlayed = 0;
	private int form = 0;
	private int budget = 0;

	public Team(String name, int attackLvl, int defenseLvl) {
		this.name = name;
		this.attackLvl = attackLvl;
		this.defenseLvl = defenseLvl;
	}

	public void setForm(int form) {
		this.form = form;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public void MatchResult(int scored, int conceded) {
		goalScored += scored;
		goalConceded += conceded;
		this.matchesPlayed++;

		if (scored > conceded) {
			this.budget += 100000;
			this.points += 3;
			this.wins++;
			if (form < 3) {
				form++;
			}
		} else if (scored == conceded) {
			this.budget += 30000;
			this.points += 1;
			this.draws++;
			if (form > 0) {
				form--;
			} else if (form < 0) {
				form++;
			}
		} else {
			this.losses++;
			if (form > -3) {
				form--;
			}
		}
	}

	// Settery
	public void setName(String name) {
		this.name = name;
	}

	public void setAttackLvl(int attackLvl) {
		this.attackLvl = attackLvl;
	}

	public void setDefenseLvl(int defenseLvl) {
		this.defenseLvl = defenseLvl;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setGoalScored(int goalScored) {
		this.goalScored = goalScored;
	}

	public void setGoalConceded(int goalConceded) {
		this.goalConceded = goalConceded;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	// Gettery
	public int getGoalDifference() {
		return this.goalScored - this.goalConceded;
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public int getGoalConceded() {
		return goalConceded;
	}

	public int getGoalScored() {
		return goalScored;
	}

	public int getAtackLvl() {
		return attackLvl;
	}

	public int getDefenseLvl() {
		return defenseLvl;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public int getDraws() {
		return draws;
	}

	public int getLosses() {
		return losses;
	}

	public int getForm() {
		return form;
	}

	public int getBudget() {
		return budget;
	}
}