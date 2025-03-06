package gg.moonflower.etched.api.util.AAC;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import net.sourceforge.jaad.adts.ADTSDemultiplexer;

public class AACInputStream extends AsynchronousAudioInputStream
{
	private final ADTSDemultiplexer adts;
	private final Decoder decoder;
	private final SampleBuffer sampleBuffer;
	private AudioFormat audioFormat = null;
	private byte[] saved;

	public AACInputStream(InputStream in) throws IOException {
		super(in, new AudioFormat(0, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, true, true), AudioSystem.NOT_SPECIFIED);
		adts = new ADTSDemultiplexer(in);
		decoder = new Decoder(adts.getDecoderSpecificInfo());
		sampleBuffer = new SampleBuffer();

		getFormat();
	}

	@Override
	public AudioFormat getFormat() {
		if(audioFormat==null) {
			//read first frame
			try {
				decoder.decodeFrame(adts.readNextFrame(), sampleBuffer);
				audioFormat = new AudioFormat(sampleBuffer.getSampleRate(), sampleBuffer.getBitsPerSample(), sampleBuffer.getChannels(), true, true);
				saved = sampleBuffer.getData();
			}
			catch(IOException e) {
				return null;
			}
		}
		return audioFormat;
	}

	public void execute() {
		try {
			if(saved==null) {
				decoder.decodeFrame(adts.readNextFrame(), sampleBuffer);
				buffer.write(sampleBuffer.getData());
			}
			else {
				buffer.write(saved);
				saved = null;
			}
		}
		catch(IOException e) {
			buffer.close();
			return;
		}
	}
}
