package com.tomitribe.training.setup;

import java.util.List;

public interface DatabaseConnection {


    List<TransactionInfo> process(List<TransactionType> callTypes) throws Exception;
}
