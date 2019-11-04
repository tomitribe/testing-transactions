package com.tomitribe.training.setup;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ApparatusTest {
    @Deployment
    public static Archive<?> createTestArchive() {

        return ShrinkWrap.create(JavaArchive.class, "tx-test.jar")
                .addPackage("com.tomitribe.training.setup")
                .addAsManifestResource("META-INF/ejb-jar.xml", "ejb-jar.xml");
    }

    @Test
    public void shouldSayHiFromContainer() throws Exception {
        final DatabaseConnection db = Lookup.lookup(TransactionType.REQUIRES_NEW);
        final List<TransactionInfo> transactionInfos = db.process(Arrays.asList(
                TransactionType.REQUIRED,
                TransactionType.REQUIRES_NEW,
                TransactionType.BEAN_MANAGED,
                TransactionType.SUPPORTS,
                TransactionType.MANDATORY,
                TransactionType.NOT_SUPPORTED));

        for (final TransactionInfo transactionInfo : transactionInfos) {
            System.out.println(transactionInfo.toString());
        }
    }


}
