# Coursework23-24

Programming I 1202 Coursework 23-24


EcsSim University Simulation


All 6 parts were done from the coursework.


Overview


The sim is a Java-based program that manages a virtual university. It includes facility management, staff hiring, student enrollment, and financial calculations.


Components


* 'EcsSim.java': Main simulation class that handles yearly university operations.
* 'University.java': Represents the University, managing its budget, staff, and reputation.
* 'Staff.java': It's the class where staff get their attributes like name, skill, and years of teaching.
* Additional supporting classes for managing the Estate, Human resources, Facility, Buildings, etc.
* Hall.java, Lab.java, Theatre.java: Represent various facilities within the university.


How to Run the program


The program requires three parameters:


1. Path to the staff file.
2. Initial funding for the university.
3. Number of years to simulate.


Command Line Execution


Go to the directory that contains the 'EcsSim.java' and other related Java files, then compile and run the program using the following commands:


javac EcsSim.java
java EcsSim <staff-config-file> <initial-funding> <simulation-years>


Replace 'staff-config-file', 'initial-funding', and 'simulation-years' with your desired values. For example:


java EcsSim staff.txt 2000 50


This would run the simulation using staff.txt as the staff file, with an initial funding of 2000 and for 50 simulation years.
