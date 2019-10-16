package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.NewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classroom.Classroom;
import seedu.address.model.classroom.ClassroomName;
import seedu.address.model.student.Student;

/**
 * Parses input arguments and creates a new NewCommand object
 */
public class NewCommandParser implements Parser<NewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the NewCommand
     * and returns an NewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public NewCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASSROOM);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSROOM) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NewCommand.MESSAGE_USAGE));
        }

        ClassroomName classroomName = ParserUtil.parseClassroomName(argMultimap.getValue(PREFIX_CLASSROOM).get());

        Classroom classroom = new Classroom(classroomName, Collections.<Student>emptySet(),
                Collections.<Assignment>emptySet());

        return new NewCommand(classroom);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
