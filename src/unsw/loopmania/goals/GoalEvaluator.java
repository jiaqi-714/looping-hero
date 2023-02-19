package unsw.loopmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.loopmania.LoopManiaWorld;

public class GoalEvaluator {
    private GoalNode expression;

    public GoalEvaluator(JSONObject jsonGoals, LoopManiaWorld world) {
        String goalCond = (String) jsonGoals.get("goal");
        if (goalCond.equals("AND")) {
            JSONArray jsonSubgoals = jsonGoals.getJSONArray("subgoals");
            this.expression = new AndGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else if (goalCond.equals("OR")) {
            JSONArray jsonSubgoals = jsonGoals.getJSONArray("subgoals");
            this.expression = new OrGoalNode(jsonSubgoals.getJSONObject(0), jsonSubgoals.getJSONObject(1), world);
        } else {
            // pass whole goal
            this.expression = new Goal(jsonGoals, world);
        }
    }
    
    public boolean evaluate() {
        return this.expression.compute();
    }

    public String prettyPrint() {
        return this.expression.print();
    }
}
