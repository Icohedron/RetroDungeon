package io.github.icohedron.retrodungeon.sound;

import javax.sound.sampled.Clip;

// Holds a sound clip and plays it
public class Sound {

	private Clip clip;
	
	public Sound(Clip clip) {
		this.clip = clip;
	}
	
	// Play the sound clip
	public void play() {
		if (clip != null) {
			new Thread(() -> { // The lambda expression makes this short and sweet!
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
			}, "RetroDungeon Sound Clip").start();
		}
	}
}
