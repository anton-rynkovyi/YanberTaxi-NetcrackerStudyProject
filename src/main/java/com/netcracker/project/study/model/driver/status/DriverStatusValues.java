package com.netcracker.project.study.model.driver.status;

import java.math.BigInteger;

public interface DriverStatusValues {

    BigInteger APPROVAL = BigInteger.valueOf(1);

    BigInteger OFF_DUTY = BigInteger.valueOf(2);

    BigInteger FREE = BigInteger.valueOf(3);

    BigInteger ON_CALL = BigInteger.valueOf(4);

    BigInteger PERFORMING_ORDER = BigInteger.valueOf(5);


    String APPROVAL_STR = "approval";

    String OFF_DUTY_STR = "off duty";

    String FREE_STR = "free";

    String ON_CALL_STR = "on call";

    String PERFORMING_ORDER_STR = "performing order";
}
