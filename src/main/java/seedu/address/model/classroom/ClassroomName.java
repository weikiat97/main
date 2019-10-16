package seedu.address.model.classroom;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Classroom's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClassroomName(String)}
 */
public class ClassroomName {

    public static final String MESSAGE_CONSTRAINTS =
            "Classroom names should only contain alphanumeric characters, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}]*";

    public final String value;

    /**
     * Constructs a {@code ClassroomName}.
     *
     * @param classroomName A valid classroom name.
     */
    public ClassroomName(String classroomName) {
        requireNonNull(classroomName);
        checkArgument(isValidClassroomName(classroomName), MESSAGE_CONSTRAINTS);
        value = classroomName;
    }

    /**
     * Returns true if a given string is a valid classroom name.
     */
    public static boolean isValidClassroomName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ClassroomName // instanceof handles nulls
                && value.equals(((ClassroomName) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
