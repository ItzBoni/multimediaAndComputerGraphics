package utils;

import java.util.ArrayList;

public class BitPacker {

    /**
     * Packs a list of integers (indices) into a byte array.
     * @param indices The list of color indices (e.g., 0, 1, 3...)
     * @param bitsPerPixel How many bits each index requires (e.g., 2 bits for 4 colors)
     * @return Packed byte array
     */
    public static byte[] packIndices(ArrayList<Integer> indices, int bitsPerPixel) {
        // 1. Calculate the size of the output array
        long totalBits = (long) indices.size() * bitsPerPixel;
        // Divide by 8, rounding up to capture the last partial byte
        int byteLength = (int) ((totalBits + 7) / 8);

        byte[] packedData = new byte[byteLength];

        long buffer = 0;       // The "funnel" (accumulator)
        int bitsInBuffer = 0; // How full the funnel is
        int currentByteIndex = 0;

        for (int index : indices) {
            // Mask the index to ensure it doesn't have trash bits higher than bitsPerPixel
            // Example: if bitsPerPixel is 3, mask with 0x07 (00000111)
            int mask = (1 << bitsPerPixel) - 1;
            int cleanIndex = index & mask;

            // 2. Push the new bits into the buffer
            // Shift the buffer to the left to make room, then OR the new index in
            buffer = (buffer << bitsPerPixel) | cleanIndex;
            bitsInBuffer += bitsPerPixel;

            // 3. Extract full bytes while we have them
            while (bitsInBuffer >= 8) {
                // Determine how much we need to shift right to get the top 8 bits
                int shiftAmount = bitsInBuffer - 8;

                // Extract the top 8 bits
                byte extractedByte = (byte) ((buffer >> shiftAmount) & 0xFF);
                packedData[currentByteIndex++] = extractedByte;

                // Reduce the bit count
                bitsInBuffer -= 8;

                // OPTIONAL CLEANUP: You don't strictly *need* to clear the used bits 
                // from 'buffer' immediately because the next << will push them out 
                // of the relevant window, but keeping the buffer clean is safer for debugging.
                // This keeps only the remaining 'bitsInBuffer' bits.
                buffer &= (1L << bitsInBuffer) - 1;
            }
        }

        // 4. Handle Leftovers
        // If there are bits left in the buffer (e.g., we have 4 bits but need 8),
        // we shift them to the left to align them to the start of the byte (padding the end with 0s).
        if (bitsInBuffer > 0) {
            packedData[currentByteIndex] = (byte) ((buffer << (8 - bitsInBuffer)) & 0xFF);
        }

        return packedData;
    }

    /**
     * Unpacks a byte array back into a list of indices.
     */
    public static int[] unpackIndices(byte[] packedData, int totalPixels, int bitsPerPixel) {
        int[] indices = new int[totalPixels];

        long buffer = 0;
        int bitsInBuffer = 0;
        int byteIndex = 0;
        int pixelIndex = 0;

        // Loop until we have found all the pixels we expect
        while (pixelIndex < totalPixels) {

            // Refill the buffer if we don't have enough bits for the next pixel
            // and we still have bytes left to read
            while (bitsInBuffer < bitsPerPixel && byteIndex < packedData.length) {
                // Read next byte (handle signed byte issue with & 0xFF)
                int nextByte = packedData[byteIndex++] & 0xFF;

                // Add it to the buffer
                buffer = (buffer << 8) | nextByte;
                bitsInBuffer += 8;
            }

            if (bitsInBuffer >= bitsPerPixel) {
                // Calculate shift to bring the target bits to the "ones" position
                int shiftAmount = bitsInBuffer - bitsPerPixel;

                // Create a mask (e.g., for 3 bits: 111 binary)
                int mask = (1 << bitsPerPixel) - 1;

                // Extract the value
                int index = (int)((buffer >> shiftAmount) & mask);
                indices[pixelIndex++] = index;

                // Update buffer state
                bitsInBuffer -= bitsPerPixel;

                // Optional: Clean buffer to prevent overflow if processing huge streams
                // (Though with ints and 8-bit refills, overflow is rare unless bitsPerPixel > 24)
                buffer &= (1L << bitsInBuffer) - 1;
            } else {
                // Should not happen if logic is correct
                break;
            }
        }

        return indices;
    }
}