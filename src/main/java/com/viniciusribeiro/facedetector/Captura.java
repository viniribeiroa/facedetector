package com.viniciusribeiro.facedetector;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.awt.event.KeyEvent;
import java.util.Scanner;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class Captura {

	public static void main(String arg[]) throws Exception, InterruptedException {

		KeyEvent tecla = null;
		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();

		OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
		camera.start();

		CascadeClassifier detectorFace = new CascadeClassifier(
				"src\\main\\java\\com\\viniciusribeiro\\facedetector\\resource\\haarcascade_frontalface_alt.xml");

		CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
		Frame frameCapturado = null;
		Mat imagemColorida = new Mat();

		int numeroAmostras = 25;
		int amostra = 1;
		System.out.println("Digite seu codigo: ");
		Scanner sc = new Scanner(System.in);
		int pessoa = sc.nextInt();
		sc.close();

		while ((frameCapturado = camera.grab()) != null) {

			imagemColorida = converteMat.convert(frameCapturado);
			Mat imagemCinza = new Mat();
			cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);

			RectVector facesDetectadas = new RectVector();
			detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150),
					new Size(500, 500));
			
			if(tecla == null) {
				tecla = cFrame.waitKey(5);
			}

			for (int i = 0; i < facesDetectadas.size(); i++) {

				Rect dadosFace = facesDetectadas.get(0);
				rectangle(imagemColorida, dadosFace, new Scalar(0, 0, 255, 0));
				Mat faceCapturada = new Mat(imagemCinza, dadosFace);
				resize(faceCapturada, faceCapturada, new Size(160, 160));
				
				if(tecla == null) {
					tecla = cFrame.waitKey(5);
				}
				
				if(tecla != null) {
					if(tecla.getKeyChar() == 'q') {
						if(amostra <= numeroAmostras) {
							imwrite("src\\main\\java\\com\\viniciusribeiro\\facedetector\\fotos\\pessoa." + pessoa + "."+ amostra + ".jpg", faceCapturada);
							System.out.println("foto " + amostra + ".capturada\n");
							amostra++;
						}
					}
					tecla = null;
				}
			}
			
			if(tecla == null) {
				tecla = cFrame.waitKey(5);
			}
			
			if (cFrame.isVisible()) {
				cFrame.showImage(frameCapturado);
			}
			
			if(amostra > numeroAmostras) {
				break;
			}
		}
		
		cFrame.dispose();
		camera.stop();
	}

}
