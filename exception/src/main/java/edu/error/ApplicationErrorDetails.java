package edu.error;

public enum ApplicationErrorDetails {
    LOGIN_ALREADY_EXISTS("${LOGIN} is already in use by another client.", "login"),
    SESSION_ID_NOT_FOUND("User session [${SESSION_ID}] has not been found.", "session_id"),
    ORDER_ID_NOT_FOUND("Order ID [${ORDER_ID}] has not been found.", "order_id"),
    SESSION_ID_NOT_PRESENT("User session id is not present in request.", "session_id"),
    LOGIN_OR_PASSWORD_INCORRECT("The entered username or password is incorrect.", "login_password"),
    NOT_ALLOWED_REQUEST("You are not allowed to request this action.", "request"),
    STATION_NAME_ALREADY_EXISTS("Station name [${STATION_NAME}] already exists.", "name"),
    STATION_ID_NOT_FOUND("Station id [${STATION_ID}] has not been found.", "station_id"),
    TRIP_ID_NOT_FOUND("Trip id [${TRIP_ID}] has not been found.", "trip_id"),
    FROM_STATION_NAME_NOT_FOUND("Departure station name [${FROM_STATION_NAME}] has not been found.", "fromStation"),
    TO_STATION_NAME_NOT_FOUND("Arrival station name [${TO_STATION_NAME}] has not been found.", "toStation"),
    BUS_NAME_NOT_FOUND("Bus name [${BUS_NAME}] has not been found.", "busName"),
    PASSENGER_NOT_FOUND("Passenger has not been found.", "passenger"),
    TRIP_DATE_NOT_FOUND("Trip date [${TRIP_DATE}] has not been found.", "date"),
    INNER_DATA_BASE_ERROR("Inner DataBase error occurred. Please, try later.", "database"),
    UNKNOWN_ERROR("Inner application error occurred. Please, try later.", "unknown_error"),
    CAN_NOT_DELETE_LAST_ADMIN("You cannot delete last administrator.", "administrator"),
    ERROR_ADDING_NEW_ADMINISTRATOR_TO_DB("ERROR_ADDING_NEW_ADMINISTRATOR_TO_DB!", "administrator"),

    VIOLATION_PASSWORD_MAX_LENGTH_CONSTRAINT("Your password is too long!", "password"),
    VIOLATION_PASSWORD_MIN_LENGTH_CONSTRAINT("Your password is too short!", "password"),
    VIOLATION_PASSWORD_EMPTY_CONSTRAINT("Don't forget enter your password", "password"),

    VIOLATION_LOGIN_MAX_LENGTH_CONSTRAINT("Your login is too long!!", "login"),
    VIOLATION_LOGIN_EMPTY_CONSTRAINT("Don't forget enter your login!", "login"),
    VIOLATION_LOGIN_PATTERN_CONSTRAINT("Your login consists of invalid characters!", "login"),

    VIOLATION_FIRST_NAME_EMPTY_CONSTRAINT("Don't forget enter your first name!", "firstname"),
    VIOLATION_FIRST_NAME_MAX_LENGTH_CONSTRAINT("Your first name is too long!", "firstname"),
    VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT("Your first name consists of invalid characters!", "firstname"),

    VIOLATION_LAST_NAME_EMPTY_CONSTRAINT("Don't forget enter your last name!", "lastname"),
    VIOLATION_LAST_NAME_MAX_LENGTH_CONSTRAINT("Your last name is too long!", "lastname"),
    VIOLATION_LAST_NAME_PATTERN_CONSTRAINT("Your last name consists of invalid characters!", "lastname"),

    VIOLATION_PATRONYMIC_MAX_LENGTH_CONSTRAINT("Your patronymic is too long!", "patronymic"),
    VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT("Your patronymic consists of invalid characters!!", "patronymic"),

    VIOLATION_POSITION_EMPTY_CONSTRAINT("Don't forget enter position!", "position"),
    VIOLATION_POSITION_MAX_LENGTH_CONSTRAINT("Your position is too long!", "position"),

    VIOLATION_PASSPORT_EMPTY_CONSTRAINT("Don't forget enter passport!", "passport"),

    VIOLATION_EMAIL_EMPTY_CONSTRAINT("Don't forget enter email!", "email"),
    VIOLATION_EMAIL_PATTERN_CONSTRAINT("Enter correct email!", "email"),
    VIOLATION_PHONE_EMPTY_CONSTRAINT("Don't forget enter your mobile phone number!", "phone"),
    VIOLATION_PHONE_PATTERN_CONSTRAINT("Enter correct mobile phone number!", "phone"),

    NO_SESSION_ID_IN_COOKIES_IN_REQUEST("Provide a valid cookie in request.", "cookie"),

    BUS_BRAND_NAME_ALREADY_EXISTS("There is such a bus brand name [${BUS_BRAND_NAME}] in DB.", "busName"),
    VIOLATION_BUS_NAME_EMPTY_CONSTRAINT("Bus name brand is empty", "busName"),
    VIOLATION_BUS_PLACE_COUNT_CONSTRAINT("Bus place count must have positive value.", "placeCount"),

    VIOLATION_START_TIME_EMPTY_OR_PATTERN_CONSTRAINT("Please, provide start time in correct way.", "start"),

    VIOLATION_DURATION_TIME_EMPTY_OR_PATTERN_CONSTRAINT("Please, provide duration time in correct way.", "duration"),

    VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT("Date is empty or given in a wrong way.", "fields with date"),

    VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT("Period is empty or given in a wrong way.", "period"),

    VIOLATION_STATION_EMPTY_CONSTRAINT("Please, provide a station name.", "station"),

    VIOLATION_PRICE_EMPTY_OR_PATTERN_CONSTRAINT("Please, provide a price in a correct way.", "price"),

    VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT("There is no schedule and dates or they present in the same time, provide only one of them.", "schedule and dates"),

    NO_SCHEDULE_INFO_IN_REQUEST("There is no schedule in request to add a new trip.", "schedule or dates"),
    WRONG_SCHEDULE_INFO_IN_REQUEST("There is an error in schedule request to add a new trip.", "schedule or dates"),

    DATES_EMPTY_OR_WRONG_PATTERN_IN_REQUEST("There is an error in schedule request(dates part) to add a new trip.", "schedule or dates"),
    TO_DATE_BEFORE_FROM_DATE_IN_SCHEDULE("Change trip dates so that toDate be after fromDate in the schedule", "toDate and fromDate"),
    NOT_ENOUGH_FREE_PASSENGER_SEATS("There are not enough free passenger seats for you.", "passengers"),

    SEAT_NUMBER_TAKEN_ALREADY("Chosen seat number is already taken by another person.", "place"),
    TRYING_TO_BOOK_SAME_SEAT("You are trying to book the same place you have already.","place"),
    INCORRECT_USER_TYPE("You are not our client to make an order. Please, login from an correct account.", "sessionId"),

    TRIP_IS_NOT_APPROVED("Trip with ID [${TRIP_ID} is not approved to make orders.]", "tripId")
    ;

    private final String message;
    private final String field;

    ApplicationErrorDetails(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}
