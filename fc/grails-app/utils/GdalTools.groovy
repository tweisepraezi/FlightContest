import org.gdal.gdal.BuildVRTOptions
import org.gdal.gdal.Dataset
import org.gdal.gdal.gdal

class GdalTools
{
    // ----------------------------------------------------------------------------------
    static void BuildVRT(String tifFileName, String vrtFileName)
    {
        gdal.AllRegister()
        Vector source_filenames = new Vector()
        source_filenames.add(tifFileName)
        Vector built_vrt_options = new Vector()
        Dataset vrt_data = gdal.BuildVRT(vrtFileName, source_filenames, new BuildVRTOptions(built_vrt_options))
        if (vrt_data) {
            vrt_data.delete()
        }
    }

    // ----------------------------------------------------------------------------------
    static void Gdal2Tiles(String workingFolder, String vrtFileName, String tilesFolderName)
    {
        vrtFileName = vrtFileName.replace('\\','/')
        //println "XX1 '${vrtFileName}' -> '${tilesFolderName}'"
        
        List<String> command = ["C:/Program Files/Python37/python.exe", "C:/Program Files/GDAL/gdal2tiles.py", "--tilesize=256", "-z 5-13", vrtFileName, tilesFolderName]
        ProcessBuilder process_builder = new ProcessBuilder(command)
        process_builder = process_builder.directory(new File(workingFolder))
        process_builder.redirectErrorStream(true)
        Process process = process_builder.start()
        
        InputStream inputstream_instance = process.getInputStream()
        BufferedReader input_reader = inputstream_instance.newReader("UTF-8")
        while (true) {
            String line = input_reader.readLine()
            if (line == null) {
                break
            }
            //println "XX2 $line"
        }
        input_reader.close()
        inputstream_instance.close()
    }

}
