# Task 
To understand how subclasses and derived from the superclass. 
# Our FlowChart
 <img width="662" height="1127" alt="image" src="https://github.com/user-attachments/assets/f58a8028-edf6-4a1b-9deb-cf7e3a28cdcd" />

# What were my challenges? 
Understanding how inheritance allows a class to use another classes method.
# My code
```java
// code from file person.java

public class Person {
   private int ageYears;
   private String lastName;

   public void setName(String userName) {
      lastName = userName;
   }

   public void setAge(int numYears) {
      ageYears = numYears;
   }

   public void printAll() {
      System.out.print("Name: " + lastName);
      System.out.print(", Age: " + ageYears);
   }
}

// code from file Student.Java
public class Student extends Person {
private int idNum;

public void setID(int studentId) {
idNum = studentId;
}

public int getID() {
return idNum;
}
}

// code from StudentDerivationFromPerson.java
public class StudentDerivationFromPerson {
public static void main(String[] args) {
Student courseStudent = new Student();

// set fields using superclass method
courseStudent.setName("Smith");
courseStudent.setAge(20);

// subclass specific field
courseStudent.setID(9999);

// print all info (person method)
courseStudent.printAll();

//print ID separately
System.out.println(", ID: " + courseStudent.getID());
}
}
```

# My Video 
https://www.youtube.com/watch?v=X2-p2bRK9Tw
