package com.netcracker.project.study.model.order;

import java.math.BigInteger;

public interface OrderStatusList {

    BigInteger NEW = BigInteger.valueOf(6);

    BigInteger ACCEPTED = BigInteger.valueOf(7);

    BigInteger PERFORMED = BigInteger.valueOf(8);

    BigInteger PERFORMING = BigInteger.valueOf(9);

    BigInteger CANCELED = BigInteger.valueOf(10);
}
