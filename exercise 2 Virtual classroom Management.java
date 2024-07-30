import java.util.*;

public class VirtualClassroomManager {
    private static final Map<String, Classroom> classrooms = new HashMap<>();
    private static final Map<String, Student> students = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Virtual Classroom Manager started. Type 'help' for a list of commands.");

        while (true) {
            String input = scanner.nextLine();
            String[] command = input.split(" ", 3);

            switch (command[0]) {
                case "add_classroom":
                    if (command.length < 2) {
                        System.out.println("Error: Class name is missing.");
                        break;
                    }
                    addClassroom(command[1]);
                    break;

                case "add_student":
                    if (command.length < 3) {
                        System.out.println("Error: Student ID or class name is missing.");
                        break;
                    }
                    addStudent(command[1], command[2]);
                    break;

                case "schedule_assignment":
                    if (command.length < 3) {
                        System.out.println("Error: Class name or assignment details are missing.");
                        break;
                    }
                    scheduleAssignment(command[1], command[2]);
                    break;

                case "submit_assignment":
                    if (command.length < 3) {
                        System.out.println("Error: Student ID, class name, or assignment details are missing.");
                        break;
                    }
                    submitAssignment(command[1], command[2], command[3]);
                    break;

                case "list_classrooms":
                    listClassrooms();
                    break;

                case "list_students":
                    listStudents();
                    break;

                case "exit":
                    System.out.println("Exiting Virtual Classroom Manager.");
                    return;

                case "help":
                    System.out.println("Commands:");
                    System.out.println("add_classroom [class_name]");
                    System.out.println("add_student [student_id] [class_name]");
                    System.out.println("schedule_assignment [class_name] [assignment_details]");
                    System.out.println("submit_assignment [student_id] [class_name] [assignment_details]");
                    System.out.println("list_classrooms");
                    System.out.println("list_students");
                    System.out.println("exit");
                    break;

                default:
                    System.out.println("Error: Unknown command.");
                    break;
            }
        }
    }

    private static void addClassroom(String className) {
        if (classrooms.containsKey(className)) {
            System.out.println("Error: Classroom already exists.");
        } else {
            classrooms.put(className, new Classroom(className));
            System.out.println("Classroom " + className + " has been created.");
        }
    }

    private static void addStudent(String studentId, String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Error: Classroom not found.");
            return;
        }

        if (students.containsKey(studentId)) {
            System.out.println("Error: Student ID already exists.");
            return;
        }

        Student student = new Student(studentId, classroom);
        students.put(studentId, student);
        classroom.addStudent(student);
        System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
    }

    private static void scheduleAssignment(String className, String assignmentDetails) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Error: Classroom not found.");
            return;
        }

        classroom.addAssignment(new Assignment(assignmentDetails));
        System.out.println("Assignment for " + className + " has been scheduled.");
    }

    private static void submitAssignment(String studentId, String className, String assignmentDetails) {
        Student student = students.get(studentId);
        if (student == null || !student.getClassroom().getName().equals(className)) {
            System.out.println("Error: Student not enrolled in the specified class.");
            return;
        }

        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            System.out.println("Error: Classroom not found.");
            return;
        }

        Assignment assignment = new Assignment(assignmentDetails);
        if (!classroom.getAssignments().contains(assignment)) {
            System.out.println("Error: Assignment not found for this class.");
            return;
        }

        student.submitAssignment(assignment);
        System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
    }

    private static void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
            return;
        }
        for (Classroom classroom : classrooms.values()) {
            System.out.println("Classroom: " + classroom.getName());
            classroom.listStudents();
        }
    }

    private static void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }
        for (Student student : students.values()) {
            System.out.println("Student ID: " + student.getId() + ", Classroom: " + student.getClassroom().getName());
        }
    }
}

class Classroom {
    private final String name;
    private final List<Student> students = new ArrayList<>();
    private final List<Assignment> assignments = new ArrayList<>();

    public Classroom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            for (Student student : students) {
                System.out.println("  Student ID: " + student.getId());
            }
        }
    }
}

class Student {
    private final String id;
    private final Classroom classroom;
    private final Set<Assignment> submittedAssignments = new HashSet<>();

    public Student(String id, Classroom classroom) {
        this.id = id;
        this.classroom = classroom;
    }

    public String getId() {
        return id;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void submitAssignment(Assignment assignment) {
        submittedAssignments.add(assignment);
    }
}

class Assignment {
    private final String details;

    public Assignment(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assignment that = (Assignment) obj;
        return details.equals(that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(details);
    }

    @Override
    public String toString() {
        return details;
    }
