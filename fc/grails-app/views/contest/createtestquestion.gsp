<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>${message(code:'fc.contest.new.test')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="contest" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <div class="box boxborder" >
                <h2>${message(code:'fc.contest.new.test')}</h2>
                <div class="block" id="forms" >
                    <g:form>
                        <fieldset>
                            <p>
                                <g:set var="print_title_labels" value="${[]}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (mit Klassen)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (Beobachtungen)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (kombinierter Wettbewerb)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb Auswertung ohne Klassen']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb Auswertung mit Klassen']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (100 Besatzungen)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (20 Besatzungen)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (Strecken)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (Intermediate)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (Curved)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Demo Wettbewerb (Procedure Turn)']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Alle']}"/>
                                <g:set var="print_title_labels" value="${print_title_labels += ['Alle mit Tests']}"/>
	                            <g:radioGroup name="demoContest" labels="${print_title_labels}" values="[2,1,4,3,11,12,13,14,21,22,23,24,98,99]" value="2">
	                                <div>
	                                    <label>${it.radio} ${it.label}</label>
	                                </div>
                                </g:radioGroup>
                            </p>
                        </fieldset>
                        <input type="hidden" name="id" value="${contestInstance?.id}"/>
                        <g:actionSubmit action="createtest" value="${message(code:'fc.create')}" tabIndex="2"/>
                        <g:actionSubmit action="cancel" value="${message(code:'fc.cancel')}" tabIndex="3"/>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>