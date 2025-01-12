<html>
    <head>
        <style type="text/css">
            @page {
                size: A4 landscape;
                margin-left: 3%;
                margin-right: 3%;
                margin-top: 5%;
                margin-bottom: 5%;
                @top-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${mapName}"
                }
                @top-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                }
                @bottom-left {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${contestInstance.printOrganizer}"
                }
                @bottom-right {
                    font-family: Noto Sans;
                    font-size: 90%;
                    content: "${message(code:'fc.program.printfoot.right')}"
                }
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="contestmap" />
    </head>
    <body>
        <g:form>
            <table class="anr">
                <tbody>
                    <tr>
                        <td>
                            <img src="${mapFileName}" style="width: 178mm;"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </g:form>
    </body>
</html>