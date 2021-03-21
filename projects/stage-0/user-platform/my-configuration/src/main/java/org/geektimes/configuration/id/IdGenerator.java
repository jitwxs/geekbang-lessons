package org.geektimes.configuration.id;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdGenerator {
    private static final long SERVER_ID_LEFT_SHIFT = 12L;

    private static long businessId;
    private static long serverId;
    private static SnowFlakeIdGenerator snowFlakeIdGenerator;

    private static final Logger logger = Logger.getLogger(IdGenerator.class.getName());

    
    public synchronized static void init(final long businessId, final long serverId) {
        if (Objects.isNull(snowFlakeIdGenerator)) {
            IdGenerator.businessId = businessId;
            IdGenerator.serverId = serverId;
            snowFlakeIdGenerator = new SnowFlakeIdGenerator(businessId, serverId);
            logger.log(Level.INFO, "IdGenerator init, businessId=%s,serverId=%s", new Object[]{businessId, serverId});
            return;
        }
        logger.log(Level.WARNING, " IdGenerator inited, businessId=%s,serverId=%s", new Object[]{businessId, serverId});
    }

    public static long getServerId(final Long id) {
        return (id >> SERVER_ID_LEFT_SHIFT) & 0x7F;
    }

    /**
     * @return
     */
    public static long nextId() {
        return snowFlakeIdGenerator.nextId();
    }


    /**
     * @return
     */
    public static long nextBillId() {
        return snowFlakeIdGenerator.nextId();
    }


    /**
     * @return
     */
    public static long nextOrderId() {
        return snowFlakeIdGenerator.nextId();
    }

    /**
     * {@link SnowFlakeIdGenerator#nextId()}  }
     *
     * @param time
     * @return
     */
    public static long getByTime(long time) {
        return time - 1546272000000L << 22 | businessId << 19 | serverId << 12;
    }

    /**
     * 生成请求ID
     *
     * @return
     */
    public static long nextRequestId() {
        return snowFlakeIdGenerator.nextId();
    }

    public static boolean isValid() {
        return Objects.nonNull(snowFlakeIdGenerator);
    }
}