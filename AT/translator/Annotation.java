

public class Annotation
{
    public String ID;
    public String label;
    public int start;
    public int end;
    public String entity;

    public Annotation()
    {
        this.ID = "";
        this.label = "";
        this.start = 0;
        this.end = 0;
        this.entity = "";
    }

    public Annotation(int id, String label, int start, int end, String entity)
    {
        this.ID = "T"+id;
        this.label = label;
        this.start = start;
        this.end = end;
        this.entity = entity;
    }

    //just for testing
    public void printAnnotation()
    {
        System.out.println(this.ID + "\t" + this.label + " " + this.start + " " + this.end + " " + "\t" + this.entity);
    }

    //returns a byte stream that represents the annotation as one line
    //of the .ann file for writing out. terrible sentence.
    public byte[] annotationAsBytes()
    {
        String annotation =  new String(this.ID + "\t" + this.label + " " + this.start + " " + this.end + " " + "\t" + this.entity + "\n");
        return annotation.getBytes();
    }

    //setter for id field
    public void id(int id)
    {
        this.ID = "T"+id;
    }
}
