import facilities.Facility;
import facilities.buildings.Building;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import university.Staff;
import university.University;

public class EcsSim {
  private final University university;
  private final List<Staff> staffMarket;

  public EcsSim(University university, List<Staff> staffMarket) {
    this.university = university;
    this.staffMarket = staffMarket;
  }

  public void simulate() {
    buildOrUpgradeFacilities();
    increaseBudget();
    hireStaff();
    allocateStaff();
    endOfYearFinances();
    YearEndReputation();
    increaseYearsOfTeaching();
    staffDeparture();
    replenishStamina();
  }

  private void buildOrUpgradeFacilities() {
    // Set it as false to keep track of it
    boolean upgraded = false;
    // Get all the facilities from the Estate
    Facility[] facilities = university.getEstate().getFacilities();

    // Attempts to upgrade existing facilities
    for (Facility facility : facilities) {
      // It checks if the facility is a building.
      if (facility instanceof Building building) {
        // Receives the upgrade cost for the buildings
        int upgradeCost = building.getUpgradeCost();
        // It checks if upgrade is possible and if the university can afford it
        if (upgradeCost != -1 && university.getBudget() >= upgradeCost) {
          try {
            // It tries to upgrade the building
            university.upgrade(building);
            /*
             It outputs a message for which building got upgraded.
             It is dynamic as well, so it can output any of the three building names.
             Also thought it would be simpler to just have the names of the class as the names of the building
             as I thought there's no need to give each new building a new name
            */
            System.out.println("Upgraded " + facility.getClass().getSimpleName());
            // Sets it true as one building got upgraded
            upgraded = true;
            // Breaks the loop as the method should only upgrade one building
            break;
            // If there's an exception (error) during upgrade, print the error message
          } catch (Exception e) {
            System.out.println("Upgrade failed: " + e.getMessage());
          }
        }
      }
    }

    /*
     Builds new facilities if no upgrades (or at max level) were done and budget allows
     It also checks each building's cost
    */
    if (!upgraded && university.getBudget() >= Hall.Base_Building_Cost) {
      buildNewFacility("Hall", Hall.Base_Building_Cost);
    }
    if (!upgraded && university.getBudget() >= Lab.Base_Building_Cost) {
      buildNewFacility("Lab", Lab.Base_Building_Cost);
    }
    if (!upgraded && university.getBudget() >= Theatre.Base_Building_Cost) {
      buildNewFacility("Theatre", Theatre.Base_Building_Cost);
    }
  }

  /* Builds new facilities based on the type that's called and the cost */
  private void buildNewFacility(String type, int cost) {
    if (university.getBudget() >= cost) {
      university.build(type, "New " + type);
      System.out.println("Built new " + type);
    }
  }

  private void increaseBudget() {
      /* Calculates the total capacity in the university */
    int totalCapacity = calculateTotalCapacity();

      /* Gets the current number of students */
    int currentStudents = university.getEstate().getNumberOfStudents();

      /* Calculate available space */
    int availableSpace = Math.max(0, totalCapacity - currentStudents);

      /* Enroll new students up to the available space */
    int newStudents = availableSpace;

      /* Update the number of students in the university estate */
    university.getEstate().setNumberOfStudents(currentStudents + newStudents);

      /* Calculate budget increase */
    final int contributionPerStudent = 10;
    int totalContribution = (currentStudents + newStudents) * contributionPerStudent;

      /* Update the university budget */
    university.setBudget(university.getBudget() + totalContribution);

      /* Print the results */
    System.out.println(
        "Enrolled "
            + newStudents
            + " new students. Total students: "
            + (currentStudents + newStudents));
    System.out.println(
        "Budget increased by " + totalContribution + ". New budget: " + university.getBudget());
  }

  private int calculateTotalCapacity() {
    int totalCapacity = 0;
    Facility[] facilities = university.getEstate().getFacilities();
    for (Facility facility : facilities) {
      if (facility instanceof Building building) {
        totalCapacity += building.getCapacity();
      }
    }
    return totalCapacity;
  }

  private float calculateSalaryForStaff(Staff staff) {
    Random random = new Random();
    // Adjusting the calculation to use skill as an integer
    return (9.5f + random.nextFloat()) * staff.getSkill();
  }

  private void hireStaff() {
    // Decided to use 20 students per 1 staff. I chose it at random.
    final int optimalStudentToStaffRatio = 20;

    // Gets the current number of students from the university
    int currentNumberOfStudents = university.getEstate().getNumberOfStudents();

    // Calculates the required staff based on the student/staff ratio
    int requiredStaff =
        (int) Math.ceil((double) currentNumberOfStudents / optimalStudentToStaffRatio);

    // Initializes a counter for the current staff count
    int currentStaffCount = 0;
    Iterator<Staff> currentStaffIterator = university.getHumanResource().getStaff();

    // Counts the existing staff members in the list
    while (currentStaffIterator.hasNext()) {
      currentStaffIterator.next();
      currentStaffCount++;
    }

    // Calculates how many more staff that are needed to meet the ratio
    int additionalStaffNeeded = requiredStaff - currentStaffCount;

    /*
     Iterate over the available staff in the staff market
     It will continue checking if there are more staff available to hire
     and if the university needs additional staff
    */
    Iterator<Staff> staffMarketIterator = staffMarket.iterator();
    while (staffMarketIterator.hasNext() && additionalStaffNeeded > 0) {
      Staff staff = staffMarketIterator.next();

      // Calculate the salary for a staff member
      float salary = calculateSalaryForStaff(staff);

      // Checks if the university has enough budget to hire more staff
      if (university.getBudget() >= salary) {
        // Gets added to the university's human resource
        university.getHumanResource().addStaff(staff);

        // Deducts the salary from the university's budget
        university.setBudget(university.getBudget() - salary);

        // Outputs the new hire
        System.out.println("Hired staff: " + staff.getName() + " with salary: " + salary);

        /*
         Removes the hired staff from the staff market
         and decrease the count of additional staff needed
        */
        staffMarketIterator.remove();
        additionalStaffNeeded--;
      }
    }

    // Alerts if the ratio is not met
    if (additionalStaffNeeded > 0) {
      System.out.println("Warning: Not enough staff hired.");
    }
  }

