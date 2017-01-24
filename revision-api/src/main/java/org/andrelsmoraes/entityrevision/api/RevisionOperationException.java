package org.andrelsmoraes.entityrevision.api;

public class RevisionOperationException extends Exception {

    public RevisionOperationException() {
    }

    public RevisionOperationException(String detailMessage) {
        super(detailMessage);
    }
}
