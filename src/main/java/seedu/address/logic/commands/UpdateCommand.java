package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSROOM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLASSROOMS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classroom.Classroom;
import seedu.address.model.classroom.ClassroomName;
import seedu.address.model.student.Student;

/**
 * Updates the classroom in the address book.
 */
public class UpdateCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the details of the classroom identified "
            + "by the classroom name used in the displayed classroom list. "
            + "Existing classroom name will be edited with the input name and existing assignments will be updated with"
            + " the input assignments.\n"
            + "Parameters: CLASSROOM_NAME (must be a valid classroom name) "
            + "[" + PREFIX_CLASSROOM + "CLASSROOM_NAME] "
            + "[" + PREFIX_ASSIGNMENT + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 4E3 "
            + PREFIX_CLASSROOM + "4E4 "
            + PREFIX_ASSIGNMENT + "Assignment 2";


    public static final String MESSAGE_UPDATE_CLASSROOM_SUCCESS = "Updated Classroom: %1$s";
    public static final String MESSAGE_NOT_UPDATED = "At least one field to update must be provided.";
    public static final String MESSAGE_DUPLICATE_CLASSROOM = "This classroom already exists in the Teacher's Notebook.";

    private final ClassroomName classroomName;
    private final UpdateClassroomDescriptor updateClassroomDescriptor;

    /**
     * @param classroomName name of the classroom in the filtered classroom list to edit
     * @param updateClassroomDescriptor details to update the classroom with
     */
    public UpdateCommand(ClassroomName classroomName, UpdateClassroomDescriptor updateClassroomDescriptor) {
        requireNonNull(classroomName);
        requireNonNull(updateClassroomDescriptor);

        this.classroomName = classroomName;
        this.updateClassroomDescriptor = new UpdateClassroomDescriptor(updateClassroomDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Classroom> lastShownList = model.getFilteredClassroomList();
        Index index = Index.fromZeroBased(0);
        boolean classroomExists = false;
        for (int i = 0; i < lastShownList.size(); i++) {
            Classroom classroom = lastShownList.get(i);
            if (classroom.getClassroomName().equals(this.classroomName)) {
                index = Index.fromZeroBased(i);
                classroomExists = true;
                break;
            }
        }
        if (!classroomExists) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLASSROOM);
        }

        Classroom classroomToUpdate = lastShownList.get(index.getZeroBased());
        Classroom updatedClassroom = createUpdatedClassroom(classroomToUpdate, updateClassroomDescriptor);

        if (!classroomToUpdate.isSameClassroom(updatedClassroom) && model.hasClassroom(updatedClassroom)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLASSROOM);
        }

        model.setClassroom(classroomToUpdate, updatedClassroom);
        model.updateFilteredClassroomList(PREDICATE_SHOW_ALL_CLASSROOMS);
        return new CommandResult(String.format(MESSAGE_UPDATE_CLASSROOM_SUCCESS, updatedClassroom));
    }

    /**
     * Creates and returns a {@code Classroom} with the details of {@code classroomToUpdate}
     * edited with {@code updateClassroomDescriptor}.
     */
    private static Classroom createUpdatedClassroom(Classroom classroomToUpdate, UpdateClassroomDescriptor
            updateClassroomDescriptor) {
        assert classroomToUpdate != null;

        ClassroomName updatedClassroomName = updateClassroomDescriptor.getClassroomName()
                .orElse(classroomToUpdate.getClassroomName());
        Set<Student> updatedStudent = classroomToUpdate.getStudents();
        Set<Assignment> updatedAssignment = updateClassroomDescriptor.getAssignments()
                .orElse(classroomToUpdate.getAssignments());
        return new Classroom(updatedClassroomName, updatedStudent, updatedAssignment);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdateCommand)) {
            return false;
        }

        // state check
        UpdateCommand e = (UpdateCommand) other;
        return classroomName.equals(e.classroomName)
                && updateClassroomDescriptor.equals(e.updateClassroomDescriptor);
    }

    /**
     * Stores the details to update the classroom with. Each non-empty field value will replace the
     * corresponding field value of the classroom.
     */
    public static class UpdateClassroomDescriptor {
        private ClassroomName classroomName;
        private Set<Student> students;
        private Set<Assignment> assignments;

        public UpdateClassroomDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code assignments} is used internally.
         */
        public UpdateClassroomDescriptor(UpdateClassroomDescriptor toCopy) {
            setClassroomName(toCopy.classroomName);
            setStudents(toCopy.students);
            setAssignments(toCopy.assignments);
        }

        /**
         * Returns true if at least one field is updated.
         */
        public boolean isAnyFieldUpdated() {
            return CollectionUtil.isAnyNonNull(classroomName, assignments);
        }

        public void setClassroomName(ClassroomName classroomName) {
            this.classroomName = classroomName;
        }

        public Optional<ClassroomName> getClassroomName() {
            return Optional.ofNullable(classroomName);
        }

        /**
         * Sets {@code students} to this object's {@code students}.
         * A defensive copy of {@code students} is used internally.
         */
        public void setStudents(Set<Student> students) {
            this.students = (students != null) ? new HashSet<>(students) : null;
        }

        /**
         * Returns an unmodifiable student set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code students} is null.
         */
        public Optional<Set<Student>> getStudents() {
            return (students != null) ? Optional.of(Collections.unmodifiableSet(students)) : Optional.empty();
        }

        /**
         * Sets {@code assignments} to this object's {@code assignments}.
         * A defensive copy of {@code assignments} is used internally.
         */
        public void setAssignments(Set<Assignment> assignments) {
            this.assignments = (assignments != null) ? new HashSet<>(assignments) : null;
        }

        /**
         * Returns an unmodifiable assignment set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code assignments} is null.
         */
        public Optional<Set<Assignment>> getAssignments() {
            return (assignments != null) ? Optional.of(Collections.unmodifiableSet(assignments)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof UpdateClassroomDescriptor)) {
                return false;
            }

            // state check
            UpdateClassroomDescriptor e = (UpdateClassroomDescriptor) other;

            return getClassroomName().equals(e.getClassroomName())
                    && getStudents().equals(e.getStudents())
                    && getAssignments().equals(e.getAssignments());
        }
    }
}
