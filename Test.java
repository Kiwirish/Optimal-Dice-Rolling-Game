import java.util.Random;

public class Test {

    static Random R = new Random();

    public static void main(String[] args) {
        boolean testIndividual = false;

        Rollin individual = new JamiesAttempt();
        // Rollin individual = new AvalancheM(true);
        String individualName = "ProbSel";

        if (testIndividual) {

            // New stuff
            int[] d = new int[6];
            boolean useRand = true;
            if (useRand) {
                for (int i = 0; i < d.length; i++) {
                    d[i] = R.nextInt(6) + 1;
                }
            } else {
                int[] f = { 1 , 1, 4, 1, 2, 6 };
                for (int i = 0; i < d.length; i++) {
                    d[i] = f[i];
                }
            }
            if (Rollin.isComplete(d)) {
                System.out.println("Complete in no steps");
                return;
            }


    
            System.out.println("Initial dice: " + java.util.Arrays.toString(d));
            int roll = R.nextInt(6) + 1;
            // roll = 6;
            System.out.println("Roll: " + roll);
            
            int re = individual.handleRoll(roll, d);
            if (re >= 0 && re <= 5) {
                d[re] = roll;
            } else {
                if (Rollin.isComplete(d)) {
                    System.out.println("Complete in one step");
                    return;
                }
            }
            // if (re == -1) {
            //     System.out.println("Complete in one step");
            //     return;
            // } else {
            //     d[re] = roll;
            // }
            
            System.out.println(individualName + " changes: index " + re);
            System.out.println("Dice: " + java.util.Arrays.toString(d));
    
            System.out.println("Running random until complete...");
            while (!Rollin.isComplete(d)) {
                roll = R.nextInt(6) + 1;
                System.out.println("Roll: " + roll);
                
                int toChange = individual.handleRoll(roll, d);
                
                System.out.println(individualName + " changes: index " + toChange);
                if (toChange >= 0 && toChange <= 5) {
                    d[toChange] = roll;
                }
                
                System.out.println("Dice: " + java.util.Arrays.toString(d));
            }

        } else {
            int numOfTrials = 1000000;

            // RunTestGeneric(numOfTrials, new RandomRoller(), "rand");
            // RunTestGeneric(numOfTrials, new Avalanche(), "Aval");
            // RunTestGeneric(numOfTrials, new PlusOrMinusOne(), "Pomo");
            // RunTestGeneric(numOfTrials, new AvalancheProbSel(), "ProbSel");
            // // RunTestGeneric(numOfTrials, new JamiesAttempt(), "Jamies"); // Has better worst case time then the other algorithms at ~1.37 ms but has the worst average case performance at ~0.018 ms which is ~4x slower then ProbSel and ~8x slower then Pomo
            // RunTestGeneric(numOfTrials, new FirstAttemptBlake(), "Blake");
            // RunTestGeneric(numOfTrials, new JamiesAttemptMod(), "JamiesMod");

            Rollin[] handlers = {
                new RandomRoller(),
                new Avalanche(),
                new PlusOrMinusOne(),
                new AvalancheProbSel(),
                new FirstAttemptBlake(),
                new JamiesAttemptMod(),
                new JamiesAttempt()
            };

            String[] names = {
                "Rand",
                "Aval",
                "Pomo",
                "ProbSel",
                "Blake",
                "JamiesMod",
                "JamiesOriginal"
            };

            RunTest(numOfTrials, handlers, names, true);
            // RunTestStatOutCSV(1000, handlers, names, true);

        }        
    }

    public static void RunTestGeneric(int numOfTrials, Rollin rollin, String name) {
        double totalRolls = 0;
        int alreadyComplete = 0;
        long worstRolls = 0;

        long totalTime = 0;
        long startTime = 0;
        long longestTime = 0;

        for (int j = 0; j < numOfTrials; j++) {
            long iterationRolls = 0;
            startTime = System.nanoTime();
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }
            int roll = 0;


            if (Rollin.isComplete(d)) {
                // was already complete
                alreadyComplete++;
            } else {
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    
                    int toChange = rollin.handleRoll(roll, d);
                    if (toChange >= 0 && toChange <= 5) {
                        d[toChange] = roll;
                    }
                    
                    totalRolls++;
                    iterationRolls++;
                }
            }

