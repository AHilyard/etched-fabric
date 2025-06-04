package gg.moonflower.etched.api.sound.stream;

import net.minecraft.client.sounds.AudioStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Skips the second track of a stereo track to read as mono.
 *
 * @author Ocelot
 */
public class MonoWrapper implements AudioStream {

    private static final Logger LOGGER = LogManager.getLogger();

    private final AudioStream source;
    private final AudioFormat format;
    private final int sourceChannels;

    public MonoWrapper(AudioStream source) {
        this.source = source;
        AudioFormat sourceFormat = source.getFormat();
        this.sourceChannels = sourceFormat.getChannels();
        this.format = this.sourceChannels != 1 ? new AudioFormat(sourceFormat.getEncoding(), sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), 1, sourceFormat.getFrameSize() / sourceFormat.getChannels(), sourceFormat.getFrameRate(), sourceFormat.isBigEndian()) : sourceFormat;
    }

    @Override
    public AudioFormat getFormat() {
        return this.format;
    }

    @Override
    public ByteBuffer read(int amount) throws IOException {
        ByteBuffer parent = this.source.read(amount * this.sourceChannels);
        //.error("Read from source: " + parent.limit() + " bytes");
        if (this.sourceChannels == 1) {
            LOGGER.error("Single channel, returning original buffer");
            return parent;
        }

        ByteBuffer modified = BufferUtils.createByteBuffer(parent.limit() / this.sourceChannels);
        int step = this.format.getSampleSizeInBits() / Byte.SIZE;
        for (int j = 0; j < parent.limit(); j += step * 2) {
            for (int l = 0; l < step; l++) {
                modified.put(parent.get(j + l));
            }
        }
        modified.rewind();

        //LOGGER.error("Downmixed buffer size: {} bytes", modified.limit());

        return modified;
    }

    @Override
    public void close() throws IOException {
        this.source.close();
    }
}
