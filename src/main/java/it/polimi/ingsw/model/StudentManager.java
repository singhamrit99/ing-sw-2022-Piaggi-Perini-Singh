package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;

import java.util.EnumMap;
import java.util.Map;

public final class StudentManager {
    public static EnumMap<Colors, Integer> addStudent(EnumMap<Colors, Integer> students, EnumMap<Colors, Integer> studentsToAdd) {
        for (Map.Entry<Colors, Integer> set : studentsToAdd.entrySet()) {
            if (set.getValue() >= 0) {
                if (students.containsKey(set.getKey())) {
                    students.put(set.getKey(), set.getValue() + students.get(set.getKey()));
                } else {
                    students.put(set.getKey(), set.getValue());
                }
            } else {
                return null;
            }
        }
        return students;
    }

    public static EnumMap<Colors, Integer> removeStudent(EnumMap<Colors, Integer> students, EnumMap<Colors, Integer> studentsToRemove) {
        for (Map.Entry<Colors, Integer> set : studentsToRemove.entrySet()) {
            if (set.getValue() >= 0) {
                if (students.containsKey(set.getKey())) {
                    students.put(set.getKey(), students.get(set.getKey()) - set.getValue());
                }
            } else {
                return null;
            }
        }
        return students;
    }

    public static EnumMap<Colors, Integer> createEmptyStudentsEnum() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }

        return students;
    }
}