package university;

import java.util.HashMap;
import java.util.Random;

public class HumanResource {
  private HashMap<Staff, Float> staffSalary;

  public HumanResource() {
    staffSalary = new HashMap<>();
  }

  public void addStaff(Staff staff) {
    Random random = new Random();
    int salary = (int) ((9.5f + (10.5f - 9.5f) * random.nextFloat()) * staff.getSkill());
    staffSalary.put(staff, (float) salary);
  }

  public java.util.Iterator<Staff> getStaff() {
    return staffSalary.keySet().iterator();
  }

  public float getTotalSalary() {
    float totalSalary = 0;
    for (Float salary : staffSalary.values()) {
      totalSalary += salary;
    }
    return totalSalary;
  }
}
