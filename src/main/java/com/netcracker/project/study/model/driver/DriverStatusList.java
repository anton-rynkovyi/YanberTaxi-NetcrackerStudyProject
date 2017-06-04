package com.netcracker.project.study.model.driver;

import java.math.BigInteger;

public interface DriverStatusList {

    BigInteger APPROVAL = BigInteger.valueOf(1);

    BigInteger OFF_DUTY = BigInteger.valueOf(2);

    BigInteger FREE = BigInteger.valueOf(3);

    BigInteger ON_CALL = BigInteger.valueOf(4);

    BigInteger PERFORMING_ORDER = BigInteger.valueOf(5);

}
