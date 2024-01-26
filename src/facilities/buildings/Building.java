package facilities.buildings;

public interface Building {

  // Return the current level of the building
  int getLevel();

  // Increase the level of the building
  void increaseLevel();

  /* Return the cost for upgrading the building to the next level.
  return -1 if building reaches max level */
  int getUpgradeCost();

  // Return the current capacity of the building
  int getCapacity();
}
