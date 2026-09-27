import java.io.Serializable;

/**
 * Represents a single animal record (livestock or poultry) belonging to a Farm.
 */
public class Animal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String animalId;
    private String type;        // Cow, Goat, Sheep, Pig, Chicken
    private int age;             // in months
    private boolean vaccinated;
    private String healthStatus; // Healthy, Sick, Under Observation

    public Animal(String animalId, String type, int age, boolean vaccinated, String healthStatus) {
        this.animalId = animalId;
        this.type = type;
        this.age = age;
        this.vaccinated = vaccinated;
        this.healthStatus = healthStatus;
    }

    public String getAnimalId() {
        return animalId;
    }

    public String getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-10s | %-5d | %-12s | %-18s",
                animalId, type, age, (vaccinated ? "Vaccinated" : "Not Vaccinated"), healthStatus);
    }
}
