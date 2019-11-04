package com.tomitribe.training.setup;

import javax.naming.InitialContext;

public class Lookup {

    public static DatabaseConnection lookup(final TransactionType txType) {
        if (txType == null) {
            return null;
        }

        try {
            final String name = "java:global/tx-test/DatabaseConnectionBean_" + txType.getLookupName() + "!com.tomitribe.training.setup.DatabaseConnection";
            return (DatabaseConnection) new InitialContext().lookup(name);
        } catch (Exception e) {
            return null;
        }
    }

}
