package org.bhavani.constructions.dao.entities.models;

public enum TransactionMode {
    CASH,
    PHONE_PE {
        @Override
        public String toString() {
            return "PHONE_PE/UPI";
        }
    },
    NEFT,
    IMPS,
    RTGS,
    ONLINE
}

