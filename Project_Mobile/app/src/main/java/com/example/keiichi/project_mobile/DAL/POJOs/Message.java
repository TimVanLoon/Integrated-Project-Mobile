package com.example.keiichi.project_mobile.DAL.POJOs;


public class Message {

    private Recipient[] bccRecipients;
    private ItemBody body;
    private String bodyPreview;
    private String[] categories;
    private Recipient[] ccRecipients;
    private String changeKey;
    private String conversationId;
    private String createdDateTime;
    private Recipient from;
    private boolean hasAttachments;
    private String id;
    private String importance;
    private String inferenceClassification;
    private String internetMessageId;
    private boolean isDeliveryReceiptRequested;
    private boolean isDraft;
    private boolean isRead;
    private boolean isReadReceiptRequested;
    private String lastModifiedDateTime;
    private String parentFolderId;
    private String receivedDateTime;
    private Recipient[] replyTo;
    private Recipient sender;
    private String sentDateTime;
    private String subject;
    private Recipient[] toRecipients;
    private ItemBody uniqueBody;
    private String webLink;
    private Attachment[] attachments;
    private Extension[] extensions;
    private MultiValueLegacyExtendedProperty[] multiValueExtendedProperties;
    private SingleValueLegacyExtendedProperty[] singleValueLegacyExtendedProperty;

    public Message(Recipient[] bccRecipients, ItemBody body, String bodyPreview, String[] categories, Recipient[] ccRecipients, String changeKey, String conversationId, String createdDateTime, Recipient from, boolean hasAttachments, String id, String importance, String inferenceClassification, String internetMessageId, boolean isDeliveryReceiptRequested, boolean isDraft, boolean isRead, boolean isReadReceiptRequested, String lastModifiedDateTime, String parentFolderId, String receivedDateTime, Recipient[] replyTo, Recipient sender, String sentDateTime, String subject, Recipient[] toRecipients, ItemBody uniqueBody, String webLink, Attachment[] attachments, Extension[] extensions, MultiValueLegacyExtendedProperty[] multiValueExtendedProperties, SingleValueLegacyExtendedProperty[] singleValueLegacyExtendedProperty) {
        this.bccRecipients = bccRecipients;
        this.body = body;
        this.bodyPreview = bodyPreview;
        this.categories = categories;
        this.ccRecipients = ccRecipients;
        this.changeKey = changeKey;
        this.conversationId = conversationId;
        this.createdDateTime = createdDateTime;
        this.from = from;
        this.hasAttachments = hasAttachments;
        this.id = id;
        this.importance = importance;
        this.inferenceClassification = inferenceClassification;
        this.internetMessageId = internetMessageId;
        this.isDeliveryReceiptRequested = isDeliveryReceiptRequested;
        this.isDraft = isDraft;
        this.isRead = isRead;
        this.isReadReceiptRequested = isReadReceiptRequested;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.parentFolderId = parentFolderId;
        this.receivedDateTime = receivedDateTime;
        this.replyTo = replyTo;
        this.sender = sender;
        this.sentDateTime = sentDateTime;
        this.subject = subject;
        this.toRecipients = toRecipients;
        this.uniqueBody = uniqueBody;
        this.webLink = webLink;
        this.attachments = attachments;
        this.extensions = extensions;
        this.multiValueExtendedProperties = multiValueExtendedProperties;
        this.singleValueLegacyExtendedProperty = singleValueLegacyExtendedProperty;
    }

    public Recipient[] getBccRecipients() {
        return bccRecipients;
    }

    public void setBccRecipients(Recipient[] bccRecipients) {
        this.bccRecipients = bccRecipients;
    }

    public ItemBody getBody() {
        return body;
    }

    public void setBody(ItemBody body) {
        this.body = body;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public Recipient[] getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(Recipient[] ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Recipient getFrom() {
        return from;
    }

    public void setFrom(Recipient from) {
        this.from = from;
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
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

    public String getInferenceClassification() {
        return inferenceClassification;
    }

    public void setInferenceClassification(String inferenceClassification) {
        this.inferenceClassification = inferenceClassification;
    }

    public String getInternetMessageId() {
        return internetMessageId;
    }

    public void setInternetMessageId(String internetMessageId) {
        this.internetMessageId = internetMessageId;
    }

    public boolean isDeliveryReceiptRequested() {
        return isDeliveryReceiptRequested;
    }

    public void setDeliveryReceiptRequested(boolean deliveryReceiptRequested) {
        isDeliveryReceiptRequested = deliveryReceiptRequested;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isReadReceiptRequested() {
        return isReadReceiptRequested;
    }

    public void setReadReceiptRequested(boolean readReceiptRequested) {
        isReadReceiptRequested = readReceiptRequested;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(String receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public Recipient[] getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Recipient[] replyTo) {
        this.replyTo = replyTo;
    }

    public Recipient getSender() {
        return sender;
    }

    public void setSender(Recipient sender) {
        this.sender = sender;
    }

    public String getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(String sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Recipient[] getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(Recipient[] toRecipients) {
        this.toRecipients = toRecipients;
    }

    public ItemBody getUniqueBody() {
        return uniqueBody;
    }

    public void setUniqueBody(ItemBody uniqueBody) {
        this.uniqueBody = uniqueBody;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment[] attachments) {
        this.attachments = attachments;
    }

    public Extension[] getExtensions() {
        return extensions;
    }

    public void setExtensions(Extension[] extensions) {
        this.extensions = extensions;
    }

    public MultiValueLegacyExtendedProperty[] getMultiValueExtendedProperties() {
        return multiValueExtendedProperties;
    }

    public void setMultiValueExtendedProperties(MultiValueLegacyExtendedProperty[] multiValueExtendedProperties) {
        this.multiValueExtendedProperties = multiValueExtendedProperties;
    }

    public SingleValueLegacyExtendedProperty[] getSingleValueLegacyExtendedProperty() {
        return singleValueLegacyExtendedProperty;
    }

    public void setSingleValueLegacyExtendedProperty(SingleValueLegacyExtendedProperty[] singleValueLegacyExtendedProperty) {
        this.singleValueLegacyExtendedProperty = singleValueLegacyExtendedProperty;
    }
}
