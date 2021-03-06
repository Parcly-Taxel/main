package seedu.address.logic.commands.note;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.note.Note;

/**
 * Adds a lecture note to NUStudy.
 */
public class AddNoteCommand extends Command {
    public static final String COMMAND_WORD = "addnote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a lecture note to NUStudy.\n"
            + "Parameters: "
            + PREFIX_TITLE + "TITLE "
            + PREFIX_CONTENT + "CONTENT ["
            + PREFIX_IMAGE + "]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "Lecture 1 "
            + PREFIX_CONTENT + "Write once debug everywhere\n"
            + "Using /i will open up a file dialog to select the image";

    public static final String MESSAGE_SUCCESS = "Lecture note added: %1$s";
    public static final String MESSAGE_DUPLICATE_TITLE = "This title already exists";

    private final Note toAdd;

    /**
     * Creates an AddNoteCommand to add the specified {@code Note}.
     */
    public AddNoteCommand(Note note) {
        requireNonNull(note);
        toAdd = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasNote(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TITLE);
        }

        toAdd.finalizeImage(model.getAppDataFilePath().getParent());
        model.addNote(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddNoteCommand // instanceof handles nulls
                && toAdd.equals(((AddNoteCommand) other).toAdd));
    }
}
