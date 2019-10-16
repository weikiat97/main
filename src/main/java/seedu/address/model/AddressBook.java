package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.classroom.Classroom;
import seedu.address.model.classroom.UniqueClassroomList;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameClassroom comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueStudentList students;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        students = new UniqueStudentList();
    }

    private final UniqueClassroomList classrooms;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        classrooms = new UniqueClassroomList();
    }


    public AddressBook() {}

    /**
     * Creates an AddressBook using the Classrooms in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Replaces the contents of the classroom list with {@code classrooms}.
     * {@code classrooms} must not contain duplicate classrooms.
     */
    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms.setClassrooms(classrooms);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
        setClassrooms(newData.getClassroomList());
    }

    //// classroom-level operations

    /**
     * Returns true if a classroom with the same identity as {@code classroom} exists in the address book.
     */
    public boolean hasClassroom(Classroom classroom) {
        requireNonNull(classroom);
        return classrooms.contains(classroom);
    }

    /**
     * Adds a classroom to the address book.
     * The classroom must not already exist in the address book.
     */
    public void addClassroom(Classroom p) {
        classrooms.add(p);
    }

    /**
     * Replaces the given classroom {@code target} in the list with {@code editedClassroom}.
     * {@code target} must exist in the address book.
     * The classroom identity of {@code editedClassroom} must not be the same as another existing classroom in the
     * address book.
     */
    public void setClassroom(Classroom target, Classroom editedClassroom) {
        requireNonNull(editedClassroom);

        classrooms.setClassroom(target, editedClassroom);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeClassroom(Classroom key) {
        classrooms.remove(key);
    }

    //// student-level operations

    /**
     * Returns true if a classroom with the same identity as {@code classroom} exists in the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     */
    public void addStudent(Student p) {
        students.add(p);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the address
     * book.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return classrooms.asUnmodifiableObservableList().size() + " classrooms";
        // TODO: refine later
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Classroom> getClassroomList() {
        return classrooms.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && students.equals(((AddressBook) other).students)
                && classrooms.equals(((AddressBook) other).classrooms));
    }

    @Override
    public int hashCode() {
        return classrooms.hashCode();
    }
}
