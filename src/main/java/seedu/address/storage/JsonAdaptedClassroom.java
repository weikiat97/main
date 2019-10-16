package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classroom.Classroom;
import seedu.address.model.classroom.ClassroomName;
import seedu.address.model.student.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonAdaptedClassroom {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Classroom's %s field is missing!";

    private final String classroomName;
    private final List<JsonAdaptedAssignment> assignment = new ArrayList<>();
    private final List<JsonAdaptedStudent> student = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedClassroom(@JsonProperty("classroomName") String classroomName,
                                @JsonProperty("student") List<JsonAdaptedStudent> student,
                                @JsonProperty("assignment") List<JsonAdaptedAssignment> assignment) {
        this.classroomName = classroomName;
        if (student != null) {
            this.student.addAll(student);
        }
        if (assignment != null) {
            this.assignment.addAll(assignment);
        }
    }

    /**
     * Converts a given {@code Classroom} into this class for Jackson use.
     */
    public JsonAdaptedClassroom(Classroom source) {
        classroomName = source.getClassroomName().value;
        student.addAll(source.getStudents().stream()
                .map(JsonAdaptedStudent::new)
                .collect(Collectors.toList()));
        assignment.addAll(source.getAssignments().stream()
                .map(JsonAdaptedAssignment::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted student object into the model's {@code Classroom} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted classroom.
     */
    public Classroom toModelType() throws IllegalValueException {
        final List<Assignment> classroomAssignments = new ArrayList<>();
        final List<Student> classroomStudents = new ArrayList<>();
        for (JsonAdaptedAssignment assignment : assignment) {
            classroomAssignments.add(assignment.toModelType());
        }
        for (JsonAdaptedStudent student : student) {
            classroomStudents.add(student.toModelType());
        }

        if (classroomName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ClassroomName.class.getSimpleName()));
        }
        if (!ClassroomName.isValidClassroomName(classroomName)) {
            throw new IllegalValueException(ClassroomName.MESSAGE_CONSTRAINTS);
        }
        final ClassroomName modelClassroomName = new ClassroomName(classroomName);
        final Set<Student> modelStudents = new HashSet<>(classroomStudents);
        final Set<Assignment> modelAssignments = new HashSet<>(classroomAssignments);
        return new Classroom(modelClassroomName, modelStudents, modelAssignments);
    }
}
