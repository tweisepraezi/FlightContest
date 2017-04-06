import org.springframework.web.context.request.RequestContextHolder
import java.util.List

class ImageService
{
    def logService
    def messageSource
    
    final static String JPG_EXTENSION = ".jpg"
    
    final static String IMAGE_EXTENSIONS = "${ImageService.JPG_EXTENSION}"
    
    //--------------------------------------------------------------------------
    Map LoadImage(String fileExtension, def file, def dataObj, String fieldName, int maxSize)
    {
        Map ret = [found: false, error: false, message: ""]
        
        String original_filename = file.getOriginalFilename()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.image.noimagefile',[original_filename, ImageService.IMAGE_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.image.nofileselected')
        } else {
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                printstart "Upload '$original_filename' to '$fieldName'"
                if (maxSize && file.size > maxSize) {
                    ret.found = true
                    ret.error = true
                    ret.message = getMsg('fc.image.notimported.size',[original_filename,file.size,maxSize])
                    
                    printerror ""
                } else {
                    // upload file
                    dataObj.(fieldName.toString()) = file.bytes
                    dataObj.save()
                    
                    ret.found = true
                    ret.message = getMsg('fc.image.imported',[original_filename])
                    
                    printdone ""
                }
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map LoadImage2(String fileExtension, File file, int maxSize)
    {
        Map ret = [found: false, error: false, message: "", bytes: null]
        
        String original_filename = file.toString()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.image.noimagefile',[original_filename, ImageService.IMAGE_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.image.nofileselected')
        } else {
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                printstart "Upload '$original_filename'"
                if (maxSize && file.size() > maxSize) {
                    ret.found = true
                    ret.error = true
                    ret.message = getMsg('fc.image.notimported.size',[original_filename,file.size(),maxSize])
                    
                    printerror ""
                } else {
                    ret.bytes = file.readBytes()
                    ret.found = true
                    ret.message = getMsg('fc.image.imported',[original_filename])
                    
                    printdone ""
                }
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    Map LoadLoggerFile(String fileExtension, File file)
    {
        Map ret = [found: false, error: false, message: "", bytes: null]
        
        String original_filename = file.toString()
        if (!fileExtension) {
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nologgerfile',[original_filename, LoggerFileTools.LOGGER_EXTENSIONS])
        } else if (!original_filename) {
            ret.found = true
            ret.error = true
            ret.message = getMsg('fc.flightresults.loggerimport.nofile')
        } else {
            if (original_filename.toLowerCase().endsWith(fileExtension)) {
                printstart "Upload '$original_filename'"
                ret.bytes = file.readBytes()
                ret.found = true
                ret.message = getMsg('fc.flightresults.loggerimport.imported',[original_filename])
                printdone ""
            }
        }
        return ret
    }
    
    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
    }
    
    //--------------------------------------------------------------------------
    private String getPrintMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.printLanguage))
    }
    
    //--------------------------------------------------------------------------
    void printstart(out)
    {
        logService.printstart out
    }

    //--------------------------------------------------------------------------
    void printerror(out)
    {
        if (out) {
            logService.printend "Error: $out"
        } else {
            logService.printend "Error."
        }
    }

    //--------------------------------------------------------------------------
    void printdone(out)
    {
        if (out) {
            logService.printend "Done: $out"
        } else {
            logService.printend "Done."
        }
    }

    //--------------------------------------------------------------------------
    void print(out)
    {
        logService.print out
    }

    //--------------------------------------------------------------------------
    void println(out)
    {
        logService.println out
    }
}
