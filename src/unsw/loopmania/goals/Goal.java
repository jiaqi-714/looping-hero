package unsw.loopmania.goals;

import org.json.JSONObject;

import unsw.loopmania.LoopManiaWorld;

public class Goal implements GoalNode {
    private LoopManiaWorld world;
    private JSONObject jsonGoals;
    private String type;
    
    public Goal(JSONObject jsonGoals, LoopManiaWorld world) {
        this.world = world;
        this.jsonGoals = jsonGoals;
        type = (String) jsonGoals.get("goal");
    }

    public boolean compute() {
        // System.out.println("Computing " + jsonGoals);
        switch (type) {
            case "experience":
                return world.isExperienceGoal((Integer)jsonGoals.get("quantity"));            
            case "gold":
                return world.isGoldGoal((Integer)jsonGoals.get("quantity"));
            case "cycles":
                return world.isCyclesGoal((Integer)jsonGoals.get("quantity"));
            case "bosses":
                return world.isBosses();
            default:
                System.out.println("Not a valid goal");
                return false;
        }
    }

    public String print() {
        switch (type) {
            case "experience":
                return ("Collect " + jsonGoals.get("quantity") + " experience");            
            case "gold":
                return ("Collect " + jsonGoals.get("quantity") + " gold");
            case "cycles":
                return ("Complete " + jsonGoals.get("quantity") + " cycles");
            case "bosses":
                return "Kill all bosses";
            default:
                return "Not a valid goal";
        }
    }
}
