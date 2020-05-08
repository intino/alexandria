// Generated from /Users/oroncal/workspace/alexandria/messaging/message/src/io/intino/alexandria/message/parser/InlGrammar.g4 by ANTLR 4.8
package io.intino.alexandria.message.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link InlGrammar}.
 */
public interface InlGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link InlGrammar#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(InlGrammar.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(InlGrammar.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#message}.
	 * @param ctx the parse tree
	 */
	void enterMessage(InlGrammar.MessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#message}.
	 * @param ctx the parse tree
	 */
	void exitMessage(InlGrammar.MessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(InlGrammar.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(InlGrammar.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#type}.
	 * @param ctx the parse tree
	 */
	void enterType(InlGrammar.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#type}.
	 * @param ctx the parse tree
	 */
	void exitType(InlGrammar.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(InlGrammar.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(InlGrammar.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(InlGrammar.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(InlGrammar.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link InlGrammar#hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterHierarchy(InlGrammar.HierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link InlGrammar#hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitHierarchy(InlGrammar.HierarchyContext ctx);
}