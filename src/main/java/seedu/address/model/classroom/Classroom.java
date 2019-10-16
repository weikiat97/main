package seedu.address.model.classroom;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.assignment.Assignment;
import seedu.address.model.student.Student;

/**
 * Represents a Classroom in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Classroom {

    // Identity fields
    private final ClassroomName classroomName;


    // Data fields
    private final Set<Assignment> assignments = new HashSet<>();
    private final Set<Student> students = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Classroom(ClassroomName classroomName, Set<Student> students, Set<Assignment> assignments) {
        requireAllNonNull(classroomName, students, assignments);
        this.classroomName = classroomName;
        this.students.addAll(students);
        this.assignments.addAll(assignments);
    }

    public ClassroomName getClassroomName() {
        return classroomName;
    }

    /**
     * Returns an immutable student set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Student> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    /**
     * Returns an immutable assignment set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Assignment> getAssignments() {
        return Collections.unmodifiableSet(assignments);
    }

    /**
     * Returns true if both classrooms of the same classroom name have the same students.
     * This defines a weaker notion of equality between two classrooms.
     */
    public boolean isSameClassroom(Classroom otherClassroom) {
        if (otherClassroom == this) {
            return true;
        }

        return otherClassroom != null
                && otherClassroom.getClassroomName().equals(getClassroomName())
                && otherClassroom.getStudents().equals(getStudents());
    }

    /**
     * Returns true if both classrooms have the same identity and data fields.
     * This defines a stronger notion of equality between two classrooms.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Classroom)) {
            return false;
        }

        Classroom otherClassroom = (Classroom) other;
        return otherClassroom.getClassroomName().equals(getClassroomName())
                && otherClassroom.getStudents().equals(getStudents())
                && otherClassroom.getAssignments().equals(getAssignments());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(classroomName, students, assignments);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClassroomName())
                .append(" Students: ");
        getStudents().forEach(builder::append);
        builder.append(" Assignments: ");
        getAssignments().forEach(builder::append);
        return builder.toString();
    }

}
