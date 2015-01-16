import org.apache.commons.lang3.StringUtils;

public class Translator{
    public static void main(String[] args)
    {
		String inPath = args[0];
		String outPath = args[1];
		String filename = args[2];
		String translationType = args[3];

		System.out.println(inPath);
		System.out.println(outPath);
		System.out.println(filename);
		System.out.println(translationType);

		if(translationType.equals("NerToBrat"))
		{
			System.out.println("We made it!");
			NerToBratTranslation trans = new NerToBratTranslation(inPath, outPath, filename);
			trans.writeToFile();
		}

		if(translationType.equals("BratToNer"))
		{
			BratToNerTranslation trans = new BratToNerTranslation(inPath, outPath, filename);
		}
    }
}


