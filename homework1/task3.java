import java.util.Scanner;

public class Main
{
        public static void main(String[] args) {
            try (Scanner in = new Scanner(System.in)) {
                int n = in.nextInt();
            int[] A = new int[n];
            for (int i = 0; i < n; ++i) {
                A[i] = in.nextInt();
            }

            int m = in.nextInt();
            int[] B = new int[m];
            for (int i = 0; i < m; ++i) {
                B[i] = in.nextInt();
            }

            int k = in.nextInt();

            int i = 0;
            int j = m - 1;
            int pairCount = 0;
            while (j >= 0) {
                while (i < n && A[i] + B[j] < k) {
                    ++i;
                }
                if (i == n) {
                    System.out.println(pairCount);
                    return;
                }
                if (A[i] + B[j] == k) {
                    ++pairCount;
                }
                --j;
            }

                System.out.println(pairCount);
            }
        }
}

