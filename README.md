# Airport Information Management Application
**Created by Jessica Zhou**

## Overview

This is an airport information management system. This program
mimics the internal operations that happen at an international
airport. It lets the user enter information about new passenger,
planes, and create new flights. Possible features include different
planes; adding passengers to the flight; listing all the passengers
on the aircraft; and searching up flights to a given destination.

## Setup and Configuration
1. **Dependencies**
   - Java JDK 8 or higher
   - Gradle 8.13 or higher

2. **Quick Start**
   ```bash
   # Build and run the application using Gradle
   ./gradlew run
   ```

3. **Available Gradle Commands**
   ```bash
   ./gradlew build     # Build the project
   ./gradlew run       # Run the application
   ./gradlew clean     # Clean build artifacts
   ./gradlew test      # Run tests
   ```

4. **Git Configuration**
   To ensure your commits are properly associated with your GitHub account:
   ```bash
   git config --global user.name "Your Name"
   git config --global user.email "your.github.email@example.com"
   ```

5. **Project Structure**
   ```
   src/
   ├── main/
   │   ├── java/      # Main application code
   │   ├── model/     # Data models
   │   ├── persistence/ # Data storage
   │   └── ui/        # User interface
   └── test/
       └── model/     # Test files
   ```

## Phase 0-2: User Stories

### What will the application do?
<p>This application stores information that the user inputs about
passengers, planes, and flights in a system such that it easy for
the user to then pull information from the program to create new
flights. Additionally, it can also search for existing flights
given a destination.</p>

### Who will use it?
<p>Airline managers who want to coordinate efficient operations at
the airport through creating flights, booking passengers onto and
off flights, and being able check for flights.</p>

### Why is this project of interest to you?
<p>The airport is such a vibrant and chaotic place. I am fascinate
by how airport systems are able to coordinate the sheer amount of
people, flights, and passengers coming from around the world. As a
business and computer science student, I wanted to create a project
that can manage the mass amount of information at a
surface level.</p>

**Task 3:**

- [x] As a user, I want to be able to add a new passenger to an aircraft
- [x] As a user, I want to be able to remove a passenger on the aircraft
- [x] As a user, I want to be able to add a passenger
- [x] As a user, I want to be able to add a new aircraft
- [x] As a user, I want to be able to add a new flight
- [x] As a user, I want to be able to view all the passengers
- [x] As a user, I want to be able to save the all the data that I have inputted (if I so choose)
- [x] As a user, I want to be able to reload airport inputs from the file
- [x] As a user, I want to be able to log the user actions (with time stamps)
- [ ] As a user, I want to be able to view all the flights on a map

## Phase 3: Instructions for Grader

### Adding
- You can generate the first required action related to adding Xs to a Y by
  clicking on the "Create new flight" button, filling in the required information,
  selecting one of the aircraft in the system using the dropdown select option,
  and clicking "OK"

### Removing
- You can generate the second required action related to adding Xs to a Y by
  clicking on a flight from the list of flights on the screen, selecting
  "Add Passenger" button, selecting a passenger from the dropdown, and clicking "OK".

### Visual Component
- You can locate my visual component by clicking on anyone of the cells. Pictures of an
  aircraft or passenger profile will pop up.

### Saving and Reloading
- You can save the state of my application by clicking on the "Save database to file"
  button or by clicking "Yes" when prompted upon exiting the application.
- You can reload the state of my application by clicking on "Yes" when prompted upon
  running the program

### Testing
- Run the test suite using Gradle:
  ```bash
  ./gradlew test
  ```
- Test reports will be generated in `build/reports/tests/test/`

## Phase 4: Event Logging and Design Reflection

### Task 2: Event Log Sample
```
Mon Apr 03 20:01:32 PDT 2023
Added private jet aircraft: Boeing898
Mon Apr 03 20:02:16 PDT 2023
Added flight in Sarah's bookings list
Mon Apr 03 20:02:16 PDT 2023
Added passenger (on flight): Sarah
Mon Apr 03 20:02:26 PDT 2023
Added flight in Elon's bookings list
Mon Apr 03 20:02:26 PDT 2023
Added passenger (on flight): Elon
Mon Apr 03 20:02:32 PDT 2023
Added flight in JJ's bookings list
Mon Apr 03 20:02:32 PDT 2023
Added passenger (on flight): JJ
Mon Apr 03 20:02:43 PDT 2023
Removed passenger: Elon
Mon Apr 03 20:02:48 PDT 2023
Removed passenger: JJ
```

### Phase 4: Task 3: Design Reflection

### UML Diagram

![UML Diagram](UML_Design_Diagram.png)

### Refactoring

The coupling is the most not the ideal as there tends to be may collections. Namely, lists of passengers
are used quite frequently. The cohesion is high however I believe that I could have made better uses of
abstract classes. I believe that the GUI can be separated into multiple classes as there is quite a bit
of code that can be grouped together. There is room for improvement and with more time, the relationship
between the classes can be improved. Namely, the bi-directional relationships in the code can be refactoring
such that saving and reloading using JSON will be less difficult. By doing so, the coupling in this application
would decrease.

### GUI sampler

![UML Diagram](GUI_Sampler_1.png)
![UML Diagram](GUI_Sampler_2.png)


### What's Next?
- [ ] Implementing an API that will allow users to map flights according to location
- [ ] Adding features that would enable visual components of the (ie. the aircrafts)
      to be able to move on the screen
