package io.intino.alexandria.xml;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Node {

	private org.w3c.dom.Node node;

	public Node(org.w3c.dom.Node node) {
		this.node = node;
	}

	public org.w3c.dom.Node get() {
		return node;
	}

	public String getNodeName() {
		return node.getNodeName();
	}

	public String getNodeValue() throws DOMException {
		return node.getNodeValue();
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		node.setNodeValue(nodeValue);
	}

	public short getNodeType() {
		return node.getNodeType();
	}

	public Node getParentNode() {
		return nodeOf(node.getParentNode());
	}

	public List<Node> getChildNodes() {
		return toList(node.getChildNodes());
	}

	public List<Node> getChildren() {
		return getChildNodes().stream().filter(n -> !(n.get() instanceof Text)).collect(Collectors.toList());
	}

	public Node getFirstChild() {
		return getChildNodes().get(0);
	}

	public Node getLastChild() {
		List<Node> childNodes = getChildNodes();
		return childNodes.get(childNodes.size() - 1);
	}

	public Node getPreviousSibling() {
		return nodeOf(node.getPreviousSibling());
	}

	public Node getNextSibling() {
		return nodeOf(node.getNextSibling());
	}

	public NamedNodeMap getAttributes() {
		return node.getAttributes();
	}

	public Document getOwnerDocument() {
		return node.getOwnerDocument();
	}

	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return nodeOf(node.insertBefore(newChild.get(), refChild.get()));
	}

	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return nodeOf(node.replaceChild(newChild.get(), oldChild.get()));
	}

	public Node removeChild(Node oldChild) throws DOMException {
		return nodeOf(node.removeChild(oldChild.get()));
	}

	public Node appendChild(Node newChild) throws DOMException {
		return nodeOf(node.appendChild(newChild.get()));
	}

	public boolean hasChildNodes() {
		return node.hasChildNodes();
	}

	public Node cloneNode(boolean deep) {
		return nodeOf(node.cloneNode(deep));
	}

	public void normalize() {
		node.normalize();
	}

	public boolean isSupported(String feature, String version) {
		return node.isSupported(feature, version);
	}

	public String getNamespaceURI() {
		return node.getNamespaceURI();
	}

	public String getPrefix() {
		return node.getPrefix();
	}

	public void setPrefix(String prefix) throws DOMException {
		node.setPrefix(prefix);
	}

	public String getLocalName() {
		return node.getLocalName();
	}

	public boolean hasAttributes() {
		return node.hasAttributes();
	}

	public String getBaseURI() {
		return node.getBaseURI();
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		return node.compareDocumentPosition(other.get());
	}

	public String getTextContent() throws DOMException {
		return node.getTextContent();
	}

	public void setTextContent(String textContent) throws DOMException {
		node.setTextContent(textContent);
	}

	public boolean isSameNode(org.w3c.dom.Node other) {
		return node.isSameNode(other);
	}

	public String lookupPrefix(String namespaceURI) {
		return node.lookupPrefix(namespaceURI);
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		return node.isDefaultNamespace(namespaceURI);
	}

	public String lookupNamespaceURI(String prefix) {
		return node.lookupNamespaceURI(prefix);
	}

	public boolean isEqualNode(org.w3c.dom.Node arg) {
		return node.isEqualNode(arg);
	}

	public Object getFeature(String feature, String version) {
		return node.getFeature(feature, version);
	}

	public Object setUserData(String key, Object data, UserDataHandler handler) {
		return node.setUserData(key, data, handler);
	}

	public Object getUserData(String key) {
		return node.getUserData(key);
	}

	public Node child(String type) {
		return getChildNodes().stream().filter(child -> child.getNodeName().equalsIgnoreCase(type)).findFirst().orElse(null);
	}

	public List<Node> children(String type) {
		return getChildNodes().stream().filter(child -> child.getNodeName().equalsIgnoreCase(type)).collect(Collectors.toList());
	}

	private List<Node> toList(NodeList nodeList) {
		return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item).map(Node::new).collect(Collectors.toList());
	}

	private Node nodeOf(org.w3c.dom.Node importNode) {
		return new Node(importNode);
	}
}
