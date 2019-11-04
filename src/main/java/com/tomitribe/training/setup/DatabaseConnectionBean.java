package com.tomitribe.training.setup;

import org.apache.geronimo.transaction.manager.TransactionImpl;
import org.apache.openejb.core.ThreadContext;
import org.apache.openejb.loader.SystemInstance;
import org.apache.openejb.resource.jdbc.managed.local.Key;
import org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseConnectionBean implements DatabaseConnection {

    private final Logger logger = Logger.getLogger(DatabaseConnectionBean.class.getName());

    @Resource
    private DataSource dataSource;

    @Resource
    private SessionContext ctx;

    @Override
    public List<TransactionInfo> process(final List<TransactionType> callTypes) throws Exception {

        final boolean bmt = "BeanManaged".equals(ThreadContext.getThreadContext().getTransactionPolicy().getTransactionType().toString());
        try {
            if (bmt) {
                ctx.getUserTransaction().begin();
            }

            try {
                try (final Connection conn = dataSource.getConnection();
                     final PreparedStatement ps = conn.prepareStatement("VALUES 1");
                     final ResultSet rs = ps.executeQuery()) {

                    int rowCount = 0;

                    while (rs.next()) {
                        rowCount++;
                    }

                    logger.info("Received " + rowCount + " row(s) from the database");
                }
            } catch (SQLException e) {
                logger.severe("Error querying database: " + e.getMessage());
            }


            final TransactionManager transactionManager = SystemInstance.get().getComponent(TransactionManager.class);
            TransactionImpl currentTx = null;

            try {
                currentTx = (TransactionImpl) transactionManager.getTransaction();
            } catch (Exception e) {
                // ignore
            }

            Connection txConnection = null;

            if (currentTx != null) {
                try {
                    ManagedDataSource mds = (ManagedDataSource) dataSource;
                    final CommonDataSource delegate = mds.getDelegate();

                    final Key key = new Key(delegate, null, null);
                    final Connection proxyConnection = (Connection) currentTx.getResource(key);
                    txConnection = proxyConnection.unwrap(Connection.class);
                } catch (Exception e) {
                    // ignore
                }
            }

            final TransactionInfo transactionInfo = new TransactionInfo(
                    ThreadContext.getThreadContext().getBeanContext().getDeploymentID().toString(),
                    ThreadContext.getThreadContext().getTransactionPolicy().getTransactionType().toString(),
                    ThreadContext.getThreadContext().getTransactionPolicy().isTransactionActive(),
                    currentTx == null ? null : currentTx.getTransactionKey().toString(),
                    txConnection == null ? null : txConnection.toString()
            );

            // propagate call
            if (callTypes == null || callTypes.isEmpty()) {
                final ArrayList<TransactionInfo> result = new ArrayList<>();
                result.add(transactionInfo);
                return result;
            } else {
                final DatabaseConnection bean = Lookup.lookup(callTypes.get(0));
                final List<TransactionInfo> result = bean.process(tail(callTypes));
                result.add(0, transactionInfo);
                return result;
            }

        } finally {
            if (bmt) {
                ctx.getUserTransaction().commit();
            }
        }
    }

    private <T> List<T> tail(final List<T> input) {
        if (input == null || input.size() == 1) {
            return Collections.emptyList();
        }

        final ArrayList<T> result = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            result.add(input.get(i));
        }

        return result;
    }
}
