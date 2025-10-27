# Task
Write a code which uses deque and tests whether a line of text is a palindrome. 
# Our Flowchart
<img width="411" height="1112" alt="image" src="https://github.com/user-attachments/assets/d2b1212e-53e8-4b2b-9bb7-99f309d91075" />

# My Challenges
Use a deque to understand the push from both ends.
# My Code
```java
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;

public class PalindromeChecker {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
System.out.print("Enter text: ");
String input = sc.nextLine();
sc.close();

// step 1
String clean = input.replaceAll("[^a-z]", "").toLowerCase();

//step 2
if (clean.length() <= 1) {
System.out.println("Yes, " + input + " is a palindrome.");
return;
}

//step 3
Deque<Character> deque = new ArrayDeque<>();
for (char c : clean.toCharArray()) {
deque.addLast(c);
}

//step 4
boolean isPalindrome = true;
while (deque.size() > 1) {
if (!deque.removeFirst().equals(deque.removeLast())) {
isPalindrome = false;
break;
}
}

//step 5
if (isPalindrome) {
System.out.println("Yes, " + input + " is a palindrome.");
} else {
System.out.println("No, \"" + input + "\" is not a palindrome.");
}
}
}
```
# Video
https://www.youtube.com/watch?v=h46Pu8KJt5g
