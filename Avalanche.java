import java.util.Random;

public class Avalanche extends Rollin {

    Random R = new Random();

    public Avalanche() {

    }

    @Override
    public int handleRoll(int roll, int[] dice) {
        // Check for complete
        if (Rollin.isComplete(dice)) {
            return -1;
        }

        // Check brute force complete
        for (int i = 0; i < dice.length; i++) {
            int[] copy = CopyArray(dice);

            copy[i] = roll;
            if (Rollin.isComplete(copy)) {
                return i;
            }
        }

        return R.nextInt(6);
    }

    private int[] CopyArray(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i]; 
        }
        return b;
    }
}