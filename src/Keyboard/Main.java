package Keyboard;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;


public class Main {
	
	/*
	 * Ideas program piano to play sheet music (I think this involves a sequencer)
	 * 
	 * Make it sound like a harpsichord
	 */
	
	/*
	 * TODO: First be able to get a harpsichord sound
	 * on either the keyboard or out of a built of the computer via synthesizer
	 */
	
	/*
	 * TODO: look into wiring with a sequencer (looping would be interesting)
	 */
	static int HARPSICHORD = 6;
		
	static String outMidi = "class com.sun.media.sound.MidiOutDeviceProvider$MidiOutDeviceInfo";
	static String inMidi = "class com.sun.media.sound.MidiInDeviceProvider$MidiInDeviceInfo";

	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
		
		Synthesizer other = (Synthesizer) getOtherSynth();
		System.out.println(other);
		other.open();
		System.out.println(other.getLatency()/1000.0);
		Receiver r = other.getReceiver();
		System.out.println(r);
		Synthesizer synth = MidiSystem.getSynthesizer();
		synth.open();
		System.out.println(synth);
		System.out.println(synth.getLatency()/1000.0);
		
		/* 
		String myDeviceName = "Roland Digital Piano";
		MidiDevice device = getInDevice(myDeviceName); // Transmitter
		device.close();
		// Can send data to the keyboard with this
		MidiDevice keyboardReceiver = getOutDevice(myDeviceName);
		// Receiver I wrote that pipes through to default synthesizer
		Receiver customReceiver = new MidiInputReceiver();
		
		connectTransmitterDeviceToReceiver(device, customReceiver);
		*/
		
		
		
		
		// Devices ignore timestamps (Both below calls give -1)
		/*System.out.println(device.getMicrosecondPosition());
		System.out.println(keyboardReceiver.getMicrosecondPosition());*/ 

	}
	
	public static MidiDevice getOtherSynth() throws MidiUnavailableException {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		System.out.println(info[1]);
		return MidiSystem.getMidiDevice(info[0]);
	}
	
	public static void connectTransmitterDeviceToReceiver(MidiDevice transmitterDevice, Receiver rcvr) throws MidiUnavailableException, InterruptedException {
		
		try {
			if (!(transmitterDevice.isOpen())) {
				transmitterDevice.open();
				Transmitter trans = transmitterDevice.getTransmitter();  
				trans.setReceiver(rcvr); 
			}
		} finally {
			System.out.println("cleaning up");
			//transmitterDevice.close();
			//rcvr.close();
		}
		
	}
	
	public static void sendMessage(Receiver rcvr) throws InvalidMidiDataException, InterruptedException, MidiUnavailableException {
		ShortMessage myMsg = new ShortMessage();
		// Start playing the note Middle C (60), 
		// moderately loud (velocity = 93).
		myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 99);
		long timeStamp = -1;
		rcvr.send(myMsg, timeStamp);
		
	}
	
	public static void sendMessageDefaultSynth() throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
		ShortMessage myMsg = new ShortMessage();
		// Start playing the note Middle C (60), 
		// moderately loud (velocity = 93).
		int nChannelNumber = 1;
		myMsg.setMessage(ShortMessage.NOTE_ON, nChannelNumber, 60, 99);
		long timeStamp = -1;
		Synthesizer synth = MidiSystem.getSynthesizer();
		System.out.println(synth);
		synth.open();
		Receiver synthRcvr = synth.getReceiver();
		MidiChannel[]   channels = synth.getChannels();
		MidiChannel channel = channels[nChannelNumber];
		channel.programChange(HARPSICHORD);
		synthRcvr.send(myMsg, timeStamp);
		Thread.sleep(10000); // Need to wait or the program stops before note finishes
	}
	
	/*
	 * Gets the MIDI channel that transmits music from the keyboard
	 */
	public static MidiDevice getInDevice(String name) throws MidiUnavailableException {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < info.length; i++) {
			if (info[i].getName().equals(name) && info[i].getClass().toString().equals(inMidi)) {
				return MidiSystem.getMidiDevice(info[i]);
			}
		}
		throw new MidiUnavailableException("Device not found.");
	}
	
	/*
	 * Gets the output MIDI to the keyboard
	 */
	public static MidiDevice getOutDevice(String name) throws MidiUnavailableException {
		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < info.length; i++) {
			if (info[i].getName().equals(name) && info[i].getClass().toString().equals(outMidi)) {
				return MidiSystem.getMidiDevice(info[i]);
			}
		}
		throw new MidiUnavailableException("Device not found.");
	}
	

}


