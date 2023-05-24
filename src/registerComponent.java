import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class registerComponent {
    public static String database = "/app/data/database.txt"; // Specify the file name or path
    private static class Student {
        private String name;
        private String id;
        private String[] courses;

        public Student(String name, String id, String[] courses) {
            this.name = name;
            this.id = id;
            this.courses = courses;
        }
        public  void insertInDataBase()
        {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(database , true))) {
                writer.write(this.name+";");
                writer.write(this.id+";");
                boolean f=true;
                for(String course:this.courses){
                    if(f)
                    {
                        writer.write(course);
                        f=false;
                    }
                    else {
                        writer.write("," + course);
                    }
                }
                writer.newLine(); // Add a new line after the new data
                System.out.println("Student Inserted to the database successfully.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }

        }
        public void display() {
            System.out.println("Name: " + name);
            System.out.println("ID: " + id);
            System.out.print("Courses: ");
            for (String course : courses) {
                System.out.print(course + " ");
            }
            System.out.println();
        }
     }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                registerStudent(scanner);
            } else if (choice.equals("2")) {
                addBatchData(scanner);
           }
            else {
                break;
            }
          }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("1-Add student data");
        System.out.println("2-Add batch students data");
     }

    private static void registerStudent(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter courses (comma-separated): ");
        String coursesInput = scanner.nextLine();
        String[] courses = coursesInput.split(",");
        Student student = new Student(name, id, courses);
        student.insertInDataBase();
     }

    private static void addBatchData(Scanner scanner) {
        File batchDataFolder = new File("/app/data/batch/");
        File[] eligibleFiles = batchDataFolder.listFiles((dir, name) -> name.contains("verified"));
        if (eligibleFiles == null || eligibleFiles.length == 0) {
            System.out.println("No eligible batch files found.");
            return;
        }
        System.out.println("Eligible batch files:");
        for (int i = 0; i < eligibleFiles.length; i++) {
            System.out.println(eligibleFiles[i].getName());
        }
        String fileName = scanner.nextLine();
        File selectedFile =  new File("/app/data/batch/" + fileName);
        List<Student> students = readStudentsFromFile(selectedFile);
        insertStudentsInfo(students);
      }
    private static List<Student> readStudentsFromFile(File file) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println("Reading now: " + line );
                line=line.substring(1, line.length() - 1);
                String[] data = line.split(";");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    String id = data[1].trim();
                    String[] courses = data[2].trim().split(",");
                    Student student = new Student(name, id, courses);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    private static void insertStudentsInfo(List<Student> students) {
         for (Student student : students) {
             student.insertInDataBase();
           }
    }
}
