package com.viniciusribeiro.facedetector;


import java.awt.event.KeyEvent;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.Frame;

public class Captura {
	
	public static void main(String arg[]) throws Exception {
		
		KeyEvent tecla = null;
		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
		
		OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
		camera.start();
		
		CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma()/ camera.getGamma());
		Frame frameCapturado = null;
		while((frameCapturado = camera.grab()) != null) {
			
			if(cFrame.isVisible()) {
				cFrame.showImage(frameCapturado);
			}
		}
		cFrame.dispose();
		camera.stop();
	}

}
