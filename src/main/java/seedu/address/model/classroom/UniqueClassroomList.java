package seedu.address.model.classroom;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.classroom.exceptions.DuplicateClassroomException;
import seedu.address.model.classroom.exceptions.ClassroomNotFoundException;

/**
 * A list of classrooms that enforces uniqueness between its elements and does not allow nulls.
 * A classroom is considered unique by comparing using {@code Classroomt#isSameClassroom(Classroom)}. As such, adding
 * and updating of classrooms uses Classroom#isSameClassroom(Classroom) for equality so as to ensure that the classroom
 * being added or updated is unique in terms of identity in the UniqueClassroomList. However, the removal of a classroom
 * uses Classroom#equals(Object) so as to ensure that the classroom with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Classroom#isSameClassroom(Classroom)
 */
public class UniqueClassroomList implements Iterable<Classroom> {

    private final ObservableList<Classroom> internalList = FXCollections.observableArrayList();
    private final ObservableList<Classroom> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent classroom as the given argument.
     */
    public boolean contains(Classroom toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameClassroom);
    }

    /**
     * Adds a classroom to the list.
     * The classroom must not already exist in the list.
     */
    public void add(Classroom toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateClassroomException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the classroom {@code target} in the list with {@code editedClassroom}.
     * {@code target} must exist in the list.
     * The classroom identity of {@code editedClassroom} must not be the same as another existing classroom in the list.
     */
    public void setClassroom(Classroom target, Classroom editedClassroom) {
        requireAllNonNull(target, editedClassroom);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ClassroomNotFoundException();
        }

        if (!target.isSameStudent(editedStudent) && contains(editedStudent)) {
            throw new DuplicateStudentException();
        }

        internalList.set(index, editedStudent);
    }

    /**
     * Removes the equivalent student from the list.
     * The student must exist in the list.
     */
    public void remove(Student toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new StudentNotFoundException();
        }
    }

    public void setStudents(UniqueStudentList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        requireAllNonNull(students);
        if (!studentsAreUnique(students)) {
            throw new DuplicateStudentException();
        }

        internalList.setAll(students);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Student> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Student> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueStudentList // instanceof handles nulls
                && internalList.equals(((UniqueStudentList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code students} contains only unique students.
     */
    private boolean studentsAreUnique(List<Student> students) {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).isSameStudent(students.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
