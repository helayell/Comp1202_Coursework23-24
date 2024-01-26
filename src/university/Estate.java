package university;

import facilities.Facility;
import facilities.buildings.Building;
import facilities.buildings.Hall;
import facilities.buildings.Lab;
import facilities.buildings.Theatre;

import java.util.ArrayList;

public class Estate {
  private ArrayList<Facility> facilities;
  private int additionalStudents;

  public Estate() {
    facilities = new ArrayList<>();
    additionalStudents = 0;
  }

  public Facility[] getFacilities() {
    return facilities.toArray(new Facility[0]);
  }

  public Facility addFacility(String type, String name) {
    Facility newFacility = createFacility(type, name);
    if (newFacility != null) {
      facilities.add(newFacility);
    }
    return newFacility;
  }

  private Facility createFacility(String type, String name) {
    type = type.toLowerCase();

    switch (type) {
      case "hall":
        return new Hall(name);
      case "lab":
        return new Lab(name);
      case "theatre":
        return new Theatre(name);
      default:
        System.out.println("Unknown facility type: " + type);
        return null;
    }
  }

  public boolean contains(Building facility) {
    return facilities.contains(facility);
  }

  public float getMaintenanceCost() {
    float totalMaintenanceCost = 0;
    for (Facility facility : facilities) {
      if (facility instanceof Building) {
        totalMaintenanceCost += 0.1 * ((Building) facility).getCapacity();
      }
    }
    return totalMaintenanceCost;
  }

  public int getNumberOfStudents() {
    int baseCapacity = calculateBaseCapacity();
    return baseCapacity + additionalStudents;
  }

  private int calculateBaseCapacity() {
    int totalCapacityHalls = 0;
    int totalCapacityLabs = 0;
    int totalCapacityTheatres = 0;

    for (Facility facility : facilities) {
      if (facility instanceof Hall) {
        totalCapacityHalls += ((Hall) facility).getCapacity();
      } else if (facility instanceof Lab) {
        totalCapacityLabs += ((Lab) facility).getCapacity();
      } else if (facility instanceof Theatre) {
        totalCapacityTheatres += ((Theatre) facility).getCapacity();
      }
    }
    return Math.min(totalCapacityHalls, Math.min(totalCapacityLabs, totalCapacityTheatres));
  }

  public void setNumberOfStudents(int additionalStudents) {
    int totalCapacity = calculateTotalCapacity();
    this.additionalStudents = Math.max(0, Math.min(additionalStudents, totalCapacity));
  }

  private int calculateTotalCapacity() {
    int totalCapacity = 0;
    for (Facility facility : facilities) {
      if (facility instanceof Building) {
        totalCapacity += ((Building) facility).getCapacity();
      }
    }
    return totalCapacity;
  }
}
