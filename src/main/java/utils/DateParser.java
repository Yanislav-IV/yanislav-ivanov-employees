package utils;

import emp.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DateParser {
    private List<String> patterns;
    private final DateTimeFormatter dtf;
    private final DateTimeFormatter dtfReversed;

    public DateParser(String patternsFileName) throws IOException {
        this.patterns = readPatterns(patternsFileName);
        this.dtf = DateTimeFormatter.ofPattern(getPatternString(this.patterns, false));
        this.dtfReversed = DateTimeFormatter.ofPattern(getPatternString(this.patterns, true));
    }

    private String getPatternString(List<String> patterns, boolean reversed) {
        if (reversed) {
            Collections.reverse(patterns);
        }
        String patternString = String.join("", patterns);
        return patternString;
    }

    private List<String> readPatterns(String patternsFileName) throws IOException {
        final BufferedReader rd = new BufferedReader(new InputStreamReader(
                DateParser.class.getResourceAsStream(patternsFileName)));

        List<String> patterns = new ArrayList<>();
        String line;

        while ((line = rd.readLine()) != null) {
            patterns.add("[" + line + "]");
        }

        return patterns;
    }


    public LocalDate parseDate(String dateString, boolean reversed) {
        if (dateString.equals("NULL")) {
            return LocalDate.now();
        }

        try {
            if (reversed) {
                return LocalDate.parse(dateString, this.dtfReversed);
            }
            return LocalDate.parse(dateString, this.dtf);
        } catch (Exception e1) {

            throw new IllegalArgumentException(dateString + " - date format not supported. Please add the pattern to datePatterns.txt");
        }
    }
}
