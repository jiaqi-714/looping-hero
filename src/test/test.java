package test;

import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random rand = new Random(5);
        for(int i = 0; i < 7; i++) {
            System.out.println(rand.nextInt(4));
        }
    }
}
