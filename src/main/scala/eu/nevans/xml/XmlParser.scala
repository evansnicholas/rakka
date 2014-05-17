package eu.nevans.xml	

import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import javax.xml.transform.stream.StreamResult
import javax.xml.XMLConstants
import java.io.File

class XmlParser {

  def goParse() = {
    val schemaFile = new File(getClass().getResource("/schema.xsd").toURI())
    val xmlFile = new File(getClass().getResource("/note.xml").toURI())
    val schemaDocuments = new StreamSource(schemaFile)
    val instanceDocument: Source = new StreamSource(xmlFile)

    val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val s: Schema = sf.newSchema(schemaDocuments)
    val v: Validator = s.newValidator()
    v.validate(instanceDocument)

  }
  
}