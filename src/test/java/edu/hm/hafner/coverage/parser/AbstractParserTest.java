package edu.hm.hafner.coverage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.coverage.CoverageParser;
import edu.hm.hafner.coverage.ModuleNode;
import edu.hm.hafner.util.FilteredLog;
import edu.hm.hafner.util.SecureXmlParserFactory.ParsingException;

import static org.assertj.core.api.Assertions.*;

/**
 * Baseclass for parser tests.
 *
 * @author Ullrich Hafner
 */
abstract class AbstractParserTest {
    private final FilteredLog log = new FilteredLog("Errors");

    ModuleNode readReport(final String fileName) {
        var parser = createParser();

        return readReport(fileName, parser);
    }

    ModuleNode readReport(final String fileName, final CoverageParser parser) {
        try (InputStream stream = AbstractParserTest.class.getResourceAsStream(fileName);
                Reader reader = new InputStreamReader(Objects.requireNonNull(stream), StandardCharsets.UTF_8)) {
            return parser.parse(reader, log);
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    abstract CoverageParser createParser();

    protected FilteredLog getLog() {
        return log;
    }

    @Test
    void shouldFailWhenParsingInvalidFiles() {
        assertThatExceptionOfType(ParsingException.class).isThrownBy(() -> readReport("/design.puml"));
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> readReport("empty.xml"));
    }
}
