# Task

To define a method named getWordFrequency. This takes an array of strings, the size of the array, and a search word as parameters.
Our method then returns the number of occurences. Then, write a main program that reads a lists of words into an array, calls our method repeatedly, and outpuds the words with their frequencies.

# Our Flowchart
<img width="726" height="1059" alt="image" src="https://github.com/user-attachments/assets/7287bfab-3141-4d05-ac78-b5b94d69d88c" />

# How would I perform a frequency analysis of a website if I had to do it?
We would extract all the text from our site, split it into words, remove common words that are unecessary (like the or is), store these words into an array, use our method to count how many times they appear, then finally output the words with their frequencies.
# What does that tell us? 
Say I was researching about a website trying to learn what its about, key words and phrases help describe its contents and show a little how search engines like Google work on a much smaller scale.
# What were the challenges? 
What does a getWordFrequency do? How would it help me?

<img width="1141" height="916" alt="image" src="https://github.com/user-attachments/assets/523a0deb-b80a-4ea7-a48a-c4217e7b6d3a" />


# Video link
https://www.youtube.com/watch?v=ghnxt_DuyCE

# My code
```java
import java.util.Scanner;

public class WordFrequencies {

//our method to count the amount of times a word pops up

public static int getWordFrequency(String[] wordsList,int listSize, String currWord) {
int count = 0;
for (int i = 0; i < listSize; i++) {
if (wordsList[i]. equalsIgnoreCase(currWord)) {
count ++;
}
}
return count;
}

public static void main(String[] args) {
Scanner sc = new Scanner(System.in);

// input the words
String inputLine = sc.nextLine();
String[] wordsList = inputLine.split(" ");
int listSize = wordsList.length;

// output words with the amount the pop up
for (int i = 0; i < listSize; i++) {
int freq = getWordFrequency(wordsList, listSize, wordsList[i]);
System.out.println(wordsList[i] + " " + freq);
}
sc.close();
}
}
```
