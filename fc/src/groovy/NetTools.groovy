class NetTools
{
    static List EMailList(String emailAdresses)
    {
        if (!emailAdresses) {
            return []
        }
        if (!emailAdresses.contains('@')) {
            return []
        }
        
        String email_separator = ""
        if (emailAdresses.contains(',')) {
            email_separator = ','
        } else if (emailAdresses.contains(';')) {
            email_separator = ';'
        } else if (emailAdresses.contains(' ')) {
            email_separator = ' '
        }
        
        List email_list = []
        if (email_separator) {
            for(String email_adress in emailAdresses.split(email_separator)) {
                if (email_adress.contains('@') && !email_adress.startsWith('@') && !email_adress.endsWith('@')) {
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
        return [map:"${file_name}.htm", pdf:"${file_name}.pdf", kmz:"${file_name}.kmz"]
    }
}
