import java.util.Random;

public class Test {

    static Random R = new Random();

    public static void main(String[] args) {
        boolean testIndividual = false;

        Rollin individual = new AvalancheM();
        String individualName = "ProbSel";

        if (testIndividual) {

            // New stuff
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }
    
            System.out.println("Initial dice: " + java.util.Arrays.toString(d));
            int roll = R.nextInt(6) + 1;
            System.out.println("Roll: " + roll);

            int re = individual.handleRoll(roll, d);
            if (re == -1) {
                System.out.println("Initial Six were complete");
            }
            d[re] = roll;
            System.out.println(individualName + " changes: index " + re);
            System.out.println("Dice: " + java.util.Arrays.toString(d));
    
            System.out.println("Running random until complete...");
            while (!Rollin.isComplete(d)) {
                roll = R.nextInt(6) + 1;
                System.out.println("Roll: " + roll);
                // int toChange = rand.handleRoll(roll, d);
                int toChange = individual.handleRoll(roll, d);
                // System.out.println("RandomRoller changes: " + toChange);
                System.out.println(individualName + " changes: index " + toChange);
                d[toChange] = roll;
                System.out.println("Dice: " + java.util.Arrays.toString(d));
            }

        } else {
            int numOfTrials = 1000000;

            runTestRandom(numOfTrials);
            runTestAvalanche(numOfTrials);
            runTestPomo(numOfTrials);
            runTestProbSel(numOfTrials);
        }        
    }

    // Test block for comparing average runs int terms of number of swaps
    public static void runTestRandom(int numOfTrials) {

        Rollin rand = new RandomRoller();
        double totalRolls = 0;
        int alreadyComplete = 0;

        for (int j = 0; j < numOfTrials; j++) {
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }

            int roll = R.nextInt(6) + 1;
            // intialise dice
            int re = rand.handleRoll(roll, d);
            if (re == -1) {
                // was already complete
                alreadyComplete++;
            } else {
                d[re] = roll;
                totalRolls++;
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = rand.handleRoll(roll, d);
                    d[toChange] = roll;
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for rand = " + totalRolls / numOfTrials);
        System.out.println("total already complete = " + alreadyComplete);
    }

    public static void runTestAvalanche(int numOfTrials) {
        Rollin aval = new Avalanche();
        double totalRolls = 0;
        int alreadyComplete = 0;

        for (int j = 0; j < numOfTrials; j++) {
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }

            int roll = R.nextInt(6) + 1;
            // intialise dice
            int re = aval.handleRoll(roll, d);
            if (re == -1) {
                // was already complete
                alreadyComplete++;
            } else {
                d[re] = roll;
                totalRolls++;
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = aval.handleRoll(roll, d);
                    d[toChange] = roll;
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for aval = " + totalRolls / numOfTrials);
        System.out.println("total already complete = " + alreadyComplete);
    }

    public static void runTestPomo(int numOfTrials) {
        Rollin Pomo = new PlusOrMinusOne();
        double totalRolls = 0;
        int alreadyComplete = 0;

        for (int j = 0; j < numOfTrials; j++) {
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }

            int roll = R.nextInt(6) + 1;
            // intialise dice
            int re = Pomo.handleRoll(roll, d);
            if (re == -1) {
                // was already complete
                alreadyComplete++;
            } else {
                d[re] = roll;
                totalRolls++;
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = Pomo.handleRoll(roll, d);
                    d[toChange] = roll;
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for pomo = " + totalRolls / numOfTrials);
        System.out.println("total already complete = " + alreadyComplete);
    }

    public static void runTestProbSel(int numOfTrials) {
        Rollin probSel = new AvalancheM();
        double totalRolls = 0;
        int alreadyComplete = 0;

        for (int j = 0; j < numOfTrials; j++) {
            int[] d = new int[6];
            for (int i = 0; i < d.length; i++) {
                d[i] = R.nextInt(6) + 1;
            }

            int roll = R.nextInt(6) + 1;
            // intialise dice
            int re = probSel.handleRoll(roll, d);
            if (re == -1) {
                // was already complete
                alreadyComplete++;
            } else {
                d[re] = roll;
                totalRolls++;
                while (!Rollin.isComplete(d)) {
                    roll = R.nextInt(6) + 1;
                    int toChange = probSel.handleRoll(roll, d);
                    d[toChange] = roll;
                    totalRolls++;
                }
            }
        }
        System.out.println("Average rolls for probSel = " + totalRolls / numOfTrials);
        System.out.println("total already complete = " + alreadyComplete);
    }

}
