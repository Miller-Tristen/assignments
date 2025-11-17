# Task
Read size of an integer array, output the array, perform an insertion sort on the array, and finally output the number of comparisons and swaps performed.
# What were my challenges?
Using an insertion sort needs to sort whats wrong and make it right. Understanding this is a key value to code it.
# Our Flowchart
<img width="252" height="964" alt="image" src="https://github.com/user-attachments/assets/edb57c8c-0d73-46fe-8bcb-d25b27d3ffb7" />

# Code
```java
import java.util.Scanner;

public class InsertionSortActivity {

// helper method

public static void printArray(int[] numbers) {
for (int i = 0; i < numbers.length; i++) {
if (i > 0) {
  System.out.print(" ");
}
  System.out.print(numbers[i]);
}
  System.out.println();
}

// main

public static void main(String[] args) {
Scanner sc = new Scanner(System.in);

// read array size
int n = sc.nextInt();

// read elements of array

int[] numbers = new int[n];
for (int i = 0; i < n; i++) {
numbers [i] = sc.nextInt();
}

// print original array
printArray(numbers);
  System.out.println();

int comparisons = 0;
int swaps = 0;

for (int i = 1; i < numbers.length; i++) {
int j = i;
while (j > 0) {

comparisons++;

if (numbers[j] < numbers[j - 1]) {

int temp = numbers[j];
numbers[j] = numbers[j - 1];
numbers[j - 1] = temp;

swaps++;

j--;
} else {

break;
}
}

  printArray(numbers);
}

  System.out.println();

  System.out.println("comparisons: " + comparisons);
  System.out.println("swaps: " + swaps);
}
}

```
# Video
https://www.youtube.com/watch?v=ZQQDjbcZGt0
