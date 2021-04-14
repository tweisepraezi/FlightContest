class NetTools
{
    static List EMailList(String emailAdresses)
    {
        if (!emailAdresses) {
            return []
        }
        if (!emailAdresses.contains(Defs.EMAIL_AT_CHAR)) {
            return []
        }
        
        String email_separator = ""
        if (emailAdresses.contains(Defs.EMAIL_LIST_SEPARATOR)) {
            email_separator = Defs.EMAIL_LIST_SEPARATOR
        } else if (emailAdresses.contains(';')) {
            email_separator = ';'
        } else if (emailAdresses.contains(' ')) {
            email_separator = ' '
        }
        
        List email_list = []
        if (email_separator) {
            for(String email_adress in Tools.Split(emailAdresses,email_separator)) {
                if (email_adress.contains(Defs.EMAIL_AT_CHAR) && !email_adress.startsWith(Defs.EMAIL_AT_CHAR) && !email_adress.endsWith(Defs.EMAIL_AT_CHAR)) {
                    email_list += email_adress
                }
            }
        } else {
            email_list += emailAdresses
        }
        return email_list
    }
    
    static List EMailReducedList(String emailAdresses, String reduceEmailAdresses)
    {
        List email_list = []
        List reduce_list = EMailList(reduceEmailAdresses)
        for (String email in EMailList(emailAdresses)) {
            if (!(email in reduce_list)) {
                email_list += email
            }
        }
        return email_list
    }
    
    static Map EMailLinks(String fileName)
    {
        String file_name = fileName.substring(0,fileName.lastIndexOf('.'))
        return [map:"${file_name}.htm", pdf:"${file_name}.pdf", kmz:"${file_name}.kmz", gpx:"${file_name}.gpx"]
    }
}
