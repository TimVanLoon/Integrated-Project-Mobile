package com.example.keiichi.project_mobile.POJO;


public class PatternedRecurrence {
    private RecurrencePattern pattern;
    private RecurrenceRange range;

    public PatternedRecurrence(RecurrencePattern pattern, RecurrenceRange range) {
        this.pattern = pattern;
        this.range = range;
    }

    public RecurrencePattern getPattern() {
        return pattern;
    }

    public void setPattern(RecurrencePattern pattern) {
        this.pattern = pattern;
    }

    public RecurrenceRange getRange() {
        return range;
    }

    public void setRange(RecurrenceRange range) {
        this.range = range;
    }
}
