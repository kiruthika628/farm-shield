import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered farm, holding its animals and biosecurity checklist.
 */
public class Farm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String farmId;
    private String farmName;
    private String ownerName;
    private String location;
    private String farmType;   // Livestock, Poultry, Mixed

    private List<Animal> animals;
    private List<ChecklistItem> checklist;

    public Farm(String farmId, String farmName, String ownerName, String location, String farmType) {
        this.farmId = farmId;
        this.farmName = farmName;
        this.ownerName = ownerName;
        this.location = location;
        this.farmType = farmType;
        this.animals = new ArrayList<>();
        this.checklist = new ArrayList<>();
    }

    public String getFarmId() {
        return farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getLocation() {
        return location;
    }

    public String getFarmType() {
        return farmType;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<ChecklistItem> getChecklist() {
        return checklist;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public boolean removeAnimal(String animalId) {
        return animals.removeIf(a -> a.getAnimalId().equalsIgnoreCase(animalId));
    }

    public Animal findAnimal(String animalId) {
        for (Animal a : animals) {
            if (a.getAnimalId().equalsIgnoreCase(animalId)) {
                return a;
            }
        }
        return null;
    }

    public void addChecklistItem(ChecklistItem item) {
        checklist.add(item);
    }

    public boolean removeChecklistItem(String itemId) {
        return checklist.removeIf(c -> c.getItemId().equalsIgnoreCase(itemId));
    }

    public ChecklistItem findChecklistItem(String itemId) {
        for (ChecklistItem c : checklist) {
            if (c.getItemId().equalsIgnoreCase(itemId)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %-15s | %-15s | %-10s | Animals: %-3d | Checklist: %-3d",
                farmId, farmName, ownerName, location, farmType, animals.size(), checklist.size());
    }
}
