
class ControllerTools
{
    // ----------------------------------------------------------------------------------
    static String GetFcUrl(def request)
    {
        String url = request.getRequestURL()
        String servlet_path = request.getServletPath()
        return url.substring(0,url.size()-servlet_path.size())
    }

}
