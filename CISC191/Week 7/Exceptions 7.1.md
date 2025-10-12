# Task 
Create Step Counter Exceptions 
# Our FlowChart
<img width="678" height="1098" alt="image" src="https://github.com/user-attachments/assets/d1ca7dfb-bb04-4ab6-8f03-2017571ea46e" />

# What were our challenges?
Where the handle the invalid input, the exceptions make it more clear to debug. 
# My Code
```java
import java.util.Scanner;

public class StepsToMiles {
/**
*covert steps to miles using 2000 steps to one mile
*/
public static double stepsToMiles(int steps) throws Exception {
if (steps < 0) {
throw new Exception("Exception: Negative step count entered.");
}
return steps / 2000.0;
}

public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
try {
int steps = sc.nextInt();
double miles = stepsToMiles(steps);
System.out.printf("%.2f", miles);
} catch (Exception e) {
System.out.println(e.getMessage());
} finally {
sc.close();
}
}
}
```
# My Video
https://www.youtube.com/watch?v=YyYBzF51waI
