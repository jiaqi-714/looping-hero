package unsw.loopmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.loopmania.LoopManiaWorld;

public class OrGoalNode implements GoalNode {
    private GoalNode g1;
    private GoalNode g2;

    public OrGoalNode(JSONObject g1, JSONObject g2, LoopManiaWorld world) {
        String g1Cond = (String) g1.get("goal");
        if (g1Cond.equals("AND")) {
            JSONArray jsonSubgoals = g1.getJSONArray("subgoals");
            this.g1 = new AndGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else if (g1Cond.equals("OR")) {
            JSONArray jsonSubgoals = g1.getJSONArray("subgoals");
            this.g1 = new OrGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else {
            // pass whole goal
            this.g1 = new Goal(g1, world);
        }

        String g2Cond = (String) g2.get("goal");
        if (g2Cond.equals("AND")) {
            JSONArray jsonSubgoals = g2.getJSONArray("subgoals");
            this.g2 = new AndGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else if (g2Cond.equals("OR")) {
            JSONArray jsonSubgoals = g2.getJSONArray("subgoals");
            this.g2 = new OrGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else {
            // pass whole goal
            this.g2 = new Goal(g2, world);
        }
    }

    public boolean compute() {
        return (g1.compute() || g2.compute());
    }
    
    public String print() {
        return "(" + g1.print() + " OR " + g2.print() + ")";
    }
}