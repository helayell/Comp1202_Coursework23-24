package university;

public class Staff {
  private String name;
  private int skill;
  private int yearsOfTeaching;
  private int stamina;

  public Staff(String name, int skill) {
    this.name = name;
    if (skill > 100) {
      this.skill = 100;
    } else {
      this.skill = skill;
    }
    this.yearsOfTeaching = 0;
    this.stamina = 100;
  }

  public int instruct(int numberOfStudents) {
    int reputation = (100 * skill) / (100 + numberOfStudents);

    if (this.skill < 100) {
      this.skill += 1;
    }
    this.stamina -= Math.ceil((double) numberOfStudents / (20 + skill)) * 20;
    this.stamina = Math.max(this.stamina, 0);
    return reputation;
  }

  public void replenishStamina() {
    this.stamina = Math.min(this.stamina + 20, 100);
  }

  public void increaseYearsOfTeaching() {
    this.yearsOfTeaching += 1;
  }

  public String getName() {
    return name;
  }

  public int getSkill() {
    return skill;
  }

  public int getStamina() {
    return stamina;
  }

  public int getYearsOfTeaching() {
    return yearsOfTeaching;
  }
}
