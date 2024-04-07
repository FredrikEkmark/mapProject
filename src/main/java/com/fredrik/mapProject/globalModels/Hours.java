package com.fredrik.mapProject.globalModels;

public enum Hours {
    H12AM("12am", "00:00", 0),
    H1AM("1am", "01:00", 1),
    H2AM("2am", "02:00", 2),
    H3AM("3am", "03:00", 3),
    H4AM("4am", "04:00", 4),
    H5AM("5am", "05:00", 5),
    H6AM("6am", "06:00", 6),
    H7AM("7am", "07:00", 7),
    H8AM("8am", "08:00", 8),
    H9AM("9am", "09:00", 9),
    H10AM("10am", "10:00", 10),
    H11AM("11am", "11:00", 11),
    H12PM("12pm", "12:00", 12),
    H1PM("1pm", "13:00", 13),
    H2PM("2pm", "14:00", 14),
    H3PM("3pm", "15:00", 15),
    H4PM("4pm", "16:00", 16),
    H5PM("5pm", "17:00", 17),
    H6PM("6pm", "18:00", 18),
    H7PM("7pm", "19:00", 19),
    H8PM("8pm", "20:00", 20),
    H9PM("9pm", "21:00", 21),
    H10PM("10pm", "22:00", 22),
    H11PM("11pm", "23:00", 23);

    private final String value;
    private final String formattedValue;
    private final int turnChangeIndex;

    Hours(String value, String formattedValue, int turnChangeIndex) {
        this.value = value;
        this.formattedValue = formattedValue;
        this.turnChangeIndex = turnChangeIndex;
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public int getTurnChangeIndex(int timeZoneOffset) {
        int value = turnChangeIndex - timeZoneOffset;

        if (value > 23)
            value -= 24;
        else if (value < 0)
            value += 24;

        return value;
    }
}