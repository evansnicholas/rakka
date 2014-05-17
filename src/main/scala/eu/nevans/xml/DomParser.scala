package eu.nevans.xml

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import org.w3c.dom.Document
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator
import javax.xml.XMLConstants
import javax.xml.transform.dom.DOMSource
import org.apache.xerces.xs.ElementPSVI
import org.apache.xerces.xs.XSModel
import org.apache.xerces.xs.XSNamedMap
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.SAXParseException
import org.apache.xerces.parsers.DOMParser
import org.xml.sax.InputSource
import org.apache.xerces.xs.XSConstants
import org.apache.xerces.xs.XSTypeDefinition
import java.io.File

class DomParser {

  def goParse() = {
    //Set the correct document implementation
    //System.setProperty("http://apache.org/xml/properties/dom/document-class-name",
      //"org.apache.xerces.dom.PSVIDocumentImpl")
      
    val errorHandler = new DefaultHandler {
      override def error(exception: SAXParseException ) = {
        println(exception)
      }
    }
    
    val entityResolver = new DefaultHandler {
      
      override def resolveEntity(publicId: String, systemId: String) = {
        println(s"publicId: ${publicId}")
        println(s"systemId: ${systemId}")
        new InputSource("schema.xsd")
      }
    }

    val xmlFile = new File(getClass().getResource("/note.xml").toURI())
    //val xmlFile = new File(getClass().getResource("/simple.xml").toURI())
    val xmlSource = new InputSource(xmlFile.toURI().toString())
    val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    
    //Load schemas
    val schemaFile = new File(getClass().getResource("/schema.xsd").toURI())
    //val schemaFile = new File(getClass().getResource("/simple.xsd").toURI())
    val schemaDocuments = new StreamSource(schemaFile)
    val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val s: Schema = sf.newSchema(schemaDocuments)
    
    //val v: Validator = s.newValidator()
    //v.validate(new DOMSource(doc))
    
    //Set the schemas
    dbFactory.setAttribute("http://apache.org/xml/features/validation/schema", 
    java.lang.Boolean.TRUE);
    dbFactory.setNamespaceAware(true)
    dbFactory.setSchema(s)
    dbFactory.setAttribute("http://apache.org/xml/properties/dom/document-class-name", 
    "org.apache.xerces.dom.PSVIDocumentImpl");
    dbFactory.setValidating(true)
    dbFactory.setAttribute("http://apache.org/xml/properties/schema/external-schemaLocation", "nick schema.xsd")
    
    //val parser = new DOMParser()
    //parser.setErrorHandler(errorHandler)
    
    //Parse
    val dBuilder: DocumentBuilder = dbFactory.newDocumentBuilder()
    
    dBuilder.setErrorHandler(errorHandler)
    dBuilder.setEntityResolver(entityResolver)
    val doc = dBuilder.parse(xmlFile);
    //parser.parse(xmlSource)
    //val doc = parser.getDocument()
    val element = doc.getDocumentElement()
    val rootPSVI = element.asInstanceOf[ElementPSVI]    
    val schema: XSModel = rootPSVI.getSchemaInformation();
    println(schema.getNamespaces()) 
    val elementDeclarations: XSNamedMap = schema.getComponents(XSConstants.ELEMENT_DECLARATION)
    val typeDef = rootPSVI.getTypeDefinition()
    println(s"root namespace: ${rootPSVI.getElementDeclaration().getNamespace()}")
    println(typeDef.getName())
    println(typeDef.getAnonymous())
    val child = element.getChildNodes().item(1)
    val childPSVI = child.asInstanceOf[ElementPSVI]
    val childType = childPSVI.getTypeDefinition()
    println(s"child: ${childPSVI.getElementDeclaration().getNamespace()}")
    //println(childType.getName())
    //println(childType.derivedFromType(childType, 1))
    //println(childType.derivedFrom("http://www.w3.org/2001/XMLSchema", "string", XSConstants.DERIVATION_EXTENSION))
    
    
    
    
  }
}