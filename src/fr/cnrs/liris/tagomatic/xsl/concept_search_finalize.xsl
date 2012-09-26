<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dc="http://purl.org/dc/terms"
    xmlns:oml="http://objectml.org/ns/200905/ObjectML">
    
    <xsl:template match="/">
    	<oml:object-list>
    		<xsl:copy-of select="//oml:object-list[1]/oml:object[not(@oml:store-id = following::oml:object/@oml:store-id)]" />
    	</oml:object-list>
    </xsl:template>   
</xsl:stylesheet>    