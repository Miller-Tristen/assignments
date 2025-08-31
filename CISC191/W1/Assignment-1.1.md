# Draw a flow chart of my thought process


<img width="386" height="876" alt="image" src="https://github.com/user-attachments/assets/139aeeb8-34e5-4fed-bbac-7c9b27b9a0c5" />



# Explain which algorithm we selected 
I selected bubble sorting after following up the different sorts. our three most common are
![OIP](https://github.com/user-attachments/assets/dc8326e7-e32b-4b54-b2dd-c2808a27f0f7)
![OIP](https://github.com/user-attachments/assets/629ef501-c363-4b96-8ccf-b75aa2eb1b06)
![download](https://github.com/user-attachments/assets/bf353797-6585-4500-ac02-faa227a8ce39)


# What were my challenges?  
These included, which algorithm we wanted to use, how best to explain, and how I want to explain it.



# The code
```java
import java.util.Scanner;

// this is for our labsorting 1.1 sorting class

public class LabSorting {

public static void sortArray(int[] myArr, int arrSize) {

for (int i = 0; i < arrSize - 1; i++) {
for(int j = 0; j < arrSize - 1 - i; j++) {
if (myArr[j] < myArr[j + 1]) {

// to swap them

int temp = myArr[j];
myArr [j] = myArr[j + 1];
myArr[j + 1] = temp;
}
}
}
}

public static void main(String [] args) {
Scanner sc = new Scanner(System.in);

// read the size
int arrSize = sc.nextInt();
int[] numbers = new int[arrSize];

//now read into the array
for (int i = 0; i < arrSize; i++) {
numbers[i] = sc.nextInt();
}

//Call
sortArray(numbers, arrSize);

//Output
for (int i = 0; i < arrSize; i++) {
if (i > 0) System.out.print(",");
System.out.print(numbers[i]);
}
System.out.println();
}
}
```

#upload video 


https://www.youtube.com/watch?v=JfaHR3tRXWk


