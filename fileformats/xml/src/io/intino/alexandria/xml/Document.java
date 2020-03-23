package io.intino.alexandria.xml;

import org.w3c.dom.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Document {


	private org.w3c.dom.Document document;

	public Document(org.w3c.dom.Document document) {
		this.document = document;
	}

	public DocumentType getDoctype() {
		return document.getDoctype();
	}

	public DOMImplementation getImplementation() {
		return document.getImplementation();
	}

	public Element getDocumentElement() {
		return document.getDocumentElement();
	}

	public Element createElement(String tagName) throws DOMException {
		return document.createElement(tagName);
	}

	public DocumentFragment createDocumentFragment() {
		return document.createDocumentFragment();
	}

	public Text createTextNode(String data) {
		return document.createTextNode(data);
	}

	public Comment createComment(String data) {
		return document.createComment(data);
	}

	public CDATASection createCDATASection(String data) throws DOMException {
		return document.createCDATASection(data);
	}

	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		return document.createProcessingInstruction(target, data);
	}

	public Attr createAttribute(String name) throws DOMException {
		return document.createAttribute(name);
	}

	public EntityReference createEntityReference(String name) throws DOMException {
		return document.createEntityReference(name);
	}

	public List<Node> getElementsByTagName(String tagname) {
		return toList(document.getElementsByTagName(tagname));
	}

	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		return nodeOf(document.importNode(importedNode.get(), deep));
	}

	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return document.createElementNS(namespaceURI, qualifiedName);
	}

	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		return document.createAttributeNS(namespaceURI, qualifiedName);
	}

	public List<Node> getElementsByTagNameNS(String namespaceURI, String localName) {
		return toList(document.getElementsByTagNameNS(namespaceURI, localName));
	}

	public Element getElementById(String elementId) {
		return document.getElementById(elementId);
	}

	public String getInputEncoding() {
		return document.getInputEncoding();
	}

	public String getXmlEncoding() {
		return document.getXmlEncoding();
	}

	public boolean getXmlStandalone() {
		return document.getXmlStandalone();
	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		document.setXmlStandalone(xmlStandalone);
	}

	public String getXmlVersion() {
		return document.getXmlVersion();
	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		document.setXmlVersion(xmlVersion);
	}

	public boolean getStrictErrorChecking() {
		return document.getStrictErrorChecking();
	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		document.setStrictErrorChecking(strictErrorChecking);
	}

	public String getDocumentURI() {
		return document.getDocumentURI();
	}

	public void setDocumentURI(String documentURI) {
		document.setDocumentURI(documentURI);
	}

	public Node adoptNode(Node source) throws DOMException {
		return nodeOf(document.adoptNode(source.get()));
	}

	public DOMConfiguration getDomConfig() {
		return document.getDomConfig();
	}

	public void normalizeDocument() {
		document.normalizeDocument();
	}

	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		return nodeOf(document.renameNode(n.get(), namespaceURI, qualifiedName));
	}

	public String getNodeName() {
		return document.getNodeName();
	}

	public String getNodeValue() throws DOMException {
		return document.getNodeValue();
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		document.setNodeValue(nodeValue);
	}

	public short getNodeType() {
		return document.getNodeType();
	}

	public Node getParentNode() {
		return nodeOf(document.getParentNode());
	}

	public List<Node> getChildNodes() {
		return toList(document.getChildNodes());
	}

	public Node getFirstChild() {
		return nodeOf(document.getFirstChild());
	}

	public Node getLastChild() {
		return nodeOf(document.getLastChild());
	}

	public Node getPreviousSibling() {
		return nodeOf(document.getPreviousSibling());
	}

	public Node getNextSibling() {
		return nodeOf(document.getNextSibling());
	}

	public NamedNodeMap getAttributes() {
		return document.getAttributes();
	}

	public org.w3c.dom.Document getOwnerDocument() {
		return document.getOwnerDocument();
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return nodeOf(document.insertBefore(newChild.get(), refChild.get()));
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return nodeOf(document.replaceChild(newChild.get(), oldChild.get()));
	}

	public Node removeChild(Node oldChild) throws DOMException {
		return nodeOf(document.removeChild(oldChild.get()));
	}

	public Node appendChild(Node newChild) throws DOMException {
		return nodeOf(document.appendChild(newChild.get()));
	}

	public boolean hasChildNodes() {
		return document.hasChildNodes();
	}

	public Node cloneNode(boolean deep) {
		return nodeOf(document.cloneNode(deep));
	}

	public void normalize() {
		document.normalize();
	}

	public boolean isSupported(String feature, String version) {
		return document.isSupported(feature, version);
	}

	public String getNamespaceURI() {
		return document.getNamespaceURI();
	}

	public String getPrefix() {
		return document.getPrefix();
	}

	public void setPrefix(String prefix) throws DOMException {
		document.setPrefix(prefix);
	}

	public String getLocalName() {
		return document.getLocalName();
	}

	public boolean hasAttributes() {
		return document.hasAttributes();
	}

	public String getBaseURI() {
		return document.getBaseURI();
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		return document.compareDocumentPosition(other.get());
	}

	public String getTextContent() throws DOMException {
		return document.getTextContent();
	}

	public void setTextContent(String textContent) throws DOMException {
		document.setTextContent(textContent);
	}

	public boolean isSameNode(Node other) {
		return document.isSameNode(other.get());
	}

	public String lookupPrefix(String namespaceURI) {
		return document.lookupPrefix(namespaceURI);
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		return document.isDefaultNamespace(namespaceURI);
	}

	public String lookupNamespaceURI(String prefix) {
		return document.lookupNamespaceURI(prefix);
	}

	public boolean isEqualNode(Node arg) {
		return document.isEqualNode(arg.get());
	}

	public Object getFeature(String feature, String version) {
		return document.getFeature(feature, version);
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		return document.setUserData(key, data, handler);
	}

	public Object getUserData(String key) {
		return document.getUserData(key);
	}

	public Node child(String type) {
		return getChildNodes().stream().filter(child -> child.getNodeName().equalsIgnoreCase(type)).findFirst().orElse(null);
	}

	public List<Node> children(String type) {
		return getChildNodes().stream().filter(child -> child.getNodeName().equalsIgnoreCase(type)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return getTextContent();
	}

	private List<Node> toList(NodeList nodeList) {
		return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item).map(Node::new).collect(Collectors.toList());
	}

	private Node nodeOf(org.w3c.dom.Node importNode) {
		return new Node(importNode);
	}
}
