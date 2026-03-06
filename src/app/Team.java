package app;

public class Team {
    private String name;
    private int attack_lvl;
    private int defense_lvl;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;
    private int points;
    private int goal_scored;
    private int goal_conceded;
    private int matches_played = 0;
    private int form = 0;


    public Team(String name, int Atack_lvl, int Defense_lvl) {
             this.name = name;
             this.attack_lvl = Atack_lvl;
             this.defense_lvl = Defense_lvl;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public void M_Result(int scored, int conceded) {
        goal_scored += scored;
        goal_conceded += conceded;
        this.matches_played++;

        if (scored > conceded) {
            this.points += 3;
            this.wins++;
            if (form < 3){
                form++;
            }
        }else if (scored == conceded) {
            this.points += 1;
            this.draws++;
            if (form > 0){
                form--;
            }else if (form < 0){
                form++;
            }
        }else {
            this.losses++;
            if(form > -3) {
                form--;
            }
        }
    }

//Gettery
    public int getGoal_difference() {
        return this.goal_scored - this.goal_conceded;
    }
    public String getName() {
        return name;
    }
    public int getPoints() {
        return points;
    }
    public int getGoal_conceded() {
        return goal_conceded;
    }
    public int getGoal_scored() {
        return goal_scored;
    }
    public int getAtack_lvl() {
        return attack_lvl;
    }
    public int getDefense_lvl() {
        return defense_lvl;
    }
    public int getMatches_played() {
        return matches_played;
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


}

