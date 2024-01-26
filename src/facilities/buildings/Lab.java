package facilities.buildings;

import facilities.Facility;

public class Lab extends Facility implements Building {

  static final int Max_Level = 5;
  static final int Base_Capacity = 5;
  public static final int Base_Building_Cost = 300;
  private int level;

  public Lab(String name) {
    super(name);
    this.level = 1;
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public void increaseLevel() {
    if (level < Max_Level) {
      level++;
    } else {
      System.out.println("Lab is already at maximum level.");
    }
  }

  @Override
  public int getUpgradeCost() {
    if (level < Max_Level) {
      return Base_Building_Cost * (level + 1);
    } else {
      return -1;
    }
  }

  @Override
  public int getCapacity() {
    return Base_Capacity
        * (int) Math.pow(2, level - 1);
  }
}
