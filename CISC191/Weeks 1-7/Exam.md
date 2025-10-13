# Task Version 1.0
Create a drup company's inventory control system. This includes Painkillers,Bandages, and Equipment. Then, write a parent class, and three derived classes with using at least one override and overload method.
# Task Version 2.0
Enhancing version 1 by Exception class.
# Task Version 3.0
Enhancing version 1 or 2 of the ICS.
# My Code 
```java
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// complie and run inventoryappv2

public class InventoryAppV2 {

  static final class C {
static final int MAX_RETRIES = 5;
static final String DATE_FORMAT_EXAMPLE = "yyyy-MM-dd (e.g., 2027-03-15)";
static final String MENU = """

\n=== Drug Company Inventory Control System ===
1) Add item
2) Display all items
3) Update an item
4) Exit
Choose: """;
static final String SECTION_PROMPT = """
\nChoose section:
a) Painkillers
b) Bandages
c) Equipment
Section: """;
}

// prevent typos
enum AgeGroup { ADULT, CHILD }

// continue

private final List<InventoryItem> items = new ArrayList<>();
private final Scanner sc = new Scanner(System.in);

public static void main(String[] args) {
new InventoryAppV2().run();
}

private void run() {
boolean running = true;
while(running) {
System.out.print(C.MENU);
String choice = sc.nextLine().trim();
switch (choice) {
case "1" -> addItem();
case "2" -> listItems();
case "3" -> updateItem();
case "4" -> running = false;
default -> System.out.println("Invalid choice. Please select 1-4.");
}
}
System.out.println("Goodbye.");
}


// menu
private void addItem() {
    System.out.print(C.SECTION_PROMPT);
    String section = sc.nextLine().trim().toLowerCase();

    InventoryItem item = switch (section) {
        case "a" -> new Painkiller();
        case "b" -> new Bandage();
        case "c" -> new Equipment();
        default -> null;
    };
    if (item == null) {
        System.out.println("Unknown section.");
        return;
    }

    try {
        item.updateFromScanner(sc);
        items.add(item);
        System.out.println("Item added.");
    } catch (ExpiredItemException e) {
        System.out.println("Custom validation: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid value: " + e.getMessage());
    }
}

private void listItems() {
    if (items.isEmpty()) {
        System.out.println("\nNo items in inventory.");
        return;
    }
    System.out.println("\n--- Inventory ---");
    for (int i = 0; i < items.size(); i++) {
        System.out.println("[" + i + "] " + items.get(i));
    }
}

private void updateItem() {
    if (items.isEmpty()) {
        System.out.println("No items to update.");
        return;
    }
    listItems();
    System.out.print("Enter index to update: ");
    try {
        int idx = Integer.parseInt(sc.nextLine().trim());
        if (idx < 0 || idx >= items.size()) {
            System.out.println("Index out of range.");
            return;
        }
        items.get(idx).updateFromScanner(sc, true);
        System.out.println("Item updated.");
    } catch (NumberFormatException e) {
        System.out.println("Please enter a whole-number index (e.g., 0, 1, 2).");
    } catch (ExpiredItemException e) {
        System.out.println("Custom validation: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid value: " + e.getMessage());
    }
}
}

// version 1
abstract class InventoryItem {
private String name;
private String company;

public void updateFromScanner(Scanner sc) throws ExpiredItemException {
updateFromScanner(sc, true);
}

public void updateFromScanner(Scanner sc, boolean promptAll) throws ExpiredItemException {
this.name = Input.readString(sc, "Enter item name", this.name, promptAll);
this.company = Input.readString(sc, "Enter company name", this.company, promptAll);
}

protected abstract String typeName();
protected String getName() { return name; }
protected void setName(String name) { this.name = name; }
protected String getCompany() { return company; }
protected void setCompany(String company) { this.company = company; }

@Override
public String toString() {
return typeName() + " | Name:" + Input.safe(name) + " | Company:" + Input.safe(company);
}
}

// painkillers

class Painkiller extends InventoryItem {
private LocalDate expiry;
private InventoryAppV2.AgeGroup ageGroup;

@Override protected String typeName() { return "Painkiller"; }

@Override
public void updateFromScanner(Scanner sc, boolean promptAll) throws ExpiredItemException {
super.updateFromScanner(sc, promptAll);
this.expiry = Input.readDate(sc,
"Enter expiry date (" + InventoryAppV2.C.DATE_FORMAT_EXAMPLE + ")",
this.expiry, promptAll, true);
this.ageGroup = Input.readAgeGroup(sc, "Enter age group (ADULT/CHILD)",
this.ageGroup, promptAll);
}
@Override
public String toString() {
return super.toString()
+ " | Expiry:" + (expiry == null ? "(unset)" : expiry)
+ " | AgeGroup:" + (ageGroup == null ? "(unset)" : ageGroup);
}
}

// bandges

class Bandage extends InventoryItem {
private LocalDate expiry;
private InventoryAppV2.AgeGroup ageGroup;
private boolean waterproof;

@Override protected String typeName() { return "Bandage"; }

@Override
public void updateFromScanner(Scanner sc, boolean promptAll) throws ExpiredItemException {
super.updateFromScanner(sc, promptAll);
this.expiry = Input.readDate(sc,
"Enter expiry date (" + InventoryAppV2.C.DATE_FORMAT_EXAMPLE + ")",
this.expiry, promptAll, true);
this.ageGroup = Input.readAgeGroup(sc, "Enter age group (ADULT/CHILD)",
this.ageGroup, promptAll);
this.waterproof = Input.readYesNo(sc, "Is waterproof? (Y/N)", this.waterproof, promptAll);
}

@Override
public String toString() {
return super.toString()
+ " | Expiry:" + (expiry == null ? "(unset)" : expiry)
+ " | AgeGroup:" + (ageGroup == null ? "(unset)" : ageGroup)
+ " | Waterproof:" + (waterproof ? "Y" : "N");

}
}

// equipment
class Equipment extends InventoryItem {
private Double weightLbs;

@Override protected String typeName() { return "Equipment"; }

@Override
public void updateFromScanner(Scanner sc, boolean promptAll) {
super.updateFromScanner(sc, promptAll);
this.weightLbs = Input.readPositiveDouble(sc,
"Enter item weight (lbs)", this.weightLbs, promptAll);
}

@Override
public String toString() {
return super.toString()
+ " | Weight(lbs):" + (weightLbs == null ? "(unset)" : weightLbs);
}
}

// version 2
final class Input {
private Input() {}

static String safe(String s) { return s == null ? "(unset)" : s; }

static String readString(Scanner sc, String label, String current, boolean promptAll) {
if (!promptAll && current != null) return current;
for (int tries = 0; tries < InventoryAppV2.C.MAX_RETRIES; tries++) {
System.out.print(label + (current != null ? " [" + current + "]" : "") + ": ");
String s = sc.nextLine().trim();
if (s.isEmpty()) return current;
return s;
}
throw new IllegalArgumentException("Too many invalid attempts for text input.");
}

static LocalDate readDate(Scanner sc, String label, LocalDate current,
boolean promptAll, boolean checkNotPast) throws ExpiredItemException {
if (!promptAll && current != null) return current;
for (int tries = 0; tries < InventoryAppV2.C.MAX_RETRIES; tries++) {
System.out.print(label + (current != null ? " [" + current + "]" : "") + ": ");
String s = sc.nextLine().trim();
if (s.isEmpty()) return current;
try {
LocalDate d = LocalDate.parse(s);
if (checkNotPast && d.isBefore(LocalDate.now())) {
throw new ExpiredItemException("Expiry date " + d + " is in the past.");
}
return d;
} catch (DateTimeParseException e) { // built-in (v2.0)
System.out.println("Invalid date format. Use " + InventoryAppV2.C.DATE_FORMAT_EXAMPLE + ".");
}
}
throw new IllegalArgumentException("Too many invalid attempts for date.");
}

// built in 2
static Double readPositiveDouble(Scanner sc, String label, Double current, boolean promptAll) {
if (!promptAll && current != null) return current;
for (int tries = 0; tries < InventoryAppV2.C.MAX_RETRIES; tries++) {
System.out.print(label + (current != null ? " [" + current + "]" : "") + ": ");
String s = sc.nextLine().trim();
if (s.isEmpty()) return current;
try {
double val = Double.parseDouble(s);  // may throw NumberFormatException (v2.0)
if (val <= 0) throw new IllegalArgumentException("Value must be > 0.");
return val;
} catch (NumberFormatException e) {
System.out.println("Invalid number. Example valid weight: 0.75");
} catch (IllegalArgumentException e) {
System.out.println(e.getMessage());
            }
        }
throw new IllegalArgumentException("Too many invalid attempts for numeric input.");
}

static InventoryAppV2.AgeGroup readAgeGroup(Scanner sc, String label,
InventoryAppV2.AgeGroup current, boolean promptAll) {
if (!promptAll && current != null) return current;
for (int tries = 0; tries < InventoryAppV2.C.MAX_RETRIES; tries++) {
System.out.print(label + (current != null ? " [" + current + "]" : "") + ": ");
String s = sc.nextLine().trim().toUpperCase();
if (s.isEmpty()) return current;
if (s.equals("ADULT")) return InventoryAppV2.AgeGroup.ADULT;
if (s.equals("CHILD")) return InventoryAppV2.AgeGroup.CHILD;
System.out.println("Please enter ADULT or CHILD.");
}
throw new IllegalArgumentException("Too many invalid attempts for age group.");
    }
static boolean readYesNo(Scanner sc, String label, boolean current, boolean promptAll) {
if (!promptAll) return current;
for (int tries = 0; tries < InventoryAppV2.C.MAX_RETRIES; tries++) {
System.out.print(label + " [" + (current ? "Y" : "N") + "]: ");
String s = sc.nextLine().trim().toUpperCase();
if (s.isEmpty()) return current;
if(s.equals("Y")) return true;
if(s.equals("N")) return false;
System.out.println("Please enter Y or N.");
}
throw new IllegalArgumentException("Too many invalid attempts for Y/N input.");
}
}
// version 3 exception
class ExpiredItemException extends Exception {
public ExpiredItemException(String message) { super(message); }
}


```
