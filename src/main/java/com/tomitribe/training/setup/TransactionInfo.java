package com.tomitribe.training.setup;

import java.io.Serializable;

public class TransactionInfo implements Serializable {

    private final String deploymentId;
    private final String txType;
    private final boolean txActive;
    private final String txId;
    private final String txConnectionInfo;

    public TransactionInfo(final String deploymentId, final String txType, final boolean txActive, final String txId, final String txConnectionInfo) {
        this.deploymentId = deploymentId;
        this.txType = txType;
        this.txActive = txActive;
        this.txId = txId;
        this.txConnectionInfo = txConnectionInfo;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public String getTxType() {
        return txType;
    }

    public String getTxId() {
        return txId;
    }

    public String getTxConnectionInfo() {
        return txConnectionInfo;
    }

    @Override
    public String toString() {
        return "TransactionInfo{" +
                "deploymentId='" + deploymentId + '\'' +
                ", txType='" + txType + '\'' +
                ", txActive=" + txActive +
                ", txId='" + txId + '\'' +
                ", txConnectionInfo='" + txConnectionInfo + '\'' +
                '}';
    }
}
