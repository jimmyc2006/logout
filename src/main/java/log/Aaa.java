package log;

public class Aaa {

    public static void main(String[] args) {
        Aaa a = new Aaa();
        a.test(15);
    }

    public void aa(int sum) {
        int currSum = 0;
        int start = 1;
        int i = 1;
        while (i <= sum && start <= i) {
            if (currSum == sum) {
                System.out.println(start + " 到 " + (i - 1));
                currSum += i;
                i++;
            } else if (currSum > sum) {
                currSum = currSum - start;
                start++;
            } else {
                currSum += i;
                i++;
            }
        }
    }

    private void test(int a) {
        int sum = 0;
        int left = 1;
        for (int i = left; i <= (a - 1) / 2 + 1; i++) {
            sum += i;
            while (sum > a) {
                sum -= left;
                left++;
            }
            if (sum == a) {
                for (int j = i; j >= left; j--) {
                    System.out.print(" " + j);
                }
                System.out.println();
            }
        }
    }
}
