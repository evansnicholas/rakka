package eu.nevans.xml

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.SAXParseException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import org.xml.sax.EntityResolver
import java.io.File
import org.xml.sax.InputSource
import org.apache.xerces.xs.ElementPSVI
import org.apache.xerces.xs.XSModel
import org.apache.xerces.xs.XSNamedMap
import org.apache.xerces.xs.XSConstants

class InstanceParser {

  def goParse() = {
    val errorHandler = new DefaultHandler {

      override def error(exception: SAXParseException) = {
        println(exception)
      }
    }
    
    val entityResolver = new DefaultHandler {
      
      override def resolveEntity(publicId: String, systemId: String) = {
        val rootDir = new File(getClass().getResource("/taxo").toURI())
        val pathFromRoot = systemId.stripPrefix("file:/home/Nick/mygit/rakka/target/classes")
        println(s"publicId: ${publicId}")
        println(s"systemId: ${systemId}")
        val resolvedFile = new File(rootDir, s"$pathFromRoot")
        println(pathFromRoot)
        //val file = new File(rootDir)
        new InputSource(s".${pathFromRoot}")
      }
    }

    val dbFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

    dbFactory.setAttribute("http://apache.org/xml/features/validation/schema",
      java.lang.Boolean.TRUE);
    dbFactory.setNamespaceAware(true)

    dbFactory.setAttribute("http://apache.org/xml/properties/dom/document-class-name",
      "org.apache.xerces.dom.PSVIDocumentImpl");
    dbFactory.setValidating(true)
    val rootString = "/home/Nick/mygit/rakka/src/main/resources/taxo"
    val instance = "http://www.xbrl.org/2003/instance ./taxo/www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"
    val linkbase = "http://www.xbrl.org/2003/linkbase ./taxo/www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd"
    val xl = "http://www.xbrl.org/2003/XLink ./taxo/www.xbrl.org/2003/xl-2003-12-31.xsd"
    val xlink = "http://www.w3.org/1999/xlink ./taxo/www.xbrl.org/2003/xlink-2003-12-31.xsd"
    val kvk_i = s"http://www.nltaxonomie.nl/8.0/basis/kvk/items/kvk-data ${rootString}/www.nltaxonomie.nl/8.0/basis/kvk/items/kvk-data.xsd"
    val kvk_t = s"http://www.nltaxonomie.nl/8.0/domein/kvk/tuples/kvk-tuples ${rootString}/www.nltaxonomie.nl/8.0/domein/kvk/tuples/kvk-tuples.xsd"
    val rj_i = s"http://www.nltaxonomie.nl/8.0/basis/rj/items/rj-data ${rootString}/www.nltaxonomie.nl/8.0/basis/rj/items/rj-data.xsd"
    
    dbFactory.setAttribute("http://apache.org/xml/properties/schema/external-schemaLocation", 
              s"$instance $kvk_i $kvk_t $rj_i")

    val dBuilder: DocumentBuilder = dbFactory.newDocumentBuilder()

    dBuilder.setErrorHandler(errorHandler)
    //dBuilder.setEntityResolver(entityResolver)
    
    val xmlFile = new File(getClass().getResource("/sampleInstance1.xml").toURI())
    val doc = dBuilder.parse(xmlFile);
    
    val element = doc.getDocumentElement()
    val rootPSVI = element.asInstanceOf[ElementPSVI]    
    val schema: XSModel = rootPSVI.getSchemaInformation();
    println(s"xbrl elem type: ${rootPSVI.getTypeDefinition().getName()}")
    val child = element.getChildNodes().item(151)
    println(child.getNodeName())
    val childPSVI = child.asInstanceOf[ElementPSVI]
    println(childPSVI.getTypeDefinition().getName())
    println(childPSVI.getElementDeclaration().getSubstitutionGroupAffiliation())
    println(childPSVI.getTypeDefinition().derivedFrom("http://www.w3.org/2001/XMLSchema", "string", XSConstants.DERIVATION_EXTENSION))
  }
  
  
}