  /* Allocates staff to students */
  private void allocateStaff() {
    int totalReputationPoints = 0;
    int unallocatedStudents = university.getEstate().getNumberOfStudents();
    /* Iterates over each staff that's employed */
    Iterator<Staff> staffIterator = university.getHumanResource().getStaff();
    while (staffIterator.hasNext()) {
      Staff staff = staffIterator.next();
      /* It stops allocating if all students already have a teacher */
      if (unallocatedStudents <= 0) break;
      /* Determines the number of students a staff can handle
      they can handle 20 maximum*/
      int numberOfStudents = Math.min(20, unallocatedStudents);
      int reputationPoints = staff.instruct(numberOfStudents);

      totalReputationPoints += reputationPoints;
      unallocatedStudents -= numberOfStudents;
    }
    // Adjust reputation based on unallocated students
    if (unallocatedStudents > 0) {
      System.out.println("Warning: " + unallocatedStudents + " students remain unallocated.");
      totalReputationPoints -= unallocatedStudents;
    }

    university.setReputation(university.getReputation() + totalReputationPoints);
  }

  private void endOfYearFinances() {
    float AmountEYF =
        university.getEstate().getMaintenanceCost()
            + university.getHumanResource().getTotalSalary();
    float EYFBudget = university.getBudget() - AmountEYF;

    System.out.println("Budget after deducting year worth of Maintenance and Salary:" + EYFBudget);
  }

  private void YearEndReputation() {
    System.out.println("End of Year Reputation: " + university.getReputation());
  }

  private void increaseYearsOfTeaching() {
    Iterator<Staff> staffIterator;
    staffIterator = university.getHumanResource().getStaff();

    while (staffIterator.hasNext()) {
      Staff staff = staffIterator.next();
      staff.increaseYearsOfTeaching();
    }
  }

  private void staffDeparture() {
    Iterator<Staff> staffIterator = university.getHumanResource().getStaff();

    while (staffIterator.hasNext()) {
      Staff staff = staffIterator.next();
      int yearsOfTeaching = staff.getYearsOfTeaching();
      int stamina = staff.getStamina();
      // removes staff if more than 30 years of studying or less than 50% stamina
      if (yearsOfTeaching >= 30 || (stamina <= 0 && Math.random() < 0.5)) {
        staffIterator.remove();
        System.out.println(staff.getName() + " has left the university.");
      }
    }
  }

  private void replenishStamina() {
    for (Iterator<Staff> it = university.getHumanResource().getStaff(); it.hasNext(); ) {
      Staff staff = it.next();
      staff.replenishStamina();
    }
  }

  public void simulate(int years) {
    for (int year = 0; year < years; year++) {
      System.out.println("Year " + (year + 1) + " simulation starts:");

      simulate();

        /* Pause for half a second between each year */
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        System.out.println("Simulation interrupted.");
        break; // Optionally break the simulation if interrupted
      }

      System.out.println("Year " + (year + 1) + " simulation ends.");
    }
  }

  private static List<Staff> readStaffConfig(String filename) throws FileNotFoundException {
    List<Staff> staffList = new ArrayList<>();

    /* Opens the file and scans it */
    Scanner scanner = new Scanner(new File(filename));

    /* Keep reading the file as long as there are more lines. */
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      line = line.trim();

        /* Finds where the parenthesis starts. This separates the name from the skill level. */
      int indexOfParenthesis = line.indexOf('(');

        /* Extracts the name */
      String name = line.substring(0, indexOfParenthesis).trim();

        /* Extracts the skill level */
      int skill =
          Integer.parseInt(line.substring(indexOfParenthesis + 1, line.length() - 1).trim());

      /* Creates a new staff member with the extracted name
      and skill level and adds them to the list */
      staffList.add(new Staff(name, skill));
    }
    scanner.close();

    return staffList;
  }

  public static void main(String[] args) {
    try {
      if (args.length != 3) {
        System.out.println(
            "Usage: java EcsSim <staff-config-file> <initial-funding> <simulation-years>");
        return;
      }

      String staffConfigFile = args[0];
      int initialFunding = Integer.parseInt(args[1]);
      int simulationYears = Integer.parseInt(args[2]);

      List<Staff> staffMarket = readStaffConfig(staffConfigFile);
      University university = new University(initialFunding);
      EcsSim simulation = new EcsSim(university, staffMarket);

      simulation.simulate(simulationYears);
    } catch (FileNotFoundException e) {
      System.out.println("Error: File not found - " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
