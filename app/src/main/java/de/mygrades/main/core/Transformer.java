package de.mygrades.main.core;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import de.mygrades.database.dao.GradeEntry;
import de.mygrades.database.dao.Rule;
import de.mygrades.database.dao.TransformerMapping;
import de.mygrades.util.exceptions.ParseException;

/**
 * Creates GradeEntry Objects from given HTML with given TransformerMappings.
 */
public class Transformer {
    private static final String TAG = Transformer.class.getSimpleName();

    // mapping from TransformerMapping Name -> GradeEntry Property
    private static final String ITERATOR = "iterator";
    private static final String EXAM_ID = "exam_id";
    private static final String NAME = "name";
    private static final String SEMESTER = "semester";
    private static final String GRADE = "grade";
    private static final String STATE = "state";
    private static final String CREDIT_POINTS = "credit_points";
    private static final String ANNOTATION = "annotation";
    private static final String ATTEMPT = "attempt";

    private static final String SEMESTER_FORMAT_SEMESTER = "semester";
    private static final String SEMSETER_FORMAT_DATE = "date";

    private static final String SEMSETER_WS = "Wintersemester ";
    private static final String SEMSETER_SS = "Sommersemester ";


    /**
     * Parser to extract values into Models.
     */
    private Parser parser;

    /**
     * HTML from which the data gets extracted.
     */
    private String html;

    /**
     * Map of String -> TransformerMapping for easy access.
     */
    private Map<String, TransformerMapping> transformerMapping;

    /**
     * Rule getting transformed
     */
    private Rule rule;

    /**
     * Compile Pattern to extract the Semester and Year of field extracted from html.
     */
    private Pattern semesterPattern;


    public Transformer(Rule rule, String html, Parser parser) {
        this.rule = rule;
        this.transformerMapping = createTransformerMappingMap(rule.getTransformerMappings());
        this.parser = parser;
        this.html = html;

        this.semesterPattern = Pattern.compile(rule.getSemesterPattern());
    }

    /**
     * Creates GradeEntry objects for all matching elements from html via
     * xPath expression 'iterator' from TransformerMapping.
     *
     * @return List of extracted GradeEntries
     * @throws ParseException if something goes wrong at parsing
     */
    public List<GradeEntry> transform() throws ParseException {
        List<GradeEntry> gradeEntries = new ArrayList<>();
        // remember strings of semester to calculate semester number
        Set<String> semestersSet = new HashSet<>();

        // get List to iterate through and respectively extract GradeEntry values
        NodeList nodeList = parser.parseToNodeList(transformerMapping.get(ITERATOR).getParseExpression(), html);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            // get Node as XML document -> so it must not created every time
            Document xmlDocument = parser.getNodeAsDocument(nNode);

            // create new GradeEntry and add all extracted values
            GradeEntry gradeEntry = new GradeEntry();
            gradeEntry.setExamId(getStringProperty(xmlDocument, EXAM_ID));
            gradeEntry.setName(getStringProperty(xmlDocument, NAME));
            gradeEntry.setSemester(calculateGradeEntrySemester(getStringProperty(xmlDocument, SEMESTER)));
            gradeEntry.setGrade(getDoubleProperty(xmlDocument, GRADE, rule.getGradeFactor()));
            gradeEntry.setState(getStringProperty(xmlDocument, STATE));
            gradeEntry.setCreditPoints(getDoubleProperty(xmlDocument, CREDIT_POINTS, null));
            gradeEntry.setAnnotation(getStringProperty(xmlDocument, ANNOTATION));
            gradeEntry.setAttempt(getStringProperty(xmlDocument, ATTEMPT));

            // update hash, used as primary key
            gradeEntry.updateHash();

            // add GradeEntry to list
            gradeEntries.add(gradeEntry);
            // add semester to set
            semestersSet.add(gradeEntry.getSemester());
        }

        // calculate semester numbers
        calculateGradeEntrySemesterNumber(gradeEntries, semestersSet);

