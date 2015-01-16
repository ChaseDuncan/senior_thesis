
import org.apache.commons.lang3.StringUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.lang.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.charset.StandardCharsets;

public class NerToBratTranslation
{
    Path pathToFile;
    Path pathToAnn;
    Path pathToTxt;

    String fileRoot;
    String rawText;
    String outText;
    String[] splitText;

    //This is used for comparison so that when we join strings
    //that are just punctuation to the outText we don't add
    //a space. Without the output looks like "hello , world !"
    //when we want "hello, world!"
    String punctuations = ",.;:'-_\"\"?)(!$%^&)'";
    Vector<Annotation> annotations = new Vector<Annotation>();

    int splitIdx; 
    int annId;
    boolean firstEntity;

    public NerToBratTranslation(String inPath, String inFile, String outPath)
    {
        //assumes that the file has some sort of extension and removes it.
        fileRoot = StringUtils.substringBeforeLast(inFile, ".");

        this.pathToFile = Paths.get(inPath+inFile);
        this.pathToAnn = Paths.get(outPath + fileRoot + ".ann");
        this.pathToTxt = Paths.get(outPath + fileRoot + ".txt");

        try{
            byte[] temp = Files.readAllBytes(this.pathToFile);
            this.rawText = new String(temp);
        }catch(IOException e){
            e.printStackTrace();
        }

        this.splitText = StringUtils.split(this.rawText);

        splitIdx = 0; 
        annId = 1;

        while(splitIdx < splitText.length)
        {
            String currStr = splitText[splitIdx];
            if(currStr.charAt(0) == '[')
            {
                //have to check if the entity is the first word in 
                //the document so that the start offset can be set
                //correctly.
                if(splitIdx == 0)
                    firstEntity = true;

                Annotation annotation = new Annotation();
                annotation.id(annId);
                annId++;

                String label = StringUtils.substring(currStr, 1, currStr.length());
                annotation.label = label;

                splitIdx++;
                
                String nextStr = splitText[splitIdx];
                String entity = null;
                do{
                    nextStr = splitText[splitIdx];
                    entity = StringUtils.join(new String[]{entity, nextStr}, " "); 
                    splitIdx++;
                }while(nextStr.charAt(nextStr.length() - 1) != ']');
                entity = StringUtils.substring(entity, 1, entity.length() - 1);
                annotation.entity = entity;
                if(!firstEntity)
                    annotation.start = outText.length() + 1;

                outText = StringUtils.join(new String[] {outText, entity}, " ");

                //there's a small chance for an edge case here where depending 
                //on how the document ends and if the named entity is the last
                //thing in the document. unless it comes up i'm ignoring it for
                //now.
                annotation.end = outText.length();
                annotations.add(annotation);
            }else{
                if(punctuations.contains(currStr))
                {
                    outText = StringUtils.join(new String[] {outText, currStr});
                }else{
                    outText = StringUtils.join(new String[] {outText, currStr}, " ");
                }

                splitIdx++;
            }
        }   
        /*
        for(int i = 0; i < annotations.size(); i++)
        {
            Annotation ann = annotations.get(i);
            ann.printAnnotation();
        }
        */
    }

    public void writeToFile()
    {
        try{ 
            Files.write(pathToAnn, annotations.get(0).annotationAsArrayList(), StandardCharsets.UTF_8, CREATE);

            for(int i = 1; i < annotations.size(); i++)
                Files.write(pathToAnn, annotations.get(i).annotationAsArrayList(), StandardCharsets.UTF_8, APPEND);
            Files.write(pathToTxt, outText.getBytes(), CREATE);
        }catch(IOException e){
            e.printStackTrace(); 
        }
    
    }
}
