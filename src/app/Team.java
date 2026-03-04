package app;

public class Team {
    private String name;
    private int Atack_lvl;
    private int Defense_lvl;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;
    private int points;
    private int Goal_scored;
    private int Goal_conceded;
    private int Matches_Played = 0;


    public Team(String name, int Atack_lvl, int Defense_lvl) {
             this.name = name;
             this.Atack_lvl = Atack_lvl;
             this.Defense_lvl = Defense_lvl;
    }


    public void M_Result(int scored, int conceded) {
        Goal_scored += scored;
        Goal_conceded += conceded;
        this.Matches_Played++;

        if (scored > conceded) {
            this.points += 3;
            this.wins++;
        }else if (scored == conceded) {
            this.points += 1;
            this.draws++;
        }else {
            this.losses++;
        }
    }

    public int getGoal_difference() {
        return this.Goal_scored - this.Goal_conceded;
    }

//Gettery
    public String getName() {
        return name;
    }
    public int getPoints() {
        return points;
    }
    public int getGoal_conceded() {
        return Goal_conceded;
    }
    public int getGoal_scored() {
        return Goal_scored;
    }
    public int getAtack_lvl() {
        return Atack_lvl;
    }
    public int getDefense_lvl() {
        return Defense_lvl;
    }
    public int getMatches_played() {
        return Matches_Played;
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
}

