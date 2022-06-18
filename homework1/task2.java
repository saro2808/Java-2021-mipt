import java.util.Scanner;

public class Main
{
    public static class Point {
        public int x;
        public int y;
    }
    
	public static void main(String[] args) {
	    try (Scanner in = new Scanner(System.in)) {
	        int n = in.nextInt();
		    Point[] Polygon = new Point[n];
		    for (int i = 0; i < n; ++i) {
		        Polygon[i] = new Point();
		        Polygon[i].x = in.nextInt();
		        Polygon[i].y = in.nextInt();
		    }
		    int S = 0;
		    for (int i = 0; i < n; ++i) {
		        S += (Polygon[(i + 1) % n].x - Polygon[i].x) * (Polygon[(i + 1) % n].y + Polygon[i].y);
		    }
		    double area = S / 2.0;
		    System.out.println(area);
	    }
	}
}

