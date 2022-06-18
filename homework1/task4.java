import java.util.Scanner;
import java.util.ArrayList;

public class Main
{
	public static void main(String[] args) {
	    try (Scanner in = new Scanner(System.in)) {
	        int N = in.nextInt();
	        int k = in.nextInt();
	    
	        ArrayList<Integer> list = new ArrayList<>();
	        for (int i = 1; i <= N; ++i) {
	            list.add(i);
	        }
	    
	        int currentIndex = (k - 1) % N;
	        while (N > 1) {
	            list.remove(currentIndex);
	            --N;
	            currentIndex = (currentIndex + k - 1) % N;
	        }
	    
	        System.out.println(list.get(0));
	    }
	}
}

