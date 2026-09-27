import java.io.Serializable;

/**
 * Represents a single biosecurity checklist item for a Farm.
 */
public class ChecklistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemId;
    private String category;    // Sanitation, Vaccination, Quarantine, Feed Management, Visitor Control
    private String description;
    private boolean completed;

    public ChecklistItem(String itemId, String category, String description, boolean completed) {
        this.itemId = itemId;
        this.category = category;
        this.description = description;
        this.completed = completed;
    }

    public String getItemId() {
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-16s | %-40s | %-10s",
                itemId, category, description, (completed ? "Done" : "Pending"));
    }
}
