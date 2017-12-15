package com.example.keiichi.project_mobile.DAL.POJOs;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Event{

    private List<Event> instances;
    private Attendee[] attendees;
    private ItemBody body;
    private String bodyPreview;
    private List<String> categories;
    private String changeKey;
    private String createdDateTime;
    private DateTimeTimeZone end;
    private boolean hasAttachments;
    private String iCalUId;
    private String id;
    private String importance;
    private boolean isAllDay;
    private boolean isCancelled;
    private boolean isOrganizer;
    private boolean isReminderOn;
    private String lastModifiedDateTime;
    private Location location;
    private String onlineMeetingUrl;
    private Recipient organizer;
    private String originalEndTimeZone;
    private String originalStart;
    private String originalStartTimeZone;
    private PatternedRecurrence recurrence;
    private int reminderMinutesBeforeStart;
    private boolean responseRequested;
    private ResponseStatus responseStatus;
    private String sensitivity;
    private String seriesMasterId;
    private String showAs;
    private DateTimeTimeZone start;
    private String subject;
    private String type;
    private String webLink;
    private List<Attachment> attachments;
    private Calendar calendar;
    private Extension extension;

    public List<Event> getInstances() {
        return instances;
    }

    public void setInstances(List<Event> instances) {
        this.instances = instances;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public String getiCalUId() {
        return iCalUId;
    }

    public void setiCalUId(String iCalUId) {
        this.iCalUId = iCalUId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public boolean isReminderOn() {
        return isReminderOn;
    }

    public void setReminderOn(boolean reminderOn) {
        isReminderOn = reminderOn;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getOnlineMeetingUrl() {
        return onlineMeetingUrl;
    }

    public void setOnlineMeetingUrl(String onlineMeetingUrl) {
        this.onlineMeetingUrl = onlineMeetingUrl;
    }

    public String getOriginalEndTimeZone() {
        return originalEndTimeZone;
    }

    public void setOriginalEndTimeZone(String originalEndTimeZone) {
        this.originalEndTimeZone = originalEndTimeZone;
    }

    public String getOriginalStartTimeZone() {
        return originalStartTimeZone;
    }

    public void setOriginalStartTimeZone(String originalStartTimeZone) {
        this.originalStartTimeZone = originalStartTimeZone;
    }

    public int getReminderMinutesBeforeStart() {
        return reminderMinutesBeforeStart;
    }

    public void setReminderMinutesBeforeStart(int reminderMinutesBeforeStart) {
        this.reminderMinutesBeforeStart = reminderMinutesBeforeStart;
    }

    public boolean isResponseRequested() {
        return responseRequested;
    }

    public void setResponseRequested(boolean responseRequested) {
        this.responseRequested = responseRequested;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getSeriesMasterId() {
        return seriesMasterId;
    }

    public void setSeriesMasterId(String seriesMasterId) {
        this.seriesMasterId = seriesMasterId;
    }

    public String getShowAs() {
        return showAs;
    }

    public void setShowAs(String showAs) {
        this.showAs = showAs;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Attendee[] getAttendees() {
        return attendees;
    }

    public void setAttendees(Attendee[] attendees) {
        this.attendees = attendees;
    }

    public ItemBody getBody() {
        return body;
    }

    public void setBody(ItemBody body) {
        this.body = body;
    }

    public DateTimeTimeZone getEnd() {
        return end;
    }

    public void setEnd(DateTimeTimeZone end) {
        this.end = end;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Recipient getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Recipient organizer) {
        this.organizer = organizer;
    }

    public String getOriginalStart() {
        return originalStart;
    }

    public void setOriginalStart(String originalStart) {
        this.originalStart = originalStart;
    }

    public PatternedRecurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(PatternedRecurrence recurrence) {
        this.recurrence = recurrence;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public DateTimeTimeZone getStart() {
        return start;
    }

    public void setStart(DateTimeTimeZone start) {
        this.start = start;
    }
}
