<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
		
	<xsl:template match="/">

		<person>
	
			<xsl:for-each select="table/tr/td/table/tr/td[normalize-space(text())='E-post'] ">
				<email>
					<xsl:apply-templates select="../following-sibling::*[1]"/>
				</email>
			</xsl:for-each>

			<xsl:for-each select="table/tr/td/table/tr/td[normalize-space(text())='Mobiltelefon'] ">
				<phone>
					<xsl:apply-templates select="../following-sibling::*[1]"/>
				</phone>
			</xsl:for-each>	
			
			<xsl:for-each select="table/tr/td/table/tr/td[normalize-space(text())='Kontor'] ">
				<office>
					<xsl:apply-templates select="../following-sibling::*[1]"/>
				</office>
			</xsl:for-each>	
			
			<xsl:for-each select="table/tr/td/img">
				<image>
					<xsl:value-of select="@src" />
				</image>
			</xsl:for-each>
			
		</person>
			
	</xsl:template>

	<xsl:template match="tr">
		<xsl:value-of select="td" />
	</xsl:template>

</xsl:stylesheet>