        return gradeEntries;
    }

    /**
     * Calculates the GradeEntry semester property to "Wintersemester" or "Sommersemester".
     * Different types are possible which is determined through the rule.
     *
     * @param origSemester Original String extracted out of html
     * @return formatted semester string
     */
    private String calculateGradeEntrySemester(String origSemester) {
        String resultSemester = "";

        if (rule.getSemesterFormat().equals(SEMESTER_FORMAT_SEMESTER)) {
            String extractedSemester = "";
            Integer extractedYear = 0;

            // match pattern to origSemester and get Year and Semester String
            Matcher matcher = semesterPattern.matcher(origSemester);
            if (matcher.find()) { // Find first match
                extractedSemester = matcher.group(1);

                try {
                    extractedYear = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException e) {
                    extractedYear = 0; // TODO: what to do with errors?
                }
            }

            // get year in correct format
            if (extractedYear.toString().length() < 4) {
                extractedYear = extractedYear + 2000;
            }

            // if extractedSemester starts with w -> Wintersemester
            if (extractedSemester.toLowerCase().startsWith("w")) {
                resultSemester += SEMSETER_WS + extractedYear + "/" + (extractedYear+1);
            } else {
                resultSemester += SEMSETER_SS + extractedYear;
            }
        } else if (rule.getSemesterFormat().equals(SEMSETER_FORMAT_DATE)) {
            semesterPattern = Pattern.compile("\\d{2}\\.(\\d{2})\\.(\\d{4})");
            Integer extractedYear = 0;
            Integer extractedMonth = 0;

            // match pattern to origSemester and get Year and Month
            Matcher matcher = semesterPattern.matcher(origSemester);
            if (matcher.find()) { // Find first match
                try {
                    extractedMonth = Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException e) {
                    extractedMonth = 0; // TODO: what to do with errors?
                }
                try {
                    extractedYear = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException e) {
                    extractedYear = 0; // TODO: what to do with errors?
                }
            }
            System.out.println("Month: " + extractedMonth + " - Year: " + extractedYear);
            // calculate Semester string depending on month and year
            if (extractedMonth >= 4 && extractedMonth <= 9) { // Sommersemester (o4-09)
                resultSemester += SEMSETER_SS + extractedYear;
            } else { // Wintersemester
                if (extractedMonth >= 10) { // first part of Wintersemester (10-12)
                    resultSemester += SEMSETER_WS + extractedYear + "/" + (extractedYear+1);
                } else { // second part of Wintersemester (01-03)
                    resultSemester += SEMSETER_WS + (extractedYear-1) + "/" + extractedYear;
                }
            }
        }

        return resultSemester;
    }

    /**
     * Updates each GradeEntry in gradeEntries with SemesterNumber.
     * This is calculated by the available Semester Strings given in semestersSet.
     *
     * @param gradeEntries GradeEntries which should get a SemesterNumber
     * @param semestersSet available
     */
    private void calculateGradeEntrySemesterNumber(List<GradeEntry> gradeEntries, Set<String> semestersSet) {
        // create List out of set to sort it
        List<String> semestersList = new ArrayList<>(semestersSet);

        // compile regex pattern for reuse
        final Pattern pattern = Pattern.compile("(^\\w+)\\s*([0-9]+)");

        // sort semester to get the correct semester number
        Collections.sort(semestersList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                Matcher matcher;
                String sem1 = "";
                String sem2 = "";
                Integer year1 = 0;
                Integer year2 = 0;

                // get Semester and Year from first String
                matcher = pattern.matcher(s1);
                if (matcher.find()) { // Find first match
                    sem1 = matcher.group(1);
                    try {
                        year1 = Integer.parseInt(matcher.group(2));
                    } catch (NumberFormatException e) {
                        year1 = 0; // TODO: what to do with errors?
                    }
                }

                // get Semester and Year from second String
                matcher = pattern.matcher(s2);
                if (matcher.find()) { // Find first match
                    sem2 = matcher.group(1);
                    try {
                        year2 = Integer.parseInt(matcher.group(2));
                    } catch (NumberFormatException e) {
                        year2 = 0; // TODO: what to do with errors?
                    }
                }

                // compare years -> if equal sem1 and sem2 (SoSe and WiSe) have to be compared
                int compYears = year1.compareTo(year2);
                if (compYears == 0) {
                    return sem1.compareTo(sem2);
                }
                return compYears;
            }
        });

        // create Map Semester -> SemesterNumber for easy adding to GradeEntry
        Map<String, Integer> semesterSemesterNumberMap = new HashMap<>();
        for (int i=0; i < semestersList.size(); i++) {
            semesterSemesterNumberMap.put(semestersList.get(i), i+1);
        }

        // finally set SemesterNumber in each GradeEntry
        for (GradeEntry gradeEntry : gradeEntries) {
            gradeEntry.setSemesterNumber(semesterSemesterNumberMap.get(gradeEntry.getSemester()));
        }
    }

    /**
     * Gets the value from Document determined by type of TransformerMapping as String.
     *
     * @param xmlDocument Document which should get parsed
     * @param type Type of TransformerMapping regarding to GradeEntry
     * @return extracted value as String
     * @throws ParseException if something goes wrong at parsing
     */
    private String getStringProperty(Document xmlDocument, String type) throws ParseException {
        String result = parser.parseToString(transformerMapping.get(type).getParseExpression(), xmlDocument).trim();

        return result;
    }

    /**
     * Gets the value from Document determined by type of TransformerMapping as Double.
     *
     * @param xmlDocument Document which should get parsed
     * @param type Type of TransformerMapping regarding to GradeEntry
     * @return extracted value as Double
     * @throws ParseException if something goes wrong at parsing
     */
    private Double getDoubleProperty(Document xmlDocument, String type, Double factor) throws ParseException {
        Double property;
        String result = parser.parseToString(transformerMapping.get(type).getParseExpression(), xmlDocument).trim();
        result = result.replace(',', '.');

        // if cannot parse to Double -> return null
        try {
            property = Double.parseDouble(result);
        } catch (NumberFormatException e) {
            return null;
        }

        // if factor is given -> multiply
        if (factor != null) {
            property = property * factor;
        }

        return property;
    }

    /**
     * Creates HashMap for TransformerMappings for easy access.
     *
     * @param transformerMappings which are put into Map
     * @return Map of TransformerMappings
     */
    private Map<String, TransformerMapping> createTransformerMappingMap(List<TransformerMapping> transformerMappings) {
        Map<String, TransformerMapping> map = new HashMap<>();
        for (TransformerMapping transformerMapping : transformerMappings) {
            map.put(transformerMapping.getName(), transformerMapping);
        }
        return map;
    }
}