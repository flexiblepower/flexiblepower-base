package org.flexiblepower.context;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

public interface FlexiblePowerContext {

    long currentTimeMillis();

    Date currentTime();

    ScheduledExecutorService getScheduler();

}
