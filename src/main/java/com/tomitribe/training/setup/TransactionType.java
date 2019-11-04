package com.tomitribe.training.setup;

public enum TransactionType {

    MANDATORY("Mandatory"),
    REQUIRED("Required"),
    REQUIRES_NEW("RequiresNew"),
    SUPPORTS("Supports"),
    NOT_SUPPORTED("NotSupported"),
    NEVER("Never"),
    BEAN_MANAGED("BeanManaged");

    private final String lookupName;

    TransactionType(final String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupName() {
        return lookupName;
    }
}