            long iterationTime = System.nanoTime() - startTime;
            if (iterationTime > longestTime) {
                longestTime = iterationTime;
            }

            if (iterationRolls > worstRolls) {
                worstRolls = iterationRolls;
            }

            totalTime += iterationTime;
        }

        // long totalTime = System.nanoTime() - startTime;
        // double timeMS = (double)totalTime / 1000000.0;

        System.out.println("--------");
        System.out.println("Average time (ms) for " + name + " = " + (((double)totalTime / (double)numOfTrials) / 1000000) + ". Total time (ms) = " + (totalTime / 1000000) + " # EFFICIENCY METRIC");
        System.out.println("Max time (ms) for " + name + " = " + ((double)longestTime / 1000000.0) + " # EFFICIENCY METRIC");

        System.out.println("Average rolls for " + name + " = " + totalRolls / (numOfTrials) + " # IMPORTANT PERFORMANCE METRIC");
        System.out.println("Average rolls for " + name + " (excluding immediately complete as 0 rolls) = " + totalRolls / (numOfTrials - alreadyComplete));
        System.out.println("Worst case rolls for " + name + " = " + worstRolls + " # IMPORTANT PERFORMANCE METRIC");
        System.out.println("Immediately Complete = " + alreadyComplete);
        System.out.println("--------");
    }

    public static void RunTest(int attemptNumOfTrials, Rollin[] handlers, String[] names, boolean includeEfficiencyMetrics) {

        long[] totalRolls = new long[handlers.length];
        long[] worstRolls = new long[handlers.length];

        long[] totalTime = new long[handlers.length];
        long[] worstTime = new long[handlers.length];

        long successfulTrials = 0;

        for (int trial = 0; trial < attemptNumOfTrials; trial++) {

            // Generate starting dice
            int[] startingDice = new int[6];
            for (int i = 0; i < startingDice.length; i++) {
                startingDice[i] = R.nextInt(6) + 1;
            }

            if (Rollin.isComplete(startingDice)) {
                continue; // Skip
            } else {
                successfulTrials++;
            }

            // Set dice of each of the handlers
            int[][] handlerDice = new int[handlers.length][6];

            for (int i = 0; i < handlers.length; i++) {
                handlerDice[i] = startingDice.clone();
            }

            boolean[] handlerComplete = new boolean[handlers.length];

            long[] iterationRolls = new long[handlers.length];
            long[] iterationTime = new long[handlers.length];

            while (!AllTrue(handlerComplete)) {
                // Whilst handlers aren't complete...

                // Roll a new dice
                int roll = R.nextInt(6) + 1;

                // Call each of the handlers
                for (int handlerID = 0; handlerID < handlers.length; handlerID++) {

                    if (handlerComplete[handlerID]) {
                        continue;
                    }

                    long startTime = System.nanoTime();

                    int toChange = handlers[handlerID].handleRoll(roll, handlerDice[handlerID]);
                    if (toChange >= 0 && toChange <= 5) {
                        handlerDice[handlerID][toChange] = roll;
                    }
    
                    long handleTime = System.nanoTime() - startTime;

                    iterationRolls[handlerID]++;
                    totalRolls[handlerID]++;
                    iterationTime[handlerID] += handleTime;


                    if (Rollin.isComplete(handlerDice[handlerID])) {
                        handlerComplete[handlerID] = true;
                    }
                }
            }

            // Do metrics

            for (int i = 0; i < handlers.length; i++) {

                totalTime[i] += iterationTime[i];

                if (iterationRolls[i] > worstRolls[i]) {
                    worstRolls[i] = iterationRolls[i];
                }

                if (iterationTime[i] > worstTime[i]) {
                    worstTime[i] = iterationTime[i];
                }
            }
        }


        // Print out metrics
        System.out.println("------------------------");
        for (int i = 0; i < handlers.length; i++) {
            System.out.println("Handler name: " + names[i]);
            System.out.println("Performance");
            System.out.println("  Average Rolls: " + ((double)totalRolls[i] / (double)successfulTrials));
            System.out.println("  Worst Rolls: " + worstRolls[i]);

            if (includeEfficiencyMetrics) {
                System.out.println("Efficiency");
                System.out.println("  Average Time: " + (((double)totalTime[i] / (double)successfulTrials) / 1000000.0) + " ms");
                System.out.println("  Worst Time: " + worstTime[i] / 1000000.0 + " ms");
            }

            System.out.println("------------------------");
        }

    }

    public static void RunTestStatOutCSV(int attemptNumOfTrials, Rollin[] handlers, String[] names, boolean includeEfficiencyMetrics) {

        long successfulTrials = 0;

        // Print names
        // StringBuilder sb = new StringBuilder();
        // for (int i = 0; i < handlers.length; i++) {
        //     sb.append(names[i]);
        //     sb.append("_Rolls");

        //     if (includeEfficiencyMetrics) {
        //         sb.append(", ");
        //         sb.append(names[i]);
        //         sb.append("_Time");
        //     }

        //     if (i < handlers.length - 1) {
        //         sb.append(", ");
        //     }
        // }
        // System.out.println(sb.toString()); // Print header

        System.out.println("Trial, Rolls, Algorithm_Type");

        for (int trial = 0; trial < attemptNumOfTrials; trial++) {

            long[] iterationRolls = new long[handlers.length];
            long[] iterationTime = new long[handlers.length];

            // Generate starting dice
            int[] startingDice = new int[6];
            for (int i = 0; i < startingDice.length; i++) {
                startingDice[i] = R.nextInt(6) + 1;
            }

            if (Rollin.isComplete(startingDice)) {
                continue; // Skip
            } else {
                successfulTrials++;
            }

            // Set dice of each of the handlers
            int[][] handlerDice = new int[handlers.length][6];

            for (int i = 0; i < handlers.length; i++) {
                handlerDice[i] = startingDice.clone();
            }

            boolean[] handlerComplete = new boolean[handlers.length];

            

            while (!AllTrue(handlerComplete)) {
                // Whilst handlers aren't complete...

                // Roll a new dice
                int roll = R.nextInt(6) + 1;

                // Call each of the handlers
                for (int handlerID = 0; handlerID < handlers.length; handlerID++) {

                    if (handlerComplete[handlerID]) {
                        continue;
                    }

                    long startTime = System.nanoTime();

                    int toChange = handlers[handlerID].handleRoll(roll, handlerDice[handlerID]);
                    if (toChange >= 0 && toChange <= 5) {
                        handlerDice[handlerID][toChange] = roll;
                    }
    
                    long handleTime = System.nanoTime() - startTime;

                    iterationRolls[handlerID]++;
                    iterationTime[handlerID] += handleTime;


                    if (Rollin.isComplete(handlerDice[handlerID])) {
                        handlerComplete[handlerID] = true;
                    }
                }
            }

            // Do metrics

            // StringBuilder trialDataSb = new StringBuilder();
            for (int i = 0; i < handlers.length; i++) {
                StringBuilder trialDataSb = new StringBuilder();

                trialDataSb.append(trial);
                trialDataSb.append(", ");
                trialDataSb.append(iterationRolls[i]);
                trialDataSb.append(", ");
                trialDataSb.append(names[i]);
                System.out.println(trialDataSb.toString());

                // if (includeEfficiencyMetrics) {
                //     trialDataSb.append(", ");
                //     trialDataSb.append(iterationTime[i]);
                // }

                // if (i < handlers.length - 1) {
                //     trialDataSb.append(", ");
                // }
            }

            // System.out.println(trialDataSb.toString());



            // for (int i = 0; i < handlers.length; i++) {

            //     totalTime[i] += iterationTime[i];

            //     if (iterationRolls[i] > worstRolls[i]) {
            //         worstRolls[i] = iterationRolls[i];
            //     }

            //     if (iterationTime[i] > worstTime[i]) {
            //         worstTime[i] = iterationTime[i];
            //     }
            // }
        }
    }

    private static boolean AllTrue(boolean[] all) {
        for (boolean item : all) {
            if (!item) {
                return false;
            }
        }

        return true;
    }

}
