package university;

import facilities.Facility;

import facilities.buildings.Building;

public class University {
  private float budget;
  private Estate estate;
  private int reputation;
  private HumanResource humanResource;

  public University(int funding) {
    this.budget = funding;
    this.estate = new Estate();
    this.reputation = 0;
    humanResource = new HumanResource();

  }

  // Build a new Facility and adds 100 rep.
  public Facility build(String type, String name) {
    Facility facility = estate.addFacility(type, name);
    if (facility != null && facility instanceof Building) {
      Building building = (Building) facility;
      int buildingCost = building.getUpgradeCost();
      if (buildingCost != -1) {
        this.budget -= buildingCost;
        this.reputation += 100;
      }
    }
    return facility;
  }

  public void upgrade(Building building) throws Exception {
    if (!estate.contains(building)) {
      throw new Exception("Building is not part of the university.");
    }

    int upgradeCost = building.getUpgradeCost();
    if (upgradeCost == -1) {
      throw new Exception("Building is already at maximum level.");
    }
    this.budget -= upgradeCost; // Deduct the cost before increasing the level
    building.increaseLevel(); // Now increase the level
    this.reputation += 50;
  }


  public float getBudget() {
    return this.budget;
  }

  public void setBudget(float budget) {
    this.budget = budget;
  }

  public int getReputation() {
    return this.reputation;
  }
  public Estate getEstate() {
    return this.estate;
  }
  public HumanResource getHumanResource(){
    return this.humanResource;
  }

  public void setReputation(int reputation) {
    this.reputation = reputation;
  }
}
