package Keyboard;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class MidiInputReceiver implements Receiver {
	
    // TODO: Bug, the keyboard should be sending note off messages
	// but the notes are lasting too long
	
	// also it sounds like it needs more channels maybe
	
	Synthesizer synth;
	Receiver synthRcvr; 
	final int nChannelNumber = 1;
	final int HARPSICHORD = 6;
	
	MidiInputReceiver() throws MidiUnavailableException {
		// TODO: a bit sloppy to have this here (refactor this)
		synth = MidiSystem.getSynthesizer();
		synth.close();
		synth.open();
		System.out.println("initial lag: " + synth.getLatency()/1000.0);
		this.synthRcvr = synth.getReceiver();
		MidiChannel[] channels = synth.getChannels();
		// Trying to set every channel to harpsichord since I don't know which its
		// sending to
		for (int i = 0; i < channels.length; i++) {
			channels[i].programChange(HARPSICHORD);
		}
		
		testConfiguration(channels);
		
		System.out.println("Successfully set up receiver");
	}
	
	/* 
	 * Plays a test note upon set up
	 */
	private void testConfiguration(MidiChannel[] channels) {
		MidiChannel channel = channels[nChannelNumber];
		channel.programChange(HARPSICHORD);
		channel.noteOn(60, 60);
	}
	
    @Override
	public void send(MidiMessage message, long timeStamp) {
		// TODO Auto-generated method stub
    	//System.out.println("Sending msg: " + message + " at time: " + timeStamp);
    	assert(synthRcvr != null);
    	try {
			// The time stamp of the input msg has a large number
    		// setting it to -1 here will have it played instantly on default synth
    			
    		synthRcvr.send(message, -1); 
    		System.out.println("latency: "+ synth.getLatency()/1000.0); // convert to ms
    	} catch (Exception e) {
    		//System.out.println(e);
    	}
	}
    
    public void close() {
    	synth.close();
    }
    
}
