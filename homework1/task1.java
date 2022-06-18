import java.util.Scanner;

public class Main
{
	public static void main(String[] args) {
		// prologue
		try (Scanner in = new Scanner(System.in)) {
		    int n = in.nextInt();
		    int[] A = new int[n];
		    for (int i = 0; i < n; ++i) {
		        A[i] = in.nextInt();
		    }
		    int[] B = new int[n];
		    for (int i = 0; i < n; ++i) {
		        B[i] = in.nextInt();
		    }
		
		    // body
		    int i0 = 0;
		    int j0 = 0;
		    int i = i0;
		    int j = j0;
		    int old_j = i;
		    while (j < n - 1) {
		        for (int k = old_j + 1; k <= j + 1; ++k) {
		            if (A[k] > A[i]) {
		                i = k;
		            }
		        }
		        if (A[i0] + B[j0] < A[i] + B[j + 1]) {
		            i0 = i;
		            j0 = j + 1;
		        }
		        old_j = j;
		        ++j;
		    }
		
		    // epilogue
		    System.out.println(i0 + " " + j0);
		}
	}
}

