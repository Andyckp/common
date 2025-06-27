package com.ac.derivativepricer.process;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Util {
    public static char[] padOrTruncate(String input, int length) {
        char[] result = new char[length];
        Arrays.fill(result, ' '); 
        
        for (int i = 0; i < Math.min(input.length(), length); i++) {
            result[i] = input.charAt(i);
        }
        return result;
    }

    public static File createTempDir()
    {
        final File tempDir;
        try
        {
            tempDir = File.createTempFile("archive", "tmp");
        }
        catch (final IOException ex)
        {
            throw new RuntimeException(ex);
        }

        if (!tempDir.delete())
        {
            throw new IllegalStateException("Cannot delete tmp file!");
        }

        if (!tempDir.mkdir())
        {
            throw new IllegalStateException("Cannot create folder!");
        }

        return tempDir;
    }
}
