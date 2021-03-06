package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CONTENT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CONTENT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CONTENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.note.EditNoteCommand;
import seedu.address.logic.commands.note.EditNoteCommand.EditNoteDescriptor;
import seedu.address.logic.parser.note.EditNoteCommandParser;
import seedu.address.model.note.Content;
import seedu.address.model.note.Title;
import seedu.address.testutil.EditNoteDescriptorBuilder;

class EditNoteCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);

    private EditNoteCommandParser parser = new EditNoteCommandParser();

    @Test
    void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TITLE_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditNoteCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 j/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC, Title.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_CONTENT_DESC, Content.MESSAGE_CONSTRAINTS); // invalid address

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC + VALID_CONTENT_AMY, Title.MESSAGE_CONSTRAINTS);
    }

    @Test
    void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = targetIndex.getOneBased() + CONTENT_DESC_AMY + TITLE_DESC_AMY;

        EditNoteCommand.EditNoteDescriptor descriptor = new EditNoteDescriptorBuilder().withTitle(VALID_TITLE_AMY)
                .withContent(VALID_CONTENT_AMY).build();
        EditNoteCommand expectedCommand = new EditNoteCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + CONTENT_DESC_AMY;

        EditNoteCommand.EditNoteDescriptor descriptor = new EditNoteDescriptorBuilder()
                .withContent(VALID_CONTENT_AMY).build();
        EditNoteCommand expectedCommand = new EditNoteCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_AMY;
        EditNoteDescriptor descriptor = new EditNoteDescriptorBuilder().withTitle(VALID_TITLE_AMY).build();
        EditNoteCommand expectedCommand = new EditNoteCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + CONTENT_DESC_AMY;
        descriptor = new EditNoteDescriptorBuilder().withContent(VALID_CONTENT_AMY).build();
        expectedCommand = new EditNoteCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + CONTENT_DESC_AMY + CONTENT_DESC_AMY + CONTENT_DESC_BOB;

        EditNoteCommand.EditNoteDescriptor descriptor = new EditNoteDescriptorBuilder()
                .withContent(VALID_CONTENT_BOB).build();
        EditNoteCommand expectedCommand = new EditNoteCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + INVALID_CONTENT_DESC + CONTENT_DESC_BOB;
        EditNoteCommand.EditNoteDescriptor descriptor = new EditNoteDescriptorBuilder()
                .withContent(VALID_CONTENT_BOB).build();
        EditNoteCommand expectedCommand = new EditNoteCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_TITLE_DESC + CONTENT_DESC_BOB + TITLE_DESC_BOB;
        descriptor = new EditNoteDescriptorBuilder().withTitle(VALID_TITLE_BOB)
                .withContent(VALID_CONTENT_BOB).build();
        expectedCommand = new EditNoteCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
