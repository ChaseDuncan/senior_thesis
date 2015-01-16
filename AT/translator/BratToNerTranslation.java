import org.apache.commons.lang3.StringUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.lang.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.charset.StandardCharsets;

public class BratToNerTranslation
{
	Path pathToAnn;
	Path pathToTxt;
	Path pathToOutput;

	String rawText;
	String outText;
	
	Vector< Annotation > annotations = new Vector< Annotation >();

	//Only pass in suffix of the file for the associated .ann and .txt
	//files. For instance, myPath/myFile.ann and myPath/myFile.txt are
	//simultaneously handled by the call
	//BratToNerTranslation(myPath, outPath, myFile);
	public BratToNerTranslation(String inPath, String outPath, String fileName)
	{
		pathToAnn = Paths.get(inPath + fileName + ".ann");
		pathToTxt = Paths.get(inPath + fileName + ".txt");
		pathToOutput = Paths.get(outPath + fileName + ".txt");
	
		//using readAllBytes and converting to string since it seems
		//to be the simplest way to maintain the character indices in the file.
		try{
			rawText = new String(Files.readAllBytes(pathToTxt), StandardCharsets.UTF_8);
		}catch(IOException e){
			System.err.format("IOException: %s%n", e);
		}

		annotations = parseAnnotations(pathToAnn);
		convertToBracketNotation();	
		writeToFile(pathToOutput);

	}

	private Vector< Annotation > parseAnnotations(Path pathToAnn)
	{

		Vector< Annotation > annotations = new Vector< Annotation >();
		try{
		List< String > annotationList = Files.readAllLines(pathToAnn);

		for(int i = 0; i < annotationList.size(); i++)
		{
			String removeTabs = new String(StringUtils.replaceChars(annotationList.get(i), '\t', ' '));
			annotations.add(stringToAnnotation(removeTabs));
		}
		}catch(IOException e){
			System.err.format("IOException: %s%n", e);
		}

		return annotations;
	}

	private Annotation stringToAnnotation(String str)
	{
		String[] tokens = StringUtils.split(str, " ");
		Annotation annotation = new Annotation();
		annotation.ID = tokens[0];
		annotation.label = tokens[1];
		annotation.start = Integer.parseInt(tokens[2]);
		annotation.end = Integer.parseInt(tokens[3]);
		annotation.entity = tokens[4];

		int tokensIdx = 5;

		while(tokensIdx < tokens.length)
		{
			annotation.entity = StringUtils.join(new String[]{annotation.entity, tokens[tokensIdx]}, ' ');
			tokensIdx++;
		}

		return annotation;
	}
	
	private void convertToBracketNotation()
	{
		int startIdx = 0;
		int endIdx = 0;
		String betweenString = null;
		//need  to take care of text after last annotation
		//if it exists
		for(int i = 0; i < annotations.size(); i++)
		{
			Annotation currAnnotation = annotations.get(i);
			endIdx = currAnnotation.start - 1;

			//there's almost surely edge cases I'm not thinking of here
			betweenString = rawText.substring(startIdx, endIdx);

			if(outText == null)
			{
				outText = betweenString;	
			}else{
				outText = StringUtils.join(new String[]{outText, betweenString}, ' ');
				
			}
			
			String newAnnotation = new String("[" + currAnnotation.label + " " + currAnnotation.entity + "]");
			outText = StringUtils.join(new String[]{outText, newAnnotation}, " ");
			startIdx = currAnnotation.end;
		}
		
		String lastBit = StringUtils.substring(rawText, startIdx, rawText.length() - 1);
		if(!lastBit.isEmpty())
			outText = StringUtils.join(new String[]{outText, lastBit}, ' ');
	}

	public void writeToFile(Path pathToOutput) 
	{
		try{
			Files.write(pathToOutput, outText.getBytes(), CREATE);
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}
	public static void main(String[] args)
	{
		System.out.println("hello world!");
	}
}

