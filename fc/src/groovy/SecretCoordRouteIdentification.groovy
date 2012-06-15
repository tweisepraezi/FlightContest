enum SecretCoordRouteIdentification 
{
	NONE                   ('fc.aflos.routedefs.secretpoint.identification.none'),
	GATEWIDTH2             ('fc.aflos.routedefs.secretpoint.identification.gatewidth'),
	SECRETMARK             ('fc.aflos.routedefs.secretpoint.identification.secretmark'),
	GATEWIDTH2ORSECRETMARK ('fc.aflos.routedefs.secretpoint.identification.gatewidthorsecretmark')
	
	SecretCoordRouteIdentification(String titleCode)
	{
		this.titleCode = titleCode
	}
	
	final String titleCode
	
}