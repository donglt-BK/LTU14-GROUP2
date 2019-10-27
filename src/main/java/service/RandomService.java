package service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomService {
    private static final Random r = new Random();

    public static int[] getRandomArray(int max, int arrayLength) {
        Set<Integer> rand = new HashSet<>();
        while (rand.size() < arrayLength) {
            rand.add(getRandomInteger(max));
        }
        return rand.stream().mapToInt(Number::intValue).sorted().toArray();
    }

    public static int getRandomInteger(int max) {
        return r.nextInt(max);
    }
}
