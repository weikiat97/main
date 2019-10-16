package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSROOM;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.UpdateCommand.UpdateClassroomDescriptor;
import seedu.address.logic.commands.UpdateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classroom.ClassroomName;

/**
 * Parses input arguments and creates a new UpdateCommand object
 */
public class UpdateCommandParser implements Parser<UpdateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UpdateCommand
     * and returns an UpdateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UpdateCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASSROOM, PREFIX_ASSIGNMENT);

        ClassroomName classroomName = ParserUtil.parseClassroomName(argMultimap.getPreamble());

        UpdateClassroomDescriptor updateClassroomDescriptor = new UpdateClassroomDescriptor();

        if (argMultimap.getValue(PREFIX_CLASSROOM).isPresent()) {
            updateClassroomDescriptor.setClassroomName(ParserUtil.parseClassroomName(argMultimap
                    .getValue(PREFIX_CLASSROOM).get()));
        }
        parseAssignmentsForUpdate(argMultimap.getAllValues(PREFIX_ASSIGNMENT))
                .ifPresent(updateClassroomDescriptor::setAssignments);

        if (!updateClassroomDescriptor.isAnyFieldUpdated()) {
            throw new ParseException(UpdateCommand.MESSAGE_NOT_UPDATED);
        }

        return new UpdateCommand(classroomName, updateClassroomDescriptor);
    }

    /**
     * Parses {@code Collection<String> assignments} into a {@code Set<Assignment>} if {@code assignments} is non-empty.
     * If {@code assignments} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Assignment>} containing zero assignments.
     */
    private Optional<Set<Assignment>> parseAssignmentsForUpdate(Collection<String> assignments) throws ParseException {
        assert assignments != null;

        if (assignments.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> assignmentSet = assignments.size() == 1 && assignments.contains("") ? Collections.emptySet()
                : assignments;
        return Optional.of(ParserUtil.parseAssignments(assignmentSet));
    }

}
