package com.viniciusribeiro.facedetector;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

public class Treinamento {
	
	public static void main(String args[]) {
		File diretorio = new File("src\\main\\java\\com\\viniciusribeiro\\facedetector\\fotos");
		FilenameFilter filtroImagem = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String nome) {
				return nome.endsWith(".jpg") || nome.endsWith(".gif") ||nome.endsWith(".png");
			}
		};
		
		File[] arquivos = diretorio.listFiles(filtroImagem);
		MatVector fotos = new MatVector(arquivos.length);
		Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
		IntBuffer rotulosBuffer = rotulos.createBuffer();
		int contador = 0;
		for(File imagem: arquivos) {
			Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
			int classe = Integer.parseInt(imagem.getName().split("\\.")[1]);
			//System.out.println(classe);
			resize(foto,foto,new Size(160,160));
			fotos.put(contador, foto);
			rotulosBuffer.put(contador, classe);
			contador++;
		}
		FaceRecognizer eigenfaces = EigenFaceRecognizer.create();
		FaceRecognizer fisherfaces = FisherFaceRecognizer.create();
		FaceRecognizer lbph = LBPHFaceRecognizer.create(2,9,9,9,1);
		
		eigenfaces.train(fotos, rotulos);
		eigenfaces.save("src\\main\\java\\com\\viniciusribeiro\\facedetector\\resource\\classificadorEigenFaces.yml");
		
		fisherfaces.train(fotos, rotulos);
        fisherfaces.save("src\\main\\java\\com\\viniciusribeiro\\facedetector\\resource\\classificadorFisherFaces.yml");
        
        lbph.train(fotos, rotulos);
        lbph.save("src\\main\\java\\com\\viniciusribeiro\\facedetector\\resource\\classificadorLBPH.yml");
		
	}
	
	
}
