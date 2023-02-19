package test;

import java.util.List;

import org.javatuples.Pair;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.Character;
import unsw.loopmania.PathPosition;

public class Setup {


    // Create a 3x3 world that contains a small loop
    public LoopManiaWorld createWorld(List<Pair<Integer, Integer>> orderedPath) {
        
        orderedPath.add(Pair.with(0, 0));
        orderedPath.add(Pair.with(0, 1));
        orderedPath.add(Pair.with(0, 2));
        orderedPath.add(Pair.with(1, 2));
        orderedPath.add(Pair.with(2, 2));
        orderedPath.add(Pair.with(2, 1));
        orderedPath.add(Pair.with(2, 0));
        orderedPath.add(Pair.with(1, 0));

        return new LoopManiaWorld(3, 3, orderedPath);
    }

    // Create a 4x4 world that contains a loop
    public LoopManiaWorld createWorld2(List<Pair<Integer, Integer>> orderedPath) {
        
        orderedPath.add(Pair.with(0,0)); //top left to right
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2));
        orderedPath.add(Pair.with(0,3)); //top right to bottom right
        orderedPath.add(Pair.with(1,3));
        orderedPath.add(Pair.with(2,3));
        orderedPath.add(Pair.with(3,3)); //bottom right to left
        orderedPath.add(Pair.with(3,2));
        orderedPath.add(Pair.with(3,1));
        orderedPath.add(Pair.with(3,0)); //bottom left to top left
        orderedPath.add(Pair.with(2,0)); 
        orderedPath.add(Pair.with(1,0));
    
        return new LoopManiaWorld(4, 4, orderedPath);
    }

    // Create a 3x11 world that contains a loop
    public LoopManiaWorld createWorld3(List<Pair<Integer, Integer>> orderedPath) {
        
        orderedPath.add(Pair.with(0,0)); //top left to right
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2)); 
        orderedPath.add(Pair.with(0,3)); 
        orderedPath.add(Pair.with(0,4));
        orderedPath.add(Pair.with(0,6)); 
        orderedPath.add(Pair.with(0,7)); 
        orderedPath.add(Pair.with(0,8));
        orderedPath.add(Pair.with(0,9));
        orderedPath.add(Pair.with(0,10)); 
        orderedPath.add(Pair.with(1,10)); 
        orderedPath.add(Pair.with(2,10));
        orderedPath.add(Pair.with(2,9));
        orderedPath.add(Pair.with(2,8)); 
        orderedPath.add(Pair.with(2,7)); 
        orderedPath.add(Pair.with(2,6));
        orderedPath.add(Pair.with(2,5)); 
        orderedPath.add(Pair.with(2,4)); 
        orderedPath.add(Pair.with(2,3));
        orderedPath.add(Pair.with(2,2));
        orderedPath.add(Pair.with(2,1));
        orderedPath.add(Pair.with(2,0));
        orderedPath.add(Pair.with(1,0));
    
        return new LoopManiaWorld(3, 11, orderedPath);
    }

    // Create a 2x2 world that contains a loop
    public LoopManiaWorld createWorld4(List<Pair<Integer, Integer>> orderedPath) {
        
        orderedPath.add(Pair.with(0,0)); //top left to right
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(1,1)); 
        orderedPath.add(Pair.with(1,0)); 
       
    
        return new LoopManiaWorld(2, 2, orderedPath);
    }

    // Create a 5x5 world that contains a loop
    public LoopManiaWorld createWorld5(List<Pair<Integer, Integer>> orderedPath) {
        
        orderedPath.add(Pair.with(0,0)); //top left to right
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2));
        orderedPath.add(Pair.with(0,3)); 
        orderedPath.add(Pair.with(0,4)); //top right to bottom right
        orderedPath.add(Pair.with(1,4));
        orderedPath.add(Pair.with(2,4));
        orderedPath.add(Pair.with(3,4)); 
        orderedPath.add(Pair.with(4,4)); //bottom right to left
        orderedPath.add(Pair.with(4,3));
        orderedPath.add(Pair.with(4,2));
        orderedPath.add(Pair.with(4,1)); 
        orderedPath.add(Pair.with(4,0)); //bottom left to top left
        orderedPath.add(Pair.with(3,0));
        orderedPath.add(Pair.with(2,0));
        orderedPath.add(Pair.with(1,0));

    
        return new LoopManiaWorld(4, 4, orderedPath);
    }
    // Create a character and set it in the world at position (0,0)
    public Character createCharacter(List<Pair<Integer, Integer>> orderedPath, LoopManiaWorld world) {
        int currPos = orderedPath.indexOf(new Pair<Integer, Integer>(0, 0));
        PathPosition add = new PathPosition(currPos, orderedPath);
        Character c  = new Character(add);
        world.setCharacter(c);
        return c;
    }

    

}
