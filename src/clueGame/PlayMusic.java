package clueGame;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class PlayMusic {
	
	public static void LoopMusic(String fileLocation) {
		try {
			File musicLocation = new File(fileLocation);
			
			if(musicLocation.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicLocation);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();
			} else {
				System.out.println("Can't find file");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void playMusic() {
		String filePath = "data/background_music.wav";
		LoopMusic(filePath);
	}
}
