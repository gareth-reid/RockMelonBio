package tools
/**
 * Created by reid gareth on 8/09/2015.
 */
public class PercentageBaseCalls {


    static void main(String[] args) {
        Execute(args)
    }

    //java -cp BioinformaticsTools.jar tools.PercentageBaseCalls -r /tmp/151106_M01053_0294_000000000-AKH6W -p 5073770 -o /tmp/done.csv
    public static void Execute(String[] args) {
        if (!args.contains("-r") || !args.contains("-p") || !args.contains("-o")){
            println("Incorrect parameters - Usage: java -cp BioinformaticsTools.jar tools.PercentageBaseCalls -r [PATHTORUNFOLDER] -p [GENOMICPOSITION] -o [OUTFILE]")
            System.exit(-1)
        }

        def runPos = args.toList().indexOf("-r")
        def run = args[runPos + 1]
        def chrPos = args.toList().indexOf("-p")
        def chr = args[chrPos + 1]
        def outFilePos = args.toList().indexOf("-o")
        def outFile = args[outFilePos + 1]

        println("Run: ${run}, Position: ${chr}")
        def sampleForError = ""
        try {
            def runFolder = new File(run)
            def results = new File(outFile)
            results.append(",A, C, G, T \n")

            def files = runFolder.listFiles()
            for (f in files) {
                try {
                    def sampleName = f.name.toString()
                    sampleForError = sampleName
                    def file = "${runFolder}/${sampleName}/${sampleName}.bam"

                    def data = "samtools mpileup -d 1000000 ${file}".execute().text
                    def dataArray = data.split("\n")
                    def position
                    dataArray.each { line ->
                        if (line.contains(chr)) {
                            position = line.split(/\t/)
                        }
                    }
                    def aCount = 0
                    def cCount = 0
                    def gCount = 0
                    def tCount = 0


                    for (c in position[4]) {
                        if (c == 'A') {
                            aCount++
                        }
                        if (c == 'C') {
                            cCount++
                        }
                        if (c == 'G') {
                            gCount++
                        }
                        if (c == 'T') {
                            tCount++
                        }
                    }
                    results.append("${sampleName}, ${aCount}, ${cCount}, ${gCount}, ${tCount}\n")
                } catch (Exception e){
                    System.out.println("Error (inner): ${e}, Sample: ${sampleForError}")
                }
            }
        } catch(Exception e){
            System.out.println("Error: ${e}, Sample: ${sampleForError}")
        }


    }
}
