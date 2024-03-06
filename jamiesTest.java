import java.util.Random;

public class jamiesTest {

    static Random R = new Random();
    
    public static void main(String[] args) {
        Rollin rand = new RandomRoller();
        Rollin jamies = new JamiesAttempt();

        int[] d = new int[6];
        for(int i = 0; i < d.length; i++) {
            d[i] = R.nextInt(6) + 1;
        }

        System.out.println("Initial dice: " + java.util.Arrays.toString(d));
        int roll = R.nextInt(6) + 1;
        System.out.println("Roll: " + roll);
        //System.out.println("Outs: " + jamies.handleRoll(roll, d));
        

        while (!Rollin.isComplete(d)) {
            roll = R.nextInt(6) + 1;
            System.out.println("Roll: " + roll);
            int toChange = rand.handleRoll(roll, d);
            System.out.println("RandomRoller changes: " + toChange);
            //int toChange = jamies.handleRoll(roll, d);
            System.out.println("indice change: " + toChange);
            d[toChange] = roll;
            System.out.println("Dice: " + java.util.Arrays.toString(d));
            //System.out.println("indice to change: " + jamies.handleRoll(roll, d));
        }
    }
  
}

