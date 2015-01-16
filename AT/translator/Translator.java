import org.apache.commons.lang3.StringUtils;

public class Translator{
    public static void main(String[] args)
    {
        /*
        String mystring = "Let's see how this works.";
        System.out.println("mystring: " + mystring);
        String[] split = StringUtils.split(mystring);

        System.out.println("split[0]: " + split[0]);
        System.out.println("split[3]: " + split[3]);
        String join03;

        join03 = StringUtils.join(new String[] {split[0], split[3]}, " ");
        System.out.println("split[0], split[3] join: " + join03);

        System.out.println("split[2]: " + split[2]);
        System.out.println("split[3]: " + split[3]);
        String join23 = StringUtils.join(new String[] {split[2], split[3]}, " ");
        System.out.println("split[2], split[3] join: " + join23);
       

        
        Annotation antTest1 = new Annotation();
        antTest1.printAnnotation();

        Annotation antTest2 = new Annotation(1, "PERSON", 0, 6, "Chase");
        antTest2.printAnnotation();
		*/
        //NerToBratTranslation trans = new NerToBratTranslation("/Users/chaseduncan/Desktop/thesis/data/test/", "001_tagged.txt", "/Users/chaseduncan/Desktop/thesis/brat-v1.3_Crunchy_Frog/data/jerry/");

        //NerToBratTranslation trans = new NerToBratTranslation("./", "001_tagged.txt", "/Users/chaseduncan/Desktop/thesis/brat-v1.3_Crunchy_Frog/data/examples/CoNLL-ST_2002/esp/");

		//trans.writeToFile();
		

		BratToNerTranslation otherTrans = new BratToNerTranslation("/Users/chaseduncan/Desktop/thesis/brat-v1.3_Crunchy_Frog/data/jerry/", "./", "001_tagged");
    }
}


