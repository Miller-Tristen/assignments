# Task 
Understand how to use overriding methods
# Our FlowChart
 <img width="343" height="654" alt="image" src="https://github.com/user-attachments/assets/da3330de-d955-4c15-986d-ca13fab7920f" />

# What were my challenges? 
keeping base vs derived responsiblities clear, and there is also a lot going on with this particular code. Making sure each part runs smoothly
# My code
```java
public class Book {
private String title;
private String author;
private String publisher;
private String publicationDate;

public void setTitle(String t) { title = t; }
public void setAuthor(String a) { author = a; }
public void setPublisher(String p) { publisher = p; }
public void setPublicationDate(String d) { publicationDate = d; }

public String getTitle() { return title; }
public String getAuthor() { return author; }
public String getPublisher() { return publisher; }
public String getPublicationDate() { return publicationDate; }

public void printInfo() {
System.out.println("Book Information: ");
System.out.println("   Book Title: " + title);
System.out.println("   Author: " + author);
System.out.println("   Publisher: " + publisher);
System.out.println("   Publication Date: " + publicationDate);
}
}

public class Encyclopedia extends Book {
private String edition;
private int numberOfPages;

public void setEdition(String e) { edition = e; }
public void setNumberOfPages(int n) { numberOfPages = n; }

public String getEdition() { return edition; }
public int getNumberOfPages() { return numberOfPages; }

@Override

public void printInfo() {
// reuse book printing, then add subclass specific details
super.printInfo();
System.out.println("   Edition: " + edition);
System.out.println("   Number of Pages: " + numberOfPages);
}
}

import java.util.Scanner;

public class BookDriver {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);

String b1Title = sc.nextLine();
String b1Author = sc.nextLine();
String b1Pub = sc.nextLine();
String b1Date = sc.nextLine();

String b2Title = sc.nextLine();
String b2Author = sc.nextLine();
String b2Pub = sc.nextLine();
String b2Date = sc.nextLine();
String b2Ed = sc.nextLine();
int    b2Pages = Integer.parseInt(sc.nextLine());

// base book
Book book1 = new Book();
book1.setTitle(b1Title);
book1.setAuthor(b1Author);
book1.setPublisher(b1Pub);
book1.setPublicationDate(b1Date);

Encyclopedia enc = new Encyclopedia();
enc.setTitle(b2Title);
enc.setAuthor(b2Author);
enc.setPublisher(b2Pub);
enc.setPublicationDate(b2Date);
enc.setEdition(b2Ed);
enc.setNumberOfPages(b2Pages);

System.out.println("Course Information:");
book1.printInfo();
enc.printInfo();

sc.close();
}
}



```

# My Video 
https://www.youtube.com/watch?v=n7pYBbU9MUE
