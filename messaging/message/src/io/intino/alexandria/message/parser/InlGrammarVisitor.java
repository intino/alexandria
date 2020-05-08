// Generated from /Users/oroncal/workspace/alexandria/messaging/message/src/io/intino/alexandria/message/parser/InlGrammar.g4 by ANTLR 4.8
package io.intino.alexandria.message.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link InlGrammar}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface InlGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link InlGrammar#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(InlGrammar.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessage(InlGrammar.MessageContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(InlGrammar.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(InlGrammar.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(InlGrammar.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(InlGrammar.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link InlGrammar#hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchy(InlGrammar.HierarchyContext ctx);
}