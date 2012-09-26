<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dc="http://purl.org/dc/terms"
    xmlns:oml="http://objectml.org/ns/200905/ObjectML"
    xmlns:xlink="http://www.w3.org/1999/xlink">
    
    <xsl:template match="/">
    	<oml:object-list>
    		<xsl:apply-templates select="//photo" />
    	</oml:object-list>
    </xsl:template>
    
    <xsl:template match="photo">
    	<oml:reference>
    		<xsl:attribute name="xlink:href">http://farm<xsl:value-of select="@farm" />.staticflickr.com/<xsl:value-of select="@server" />/<xsl:value-of select="@id" />_<xsl:value-of select="@secret" />.jpg</xsl:attribute>
    		<xsl:attribute name="dc:spatial"><xsl:value-of select="location/@latitude" />:<xsl:value-of select="location/@longitude" /></xsl:attribute>
    	</oml:reference>
    </xsl:template>
</xsl:stylesheet>    