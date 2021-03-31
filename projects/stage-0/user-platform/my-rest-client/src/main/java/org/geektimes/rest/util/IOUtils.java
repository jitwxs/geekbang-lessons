package org.geektimes.rest.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jitwxs
 * @date 2021-03-30 23:41
 */
public class IOUtils {
    private static final Logger logger = Logger.getLogger(IOUtils.class.getName());

    public static void writeJson(final OutputStream outputStream, final Object entity, final String charsetName) {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, charsetName)) {
            writer.write(new ObjectMapper().writeValueAsString(entity));
            writer.flush();
        } catch (IOException e) {
            logger.log(Level.FINER, "IOUtils writeJson error: " + e.getMessage());
        }
    }
}
