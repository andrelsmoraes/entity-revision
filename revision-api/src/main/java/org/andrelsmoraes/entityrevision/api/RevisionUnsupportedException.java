package org.andrelsmoraes.entityrevision.api;

public class RevisionUnsupportedException extends Exception {

    public RevisionUnsupportedException() {
    }

    public RevisionUnsupportedException(String detailMessage) {
        super(detailMessage);
    }
}
