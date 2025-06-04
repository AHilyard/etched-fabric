package gg.moonflower.etched.api.util.Flac;

import javazoom.jl.decoder.Obuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FlacInputStream  extends InputStream implements PCMProcessor {

    private static final Logger LOGGER = LogManager.getLogger();
    private final FLACDecoder decoder;
    private AudioFormat format;
    private ByteBuffer buffer;

    public FlacInputStream(InputStream input) throws IOException {
        this.decoder = new FLACDecoder(input);

        this.decoder.addPCMProcessor(this);

        if(this.fillBuffer()) {
            throw new IOException("Failed to fill buffer");
        }

//        StreamInfo info = decoder.readStreamInfo();
//        this.format = new AudioFormat(
//                info.getSampleRate(),
//                info.getBitsPerSample(),
//                info.getChannels(),
//                true,
//                true
//        );
    }

    private boolean fillBuffer() {
        try {
            decoder.decode();
            return false;
        } catch (IOException e) {
            LOGGER.error(e);
            return true;
        }
    }

    @Override
    public int read() throws IOException {
            if (!this.buffer.hasRemaining()) {
                return -1;
            }
            return ((int) this.buffer.get()) & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = 0;
        while (read < len) {
            if (!this.buffer.hasRemaining()) {
                return read > 0 ? read : -1;
            }

            int readLength = Math.min(this.buffer.remaining(), len - read);
            this.buffer.get(b, off + read, readLength);
            read += readLength;
        }

        return read;
    }

    @Override
    public int available() {
        return this.buffer.remaining();
    }

    @Override
    public void processStreamInfo(StreamInfo streamInfo) {
        this.format = new AudioFormat(
                streamInfo.getSampleRate(),
                streamInfo.getBitsPerSample(),
                streamInfo.getChannels(),
                true,
                true
        );
    }

    @Override
    public void processPCM(ByteData pcm) {
        byte[] data = pcm.getData();
        if (this.buffer == null || this.buffer.capacity() < data.length) {
            this.buffer = ByteBuffer.allocate(data.length).order(ByteOrder.BIG_ENDIAN);
        } else {
            this.buffer.clear(); // Reset for new data
        }
        this.buffer.put(data);
        this.buffer.flip();
    }

    public AudioFormat getFormat() {
        return this.format;
    }
}
