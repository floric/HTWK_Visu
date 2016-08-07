package org.htwkvisu.domain;

/**
 * NamedQueryConstants - the holder of all named-query-constants for the score_value table
 * This class exists, because variable names are not allowed for the named-query definition, so
 * this can not be replaced with an enum.
 */
public class NamedQueryConstants {

    public static final String FIND_TERMINAL = "findTerminal";
    public static final String FIND_HELIPAD = "findHelipad";
    public static final String FIND_AERODROME = "findAerodrome";
    public static final String FIND_BUS = "findBus";
    public static final String FIND_TRAIN = "findTrain";
    public static final String FIND_TRAM = "findTram";
    public static final String FIND_HOSPITAL = "findHospital";
    public static final String FIND_PHARMACY = "findPharmacy";
    public static final String FIND_DENTIST = "findDentist";
    public static final String FIND_VETERINARY = "findVeterinary";
    public static final String FIND_DOCTORS = "findDoctors";
    public static final String FIND_BLOOD_DONATION = "findBloodDonation";
    public static final String FIND_SCHOOL = "findSchool";
    public static final String FIND_COLLEGE = "findCollege";
    public static final String FIND_LIBRARY = "findLibrary";
    public static final String FIND_MUSEUM = "findMuseum";
    public static final String FIND_RESEARCH_INSTITUTION = "findResearchInstitution";
    public static final String FIND_THEATRE = "findTheatre";

    static final String FROM_WHERE = "select s from score_value s where";
}
