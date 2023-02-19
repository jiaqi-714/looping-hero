package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.goals.GoalEvaluator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoalsTest {
    public static JSONObject parseJSON(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            return new JSONObject(content);
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    public void simpleGoalTest() {
        // JSONObject simpleGoal = parseJSON("21T2-cs2511-project\\worlds\\world_with_twists_and_turns.json");
        JSONObject simpleGoal = new JSONObject();
        simpleGoal.put("goal", "experience");
        simpleGoal.put("quantity", 123456);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(simpleGoal, testWorld);
        assertEquals(testGoals.prettyPrint(), "Collect 123456 experience");
    }

    @Test
    public void simpleAndGoalTest() {
        JSONObject json = new JSONObject();
        json.put("goal", "AND");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "experience");
        goal1.put("quantity", 9999);
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "gold");
        goal2.put("quantity", 1234);
        array.put(goal1);
        array.put(goal2);
        json.put("subgoals", array);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(json, testWorld);
        assertEquals(testGoals.prettyPrint(), "(Collect 9999 experience AND Collect 1234 gold)");
    }

    @Test
    public void simpleOrGoalTest() {
        JSONObject json = new JSONObject();
        json.put("goal", "OR");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "cycles");
        goal1.put("quantity", 1299);
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "gold");
        goal2.put("quantity", 1534);
        array.put(goal1);
        array.put(goal2);
        json.put("subgoals", array);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(json, testWorld);
        assertEquals(testGoals.prettyPrint(), "(Complete 1299 cycles OR Collect 1534 gold)");
    }

    @Test 
    public void complexAndGoalTest() {
        JSONObject json = new JSONObject();
        json.put("goal", "AND");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "cycles");
        goal1.put("quantity", 1299);
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "AND");
        JSONArray g2array = new JSONArray();
        JSONObject g2a = new JSONObject();
        g2a.put("goal", "bosses");
        JSONObject g2b = new JSONObject();
        g2b.put("goal", "gold");
        g2b.put("quantity", 123456);
        g2array.put(g2a);
        g2array.put(g2b);
        array.put(goal1);
        goal2.put("subgoals", g2array);
        array.put(goal2);
        json.put("subgoals", array);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(json, testWorld);
        assertEquals(testGoals.prettyPrint(), "(Complete 1299 cycles AND (Kill all bosses AND Collect 123456 gold))");
    }
    
    @Test
    public void complexOrGoalTest() {
        JSONObject json = new JSONObject();
        json.put("goal", "OR");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "bosses");
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "OR");
        JSONArray g2array = new JSONArray();
        JSONObject g2a = new JSONObject();
        g2a.put("goal", "gold");
        g2a.put("quantity", 500);
        JSONObject g2b = new JSONObject();
        g2b.put("goal", "bosses");
        g2array.put(g2a);
        g2array.put(g2b);
        array.put(goal1);
        goal2.put("subgoals", g2array);
        array.put(goal2);
        json.put("subgoals", array);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(json, testWorld);
        assertEquals(testGoals.prettyPrint(), "(Kill all bosses OR (Collect 500 gold OR Kill all bosses))");
    }

    @Test
    public void complexMixedGoalTest() {
        JSONObject json = new JSONObject();
        json.put("goal", "OR");
        JSONArray array = new JSONArray();
        JSONObject goal1 = new JSONObject();
        goal1.put("goal", "bosses");
        JSONObject goal2 = new JSONObject();
        goal2.put("goal", "AND");
        JSONArray g2array = new JSONArray();
        JSONObject g2a = new JSONObject();
        g2a.put("goal", "gold");
        g2a.put("quantity", 500);
        JSONObject g2b = new JSONObject();
        g2b.put("goal", "experience");
        g2b.put("quantity", 999999);
        g2array.put(g2a);
        g2array.put(g2b);
        array.put(goal1);
        goal2.put("subgoals", g2array);
        array.put(goal2);
        json.put("subgoals", array);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(json, testWorld);
        assertEquals(testGoals.prettyPrint(), "(Kill all bosses OR (Collect 500 gold AND Collect 999999 experience))");
    }

    @Test
    public void allGoalsTest() {
        JSONObject goal1 = new JSONObject().put("goal", "cycles");
        goal1.put("quantity", 1234);
        JSONObject goal2 = new JSONObject().put("goal", "experience");
        goal2.put("quantity", 4321);
        JSONObject goal3 = new JSONObject().put("goal", "gold");
        goal3.put("quantity", 1111);
        JSONObject goal4 = new JSONObject().put("goal", "bosses");

        JSONObject finalGoal = new JSONObject().put("goal", "AND");
        JSONObject g1 = new JSONObject().put("goal", "AND");
        JSONObject g2 = new JSONObject().put("goal", "AND");

        JSONArray g1array = new JSONArray();
        g1array.put(goal1);
        g1array.put(goal2);
        g1.put("subgoals", g1array);

        JSONArray g2array = new JSONArray();
        g2array.put(goal3);
        g2array.put(goal4);
        g2.put("subgoals", g2array);

        JSONArray finalArray = new JSONArray();
        finalArray.put(g1);
        finalArray.put(g2);
        finalGoal.put("subgoals", finalArray);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(finalGoal, testWorld);
        assertEquals(testGoals.prettyPrint(), "((Complete 1234 cycles AND Collect 4321 experience) AND (Collect 1111 gold AND Kill all bosses))");
    }

    @Test
    public void allOrGoalsTest() {
        JSONObject goal1 = new JSONObject().put("goal", "cycles");
        goal1.put("quantity", 1234);
        JSONObject goal2 = new JSONObject().put("goal", "experience");
        goal2.put("quantity", 4321);
        JSONObject goal3 = new JSONObject().put("goal", "gold");
        goal3.put("quantity", 1111);
        JSONObject goal4 = new JSONObject().put("goal", "bosses");

        JSONObject finalGoal = new JSONObject().put("goal", "OR");
        JSONObject g1 = new JSONObject().put("goal", "OR");
        JSONObject g2 = new JSONObject().put("goal", "OR");

        JSONArray g1array = new JSONArray();
        g1array.put(goal1);
        g1array.put(goal2);
        g1.put("subgoals", g1array);

        JSONArray g2array = new JSONArray();
        g2array.put(goal3);
        g2array.put(goal4);
        g2.put("subgoals", g2array);

        JSONArray finalArray = new JSONArray();
        finalArray.put(g1);
        finalArray.put(g2);
        finalGoal.put("subgoals", finalArray);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(finalGoal, testWorld);
        assertEquals(testGoals.prettyPrint(), "((Complete 1234 cycles OR Collect 4321 experience) OR (Collect 1111 gold OR Kill all bosses))");
    }

    @Test
    public void halfOrGoalsTest() {
        JSONObject goal1 = new JSONObject().put("goal", "cycles");
        goal1.put("quantity", 1234);
        JSONObject goal2 = new JSONObject().put("goal", "experience");
        goal2.put("quantity", 4321);
        JSONObject goal3 = new JSONObject().put("goal", "gold");
        goal3.put("quantity", 1111);
        JSONObject goal4 = new JSONObject().put("goal", "bosses");

        JSONObject finalGoal = new JSONObject().put("goal", "OR");
        JSONObject g1 = new JSONObject().put("goal", "AND");
        JSONObject g2 = new JSONObject().put("goal", "OR");

        JSONArray g1array = new JSONArray();
        g1array.put(goal1);
        g1array.put(goal2);
        g1.put("subgoals", g1array);

        JSONArray g2array = new JSONArray();
        g2array.put(goal3);
        g2array.put(goal4);
        g2.put("subgoals", g2array);

        JSONArray finalArray = new JSONArray();
        finalArray.put(g1);
        finalArray.put(g2);
        finalGoal.put("subgoals", finalArray);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(finalGoal, testWorld);
        assertEquals(testGoals.prettyPrint(), "((Complete 1234 cycles AND Collect 4321 experience) OR (Collect 1111 gold OR Kill all bosses))");
    }

    @Test
    public void halfAndGoalsTest() {
        JSONObject goal1 = new JSONObject().put("goal", "cycles");
        goal1.put("quantity", 1234);
        JSONObject goal2 = new JSONObject().put("goal", "experience");
        goal2.put("quantity", 4321);
        JSONObject goal3 = new JSONObject().put("goal", "gold");
        goal3.put("quantity", 1111);
        JSONObject goal4 = new JSONObject().put("goal", "bosses");

        JSONObject finalGoal = new JSONObject().put("goal", "AND");
        JSONObject g1 = new JSONObject().put("goal", "OR");
        JSONObject g2 = new JSONObject().put("goal", "AND");

        JSONArray g1array = new JSONArray();
        g1array.put(goal1);
        g1array.put(goal2);
        g1.put("subgoals", g1array);

        JSONArray g2array = new JSONArray();
        g2array.put(goal3);
        g2array.put(goal4);
        g2.put("subgoals", g2array);

        JSONArray finalArray = new JSONArray();
        finalArray.put(g1);
        finalArray.put(g2);
        finalGoal.put("subgoals", finalArray);
        LoopManiaWorld testWorld = helperCreateWorld();
        GoalEvaluator testGoals = new GoalEvaluator(finalGoal, testWorld);
        assertEquals(testGoals.prettyPrint(), "((Complete 1234 cycles OR Collect 4321 experience) AND (Collect 1111 gold AND Kill all bosses))");
    }
    
    public LoopManiaWorld helperCreateWorld() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(0, 0));
        return new LoopManiaWorld(6, 6, orderedPath);
    }
}
