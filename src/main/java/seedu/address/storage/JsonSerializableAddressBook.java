package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.classroom.Classroom;
import seedu.address.model.student.Student;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_CLASSROOM = "Classroom list contains duplicate classroom(s).";

    private final List<JsonAdaptedClassroom> classrooms = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given classrooms.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("classrooms") List<JsonAdaptedClassroom> classrooms) {
        this.classrooms.addAll(classrooms);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        classrooms.addAll(source.getClassroomList().stream().map(JsonAdaptedClassroom::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedClassroom jsonAdaptedClassroom : classrooms) {
            Classroom classroom = jsonAdaptedClassroom.toModelType();
            if (addressBook.hasClassroom(classroom)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLASSROOM);
            }
            addressBook.addClassroom(classroom);
        }
        return addressBook;
    }

}
