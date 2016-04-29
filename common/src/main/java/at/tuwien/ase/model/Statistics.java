package at.tuwien.ase.model;

import org.codehaus.enunciate.json.JsonRootType;

import java.util.Date;
import java.util.List;

public interface Statistics {
    @JsonRootType
    class UserTaskType {
        int id;
        String name;
        String company;

        List<TaskType> taskTypes;

        public UserTaskType(int id, String name, String company, List<TaskType> taskTypes) {
            this.id = id;
            this.name = name;
            this.company = company;
            this.taskTypes = taskTypes;
        }

        public List<TaskType> getTaskTypes() {
            return taskTypes;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getCompany() {
            return company;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            User user = (User) o;

            return id == user.id;

        }

        @Override public int hashCode() {
            return id;
        }
    }

    @JsonRootType
    class TaskType {
        int id;
        String name;
        int totalMinutes;

        public TaskType(int id, String name, int totalMinutes) {
            this.id = id;
            this.name = name;
            this.totalMinutes = totalMinutes;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getTotalMinutes() {
            return totalMinutes;
        }
    }

    @JsonRootType
    class ResourceSummary {
        String name;
        int totalMinutes;
        int totalQuantity;

        public ResourceSummary(String name, int totalMinutes, int totalQuantity) {
            this.name = name;
            this.totalMinutes = totalMinutes;
            this.totalQuantity = totalQuantity;
        }

        public String getName() {
            return name;
        }

        public int getTotalMinutes() {
            return totalMinutes;
        }

        public int getTotalQuantity() {
            return totalQuantity;
        }

        public boolean isMachine() {
            return totalQuantity == 0;
        }
    }

    @JsonRootType
    class Item<T> {
        T value;
        String name;

        public Item(T value, String name) {
            this.value = value;
            this.name = name;
        }

        public T getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    @JsonRootType
    class DateItem<T> {
        T value;
        Date date;

        public DateItem(T value, Date date) {
            this.value = value;
            this.date = date;
        }

        public T getValue() {
            return value;
        }


        public Date getDate() {
            return date;
        }
    }
}
