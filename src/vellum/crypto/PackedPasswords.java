/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author evan
 */
public class PackedPasswords {
    
    public static byte[] hashPassword(char[] password)
            throws GeneralSecurityException, IOException {
        return hashPassword(password, Passwords.ITERATION_COUNT, Passwords.KEY_SIZE);
    }

    public static byte[] hashPassword(char[] password, int iterationCount, int keySize)
            throws GeneralSecurityException, IOException {
        return new PasswordHash(password, iterationCount, keySize).getBytes();
    }

    public static boolean matches(char[] password, byte[] packedBytes)
        throws IOException, GeneralSecurityException {
            return new PasswordHash(packedBytes).matches(password);
        }
    }
