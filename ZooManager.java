
// Import necessary Java libraries for file handling and data management
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Base class for all animals
class Animal {
    private String name;
    private int age;
    private String species;

    public Animal(String name, int age, String species) {
        this.name = name;
        this.age = age;
        this.species = species;
    }

    // Getter and setter methods for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return name + ", Age: " + age;
    }
}

// Subclasses for specific animal types
class Hyena extends Animal {
    public Hyena(String name, int age) {
        super(name, age, "Hyena");
    }
}

class Lion extends Animal {
    public Lion(String name, int age) {
        super(name, age, "Lion");
    }
}

class Tiger extends Animal {
    public Tiger(String name, int age) {
        super(name, age, "Tiger");
    }
}

class Bear extends Animal {
    public Bear(String name, int age) {
        super(name, age, "Bear");
    }
}

// Main class handling the zoo management
public class ZooManager {
    private static ArrayList<Animal> animals = new ArrayList<>();
    private static HashMap<String, Integer> speciesCount = new HashMap<>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ZooManager <input file> <output file>");
            return;
        }
        String inputFileName = args[0];
        String outputFileName = args[1];

        readAnimals(inputFileName);
        writeReport(outputFileName);
    }

    // Reads animal data from a file, creating Animal instances as appropriate
    private static void readAnimals(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Input file not found: " + filename);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length != 3)
                        throw new IllegalArgumentException("Incorrect data format");
                    String species = data[0].trim();
                    String name = data[1].trim();
                    int age = Integer.parseInt(data[2].trim());
                    if (age < 0)
                        throw new IllegalArgumentException("Age cannot be negative");
                    Animal animal = createAnimal(species, name, age);
                    if (animal != null) {
                        animals.add(animal);
                        speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                    }
                } catch (Exception e) {
                    System.out.println("Skipping invalid entry: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    // Method to create specific types of animals
    private static Animal createAnimal(String species, String name, int age) {
        switch (species) {
            case "Hyena":
                return new Hyena(name, age);
            case "Lion":
                return new Lion(name, age);
            case "Tiger":
                return new Tiger(name, age);
            case "Bear":
                return new Bear(name, age);
            default:
                System.out.println("Unknown species: " + species);
                return null;
        }
    }

    // Writes a report to a file summarizing the animal data
    private static void writeReport(String filename) {
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                String species = entry.getKey();
                int count = entry.getValue();
                writer.println(species + ": " + count + " animals");
                animals.stream()
                        .filter(animal -> animal.getSpecies().equals(species))
                        .forEach(animal -> writer.println("\t" + animal.toString()));
            }
        } catch (IOException e) {
            System.out.println("Error writing the report: " + e.getMessage());
        }
    }
}
