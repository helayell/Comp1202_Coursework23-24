package facilities.buildings;

import facilities.Facility;

public class Hall extends Facility implements Building {

  static final int Max_Level = 4;
  static final int Base_Capacity = 6;
  public static final int Base_Building_Cost = 100;
  private int level;

  public Hall(String name) {
    super(name);
    this.level = 1; // The initial level is set to 1
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public void increaseLevel() {
    if (level < Max_Level) {
      level++; // Increases the level by 1. so if its 2 it would become 3
    } else {
      System.out.println("Hall is already at maximum level.");
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
    return Base_Capacity * (int) Math.pow(2, level - 1);
  }
}
