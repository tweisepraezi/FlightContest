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
}
