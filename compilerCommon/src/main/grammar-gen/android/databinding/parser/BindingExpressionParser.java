// Generated from BindingExpression.g4 by ANTLR 4.5
package android.databinding.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class BindingExpressionParser extends Parser {
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, THIS=44, VoidLiteral=45, 
		IntegerLiteral=46, FloatingPointLiteral=47, BooleanLiteral=48, CharacterLiteral=49, 
		SingleQuoteString=50, DoubleQuoteString=51, NullLiteral=52, Identifier=53, 
		WS=54, ResourceReference=55, PackageName=56, ResourceType=57;
	public static final int
		RULE_bindingSyntax = 0, RULE_defaults = 1, RULE_constantValue = 2, RULE_lambdaExpression = 3, 
		RULE_lambdaParameters = 4, RULE_inferredFormalParameterList = 5, RULE_expression = 6, 
		RULE_classExtraction = 7, RULE_expressionList = 8, RULE_literal = 9, RULE_identifier = 10, 
		RULE_javaLiteral = 11, RULE_stringLiteral = 12, RULE_explicitGenericInvocation = 13, 
		RULE_typeArguments = 14, RULE_type = 15, RULE_explicitGenericInvocationSuffix = 16, 
		RULE_arguments = 17, RULE_classOrInterfaceType = 18, RULE_primitiveType = 19, 
		RULE_resources = 20, RULE_resourceParameters = 21;
	public static final String[] ruleNames = {
		"bindingSyntax", "defaults", "constantValue", "lambdaExpression", "lambdaParameters", 
		"inferredFormalParameterList", "expression", "classExtraction", "expressionList", 
		"literal", "identifier", "javaLiteral", "stringLiteral", "explicitGenericInvocation", 
		"typeArguments", "type", "explicitGenericInvocationSuffix", "arguments", 
		"classOrInterfaceType", "primitiveType", "resources", "resourceParameters"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'default'", "'='", "'->'", "'('", "')'", "'.'", "'['", "']'", 
		"'+'", "'-'", "'~'", "'!'", "'*'", "'/'", "'%'", "'<<'", "'>>>'", "'>>'", 
		"'<='", "'>='", "'>'", "'<'", "'instanceof'", "'=='", "'!='", "'&'", "'^'", 
		"'|'", "'&&'", "'||'", "'?'", "':'", "'??'", "'class'", "'boolean'", "'char'", 
		"'byte'", "'short'", "'int'", "'long'", "'float'", "'double'", "'this'", 
		null, null, null, null, null, null, null, "'null'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, "THIS", "VoidLiteral", 
		"IntegerLiteral", "FloatingPointLiteral", "BooleanLiteral", "CharacterLiteral", 
		"SingleQuoteString", "DoubleQuoteString", "NullLiteral", "Identifier", 
		"WS", "ResourceReference", "PackageName", "ResourceType"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	@NotNull
	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "BindingExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	public BindingExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN);
	}
	public static class BindingSyntaxContext extends ParserRuleContext {
		public BindingSyntaxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bindingSyntax; }
	 
		public BindingSyntaxContext() { }
		public void copyFrom(BindingSyntaxContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class RootExprContext extends BindingSyntaxContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DefaultsContext defaults() {
			return getRuleContext(DefaultsContext.class,0);
		}
		public RootExprContext(BindingSyntaxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterRootExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitRootExpr(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitRootExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RootLambdaContext extends BindingSyntaxContext {
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public RootLambdaContext(BindingSyntaxContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterRootLambda(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitRootLambda(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitRootLambda(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final BindingSyntaxContext bindingSyntax() throws RecognitionException {
		BindingSyntaxContext _localctx = new BindingSyntaxContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_bindingSyntax);
		int _la;
		try {
			setState(49);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new RootExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				expression(0);
				setState(46);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(45);
					defaults();
					}
				}

				}
				break;

			case 2:
				_localctx = new RootLambdaContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				lambdaExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefaultsContext extends ParserRuleContext {
		public ConstantValueContext constantValue() {
			return getRuleContext(ConstantValueContext.class,0);
		}
		public DefaultsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaults; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterDefaults(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitDefaults(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitDefaults(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final DefaultsContext defaults() throws RecognitionException {
		DefaultsContext _localctx = new DefaultsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_defaults);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(T__0);
			setState(52);
			match(T__1);
			setState(53);
			match(T__2);
			setState(54);
			constantValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantValueContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode ResourceReference() { return getToken(BindingExpressionParser.ResourceReference, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ConstantValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterConstantValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitConstantValue(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitConstantValue(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ConstantValueContext constantValue() throws RecognitionException {
		ConstantValueContext _localctx = new ConstantValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_constantValue);
		try {
			setState(59);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BooleanLiteral:
			case CharacterLiteral:
			case SingleQuoteString:
			case DoubleQuoteString:
			case NullLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				literal();
				}
				break;
			case ResourceReference:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				match(ResourceReference);
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 3);
				{
				setState(58);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaExpressionContext extends ParserRuleContext {
		public LambdaParametersContext args;
		public ExpressionContext expr;
		public LambdaParametersContext lambdaParameters() {
			return getRuleContext(LambdaParametersContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitLambdaExpression(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitLambdaExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			_localctx.args = lambdaParameters();
			setState(62);
			match(T__3);
			setState(63);
			_localctx.expr = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaParametersContext extends ParserRuleContext {
		public LambdaParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaParameters; }
	 
		public LambdaParametersContext() { }
		public void copyFrom(LambdaParametersContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LambdaParameterListContext extends LambdaParametersContext {
		public InferredFormalParameterListContext params;
		public InferredFormalParameterListContext inferredFormalParameterList() {
			return getRuleContext(InferredFormalParameterListContext.class,0);
		}
		public LambdaParameterListContext(LambdaParametersContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterLambdaParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitLambdaParameterList(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitLambdaParameterList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SingleLambdaParameterContext extends LambdaParametersContext {
		public TerminalNode Identifier() { return getToken(BindingExpressionParser.Identifier, 0); }
		public SingleLambdaParameterContext(LambdaParametersContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterSingleLambdaParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitSingleLambdaParameter(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitSingleLambdaParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final LambdaParametersContext lambdaParameters() throws RecognitionException {
		LambdaParametersContext _localctx = new LambdaParametersContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_lambdaParameters);
		int _la;
		try {
			setState(71);
			switch (_input.LA(1)) {
			case Identifier:
				_localctx = new SingleLambdaParameterContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(65);
				match(Identifier);
				}
				break;
			case T__4:
				_localctx = new LambdaParameterListContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(66);
				match(T__4);
				setState(68);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(67);
					((LambdaParameterListContext)_localctx).params = inferredFormalParameterList();
					}
				}

				setState(70);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InferredFormalParameterListContext extends ParserRuleContext {
		public List<? extends TerminalNode> Identifier() { return getTokens(BindingExpressionParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(BindingExpressionParser.Identifier, i);
		}
		public InferredFormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inferredFormalParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterInferredFormalParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitInferredFormalParameterList(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitInferredFormalParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final InferredFormalParameterListContext inferredFormalParameterList() throws RecognitionException {
		InferredFormalParameterListContext _localctx = new InferredFormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_inferredFormalParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			match(Identifier);
			setState(78);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(74);
				match(T__0);
				setState(75);
				match(Identifier);
				}
				}
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BracketOpContext extends ExpressionContext {
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BracketOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterBracketOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitBracketOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitBracketOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ResourceContext extends ExpressionContext {
		public ResourcesContext resources() {
			return getRuleContext(ResourcesContext.class,0);
		}
		public ResourceContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitResource(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitResource(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CastOpContext extends ExpressionContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public CastOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterCastOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitCastOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitCastOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryOpContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterUnaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitUnaryOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitUnaryOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndOrOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AndOrOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterAndOrOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitAndOrOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitAndOrOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodInvocationContext extends ExpressionContext {
		public ExpressionContext target;
		public Token methodName;
		public ExpressionListContext args;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(BindingExpressionParser.Identifier, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MethodInvocationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterMethodInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitMethodInvocation(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitMethodInvocation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimaryContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode VoidLiteral() { return getToken(BindingExpressionParser.VoidLiteral, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ClassExtractionContext classExtraction() {
			return getRuleContext(ClassExtractionContext.class,0);
		}
		public PrimaryContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitPrimary(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupingContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GroupingContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterGrouping(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitGrouping(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitGrouping(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TernaryOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext iftrue;
		public ExpressionContext iffalse;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TernaryOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterTernaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitTernaryOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitTernaryOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparisonOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ComparisonOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterComparisonOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitComparisonOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitComparisonOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DotOpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(BindingExpressionParser.Identifier, 0); }
		public DotOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterDotOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitDotOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitDotOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MathOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MathOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterMathOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitMathOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitMathOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class QuestionQuestionOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public QuestionQuestionOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterQuestionQuestionOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitQuestionQuestionOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitQuestionQuestionOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitShiftOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BitShiftOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterBitShiftOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitBitShiftOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitBitShiftOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InstanceOfOpContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public InstanceOfOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterInstanceOfOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitInstanceOfOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitInstanceOfOp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryOpContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BinaryOpContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterBinaryOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitBinaryOp(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitBinaryOp(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				_localctx = new CastOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(82);
				match(T__4);
				setState(83);
				type();
				setState(84);
				match(T__5);
				setState(85);
				expression(16);
				}
				break;

			case 2:
				{
				_localctx = new UnaryOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(87);
				((UnaryOpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
					((UnaryOpContext)_localctx).op = _errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(88);
				expression(15);
				}
				break;

			case 3:
				{
				_localctx = new UnaryOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(89);
				((UnaryOpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==T__12) ) {
					((UnaryOpContext)_localctx).op = _errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(90);
				expression(14);
				}
				break;

			case 4:
				{
				_localctx = new GroupingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(91);
				match(T__4);
				setState(92);
				expression(0);
				setState(93);
				match(T__5);
				}
				break;

			case 5:
				{
				_localctx = new PrimaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(95);
				literal();
				}
				break;

			case 6:
				{
				_localctx = new PrimaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(96);
				match(VoidLiteral);
				}
				break;

			case 7:
				{
				_localctx = new PrimaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(97);
				identifier();
				}
				break;

			case 8:
				{
				_localctx = new PrimaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(98);
				classExtraction();
				}
				break;

			case 9:
				{
				_localctx = new ResourceContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(99);
				resources();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(162);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(160);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new MathOpContext(new ExpressionContext(_parentctx, _parentState));
						((MathOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(102);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(103);
						((MathOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__13) | (1L << T__14) | (1L << T__15))) != 0)) ) {
							((MathOpContext)_localctx).op = _errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(104);
						((MathOpContext)_localctx).right = expression(14);
						}
						break;

					case 2:
						{
						_localctx = new MathOpContext(new ExpressionContext(_parentctx, _parentState));
						((MathOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(105);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(106);
						((MathOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__10) ) {
							((MathOpContext)_localctx).op = _errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(107);
						((MathOpContext)_localctx).right = expression(13);
						}
						break;

					case 3:
						{
						_localctx = new BitShiftOpContext(new ExpressionContext(_parentctx, _parentState));
						((BitShiftOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(108);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(109);
						((BitShiftOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18))) != 0)) ) {
							((BitShiftOpContext)_localctx).op = _errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(110);
						((BitShiftOpContext)_localctx).right = expression(12);
						}
						break;

					case 4:
						{
						_localctx = new ComparisonOpContext(new ExpressionContext(_parentctx, _parentState));
						((ComparisonOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(111);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(112);
						((ComparisonOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22))) != 0)) ) {
							((ComparisonOpContext)_localctx).op = _errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(113);
						((ComparisonOpContext)_localctx).right = expression(11);
						}
						break;

					case 5:
						{
						_localctx = new ComparisonOpContext(new ExpressionContext(_parentctx, _parentState));
						((ComparisonOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(114);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(115);
						((ComparisonOpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__24 || _la==T__25) ) {
							((ComparisonOpContext)_localctx).op = _errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(116);
						((ComparisonOpContext)_localctx).right = expression(9);
						}
						break;

					case 6:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(117);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(118);
						((BinaryOpContext)_localctx).op = match(T__26);
						setState(119);
						((BinaryOpContext)_localctx).right = expression(8);
						}
						break;

					case 7:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(120);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(121);
						((BinaryOpContext)_localctx).op = match(T__27);
						setState(122);
						((BinaryOpContext)_localctx).right = expression(7);
						}
						break;

					case 8:
						{
						_localctx = new BinaryOpContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(123);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(124);
						((BinaryOpContext)_localctx).op = match(T__28);
						setState(125);
						((BinaryOpContext)_localctx).right = expression(6);
						}
						break;

					case 9:
						{
						_localctx = new AndOrOpContext(new ExpressionContext(_parentctx, _parentState));
						((AndOrOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(126);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(127);
						((AndOrOpContext)_localctx).op = match(T__29);
						setState(128);
						((AndOrOpContext)_localctx).right = expression(5);
						}
						break;

					case 10:
						{
						_localctx = new AndOrOpContext(new ExpressionContext(_parentctx, _parentState));
						((AndOrOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(129);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(130);
						((AndOrOpContext)_localctx).op = match(T__30);
						setState(131);
						((AndOrOpContext)_localctx).right = expression(4);
						}
						break;

					case 11:
						{
						_localctx = new TernaryOpContext(new ExpressionContext(_parentctx, _parentState));
						((TernaryOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(132);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(133);
						((TernaryOpContext)_localctx).op = match(T__31);
						setState(134);
						((TernaryOpContext)_localctx).iftrue = expression(0);
						setState(135);
						match(T__32);
						setState(136);
						((TernaryOpContext)_localctx).iffalse = expression(2);
						}
						break;

					case 12:
						{
						_localctx = new QuestionQuestionOpContext(new ExpressionContext(_parentctx, _parentState));
						((QuestionQuestionOpContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(138);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(139);
						((QuestionQuestionOpContext)_localctx).op = match(T__33);
						setState(140);
						((QuestionQuestionOpContext)_localctx).right = expression(2);
						}
						break;

					case 13:
						{
						_localctx = new DotOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(141);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(142);
						match(T__6);
						setState(143);
						match(Identifier);
						}
						break;

					case 14:
						{
						_localctx = new BracketOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(144);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(145);
						match(T__7);
						setState(146);
						expression(0);
						setState(147);
						match(T__8);
						}
						break;

					case 15:
						{
						_localctx = new MethodInvocationContext(new ExpressionContext(_parentctx, _parentState));
						((MethodInvocationContext)_localctx).target = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(149);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(150);
						match(T__6);
						setState(151);
						((MethodInvocationContext)_localctx).methodName = match(Identifier);
						setState(152);
						match(T__4);
						setState(154);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << VoidLiteral) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << SingleQuoteString) | (1L << DoubleQuoteString) | (1L << NullLiteral) | (1L << Identifier) | (1L << ResourceReference))) != 0)) {
							{
							setState(153);
							((MethodInvocationContext)_localctx).args = expressionList();
							}
						}

						setState(156);
						match(T__5);
						}
						break;

					case 16:
						{
						_localctx = new InstanceOfOpContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(157);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(158);
						match(T__23);
						setState(159);
						type();
						}
						break;
					}
					} 
				}
				setState(164);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ClassExtractionContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ClassExtractionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classExtraction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterClassExtraction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitClassExtraction(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitClassExtraction(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ClassExtractionContext classExtraction() throws RecognitionException {
		ClassExtractionContext _localctx = new ClassExtractionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_classExtraction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			type();
			setState(166);
			match(T__6);
			setState(167);
			match(T__34);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<? extends ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitExpressionList(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			expression(0);
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(170);
				match(T__0);
				setState(171);
				expression(0);
				}
				}
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public JavaLiteralContext javaLiteral() {
			return getRuleContext(JavaLiteralContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitLiteral(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_literal);
		try {
			setState(179);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BooleanLiteral:
			case CharacterLiteral:
			case NullLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(177);
				javaLiteral();
				}
				break;
			case SingleQuoteString:
			case DoubleQuoteString:
				enterOuterAlt(_localctx, 2);
				{
				setState(178);
				stringLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(BindingExpressionParser.Identifier, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitIdentifier(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaLiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(BindingExpressionParser.IntegerLiteral, 0); }
		public TerminalNode FloatingPointLiteral() { return getToken(BindingExpressionParser.FloatingPointLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(BindingExpressionParser.BooleanLiteral, 0); }
		public TerminalNode NullLiteral() { return getToken(BindingExpressionParser.NullLiteral, 0); }
		public TerminalNode CharacterLiteral() { return getToken(BindingExpressionParser.CharacterLiteral, 0); }
		public JavaLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterJavaLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitJavaLiteral(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitJavaLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final JavaLiteralContext javaLiteral() throws RecognitionException {
		JavaLiteralContext _localctx = new JavaLiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_javaLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << NullLiteral))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode SingleQuoteString() { return getToken(BindingExpressionParser.SingleQuoteString, 0); }
		public TerminalNode DoubleQuoteString() { return getToken(BindingExpressionParser.DoubleQuoteString, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitStringLiteral(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stringLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			_la = _input.LA(1);
			if ( !(_la==SingleQuoteString || _la==DoubleQuoteString) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationContext extends ParserRuleContext {
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterExplicitGenericInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitExplicitGenericInvocation(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitExplicitGenericInvocation(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ExplicitGenericInvocationContext explicitGenericInvocation() throws RecognitionException {
		ExplicitGenericInvocationContext _localctx = new ExplicitGenericInvocationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_explicitGenericInvocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			typeArguments();
			setState(188);
			explicitGenericInvocationSuffix();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsContext extends ParserRuleContext {
		public List<? extends TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitTypeArguments(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitTypeArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(T__22);
			setState(191);
			type();
			setState(196);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(192);
				match(T__0);
				setState(193);
				type();
				}
				}
				setState(198);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(199);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitType(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_type);
		try {
			int _alt;
			setState(217);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(201);
				classOrInterfaceType();
				setState(206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(202);
						match(T__7);
						setState(203);
						match(T__8);
						}
						} 
					}
					setState(208);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;
			case T__35:
			case T__36:
			case T__37:
			case T__38:
			case T__39:
			case T__40:
			case T__41:
			case T__42:
				enterOuterAlt(_localctx, 2);
				{
				setState(209);
				primitiveType();
				setState(214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(210);
						match(T__7);
						setState(211);
						match(T__8);
						}
						} 
					}
					setState(216);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationSuffixContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(BindingExpressionParser.Identifier, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocationSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterExplicitGenericInvocationSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitExplicitGenericInvocationSuffix(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitExplicitGenericInvocationSuffix(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws RecognitionException {
		ExplicitGenericInvocationSuffixContext _localctx = new ExplicitGenericInvocationSuffixContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_explicitGenericInvocationSuffix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(Identifier);
			setState(220);
			arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitArguments(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(T__4);
			setState(224);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << VoidLiteral) | (1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BooleanLiteral) | (1L << CharacterLiteral) | (1L << SingleQuoteString) | (1L << DoubleQuoteString) | (1L << NullLiteral) | (1L << Identifier) | (1L << ResourceReference))) != 0)) {
				{
				setState(223);
				expressionList();
				}
			}

			setState(226);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<? extends TypeArgumentsContext> typeArguments() {
			return getRuleContexts(TypeArgumentsContext.class);
		}
		public TypeArgumentsContext typeArguments(int i) {
			return getRuleContext(TypeArgumentsContext.class,i);
		}
		public List<? extends TerminalNode> Identifier() { return getTokens(BindingExpressionParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(BindingExpressionParser.Identifier, i);
		}
		public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterClassOrInterfaceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitClassOrInterfaceType(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitClassOrInterfaceType(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
		ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_classOrInterfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			identifier();
			setState(230);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(229);
				typeArguments();
				}
				break;
			}
			setState(239);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(232);
					match(T__6);
					setState(233);
					match(Identifier);
					setState(235);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						setState(234);
						typeArguments();
						}
						break;
					}
					}
					} 
				}
				setState(241);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitPrimitiveType(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourcesContext extends ParserRuleContext {
		public TerminalNode ResourceReference() { return getToken(BindingExpressionParser.ResourceReference, 0); }
		public ResourceParametersContext resourceParameters() {
			return getRuleContext(ResourceParametersContext.class,0);
		}
		public ResourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterResources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitResources(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitResources(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ResourcesContext resources() throws RecognitionException {
		ResourcesContext _localctx = new ResourcesContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_resources);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(ResourceReference);
			setState(246);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(245);
				resourceParameters();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourceParametersContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ResourceParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).enterResourceParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BindingExpressionListener ) ((BindingExpressionListener)listener).exitResourceParameters(this);
		}
		@Override
		public <Result> Result accept(ParseTreeVisitor<? extends Result> visitor) {
			if ( visitor instanceof BindingExpressionVisitor<?> ) return ((BindingExpressionVisitor<? extends Result>)visitor).visitResourceParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	@RuleVersion(0)
	public final ResourceParametersContext resourceParameters() throws RecognitionException {
		ResourceParametersContext _localctx = new ResourceParametersContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_resourceParameters);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(T__4);
			setState(249);
			expressionList();
			setState(250);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);

		case 1:
			return precpred(_ctx, 12);

		case 2:
			return precpred(_ctx, 11);

		case 3:
			return precpred(_ctx, 10);

		case 4:
			return precpred(_ctx, 8);

		case 5:
			return precpred(_ctx, 7);

		case 6:
			return precpred(_ctx, 6);

		case 7:
			return precpred(_ctx, 5);

		case 8:
			return precpred(_ctx, 4);

		case 9:
			return precpred(_ctx, 3);

		case 10:
			return precpred(_ctx, 2);

		case 11:
			return precpred(_ctx, 1);

		case 12:
			return precpred(_ctx, 19);

		case 13:
			return precpred(_ctx, 18);

		case 14:
			return precpred(_ctx, 17);

		case 15:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uaf6f\u8320\u479d\ub75c\u4880\u1605\u191c\uab37\3;\u00ff\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\5\2\61\n\2"+
		"\3\2\5\2\64\n\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\6\5\6G\n\6\3\6\5\6J\n\6\3\7\3\7\3\7\7\7O\n\7\f\7\16\7R\13"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\5\bg\n\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\b\u009d\n\b\3\b\3\b\3\b\3\b\7\b\u00a3\n\b\f\b\16\b"+
		"\u00a6\13\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\7\n\u00af\n\n\f\n\16\n\u00b2\13"+
		"\n\3\13\3\13\5\13\u00b6\n\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\7\20\u00c5\n\20\f\20\16\20\u00c8\13\20\3\20\3\20"+
		"\3\21\3\21\3\21\7\21\u00cf\n\21\f\21\16\21\u00d2\13\21\3\21\3\21\3\21"+
		"\7\21\u00d7\n\21\f\21\16\21\u00da\13\21\5\21\u00dc\n\21\3\22\3\22\3\22"+
		"\3\23\3\23\5\23\u00e3\n\23\3\23\3\23\3\24\3\24\5\24\u00e9\n\24\3\24\3"+
		"\24\3\24\5\24\u00ee\n\24\7\24\u00f0\n\24\f\24\16\24\u00f3\13\24\3\25\3"+
		"\25\3\26\3\26\5\26\u00f9\n\26\3\27\3\27\3\27\3\27\3\27\2\2\3\16\30\2\2"+
		"\4\2\6\2\b\2\n\2\f\2\16\2\20\2\22\2\24\2\26\2\30\2\32\2\34\2\36\2 \2\""+
		"\2$\2&\2(\2*\2,\2\2\13\3\2\f\r\3\2\16\17\3\2\20\22\3\2\23\25\3\2\26\31"+
		"\3\2\33\34\4\2\60\63\66\66\3\2\64\65\3\2&-\u0113\2\63\3\2\2\2\4\65\3\2"+
		"\2\2\6=\3\2\2\2\b?\3\2\2\2\nI\3\2\2\2\fK\3\2\2\2\16f\3\2\2\2\20\u00a7"+
		"\3\2\2\2\22\u00ab\3\2\2\2\24\u00b5\3\2\2\2\26\u00b7\3\2\2\2\30\u00b9\3"+
		"\2\2\2\32\u00bb\3\2\2\2\34\u00bd\3\2\2\2\36\u00c0\3\2\2\2 \u00db\3\2\2"+
		"\2\"\u00dd\3\2\2\2$\u00e0\3\2\2\2&\u00e6\3\2\2\2(\u00f4\3\2\2\2*\u00f6"+
		"\3\2\2\2,\u00fa\3\2\2\2.\60\5\16\b\2/\61\5\4\3\2\60/\3\2\2\2\60\61\3\2"+
		"\2\2\61\64\3\2\2\2\62\64\5\b\5\2\63.\3\2\2\2\63\62\3\2\2\2\64\3\3\2\2"+
		"\2\65\66\7\3\2\2\66\67\7\4\2\2\678\7\5\2\289\5\6\4\29\5\3\2\2\2:>\5\24"+
		"\13\2;>\79\2\2<>\5\26\f\2=:\3\2\2\2=;\3\2\2\2=<\3\2\2\2>\7\3\2\2\2?@\5"+
		"\n\6\2@A\7\6\2\2AB\5\16\b\2B\t\3\2\2\2CJ\7\67\2\2DF\7\7\2\2EG\5\f\7\2"+
		"FE\3\2\2\2FG\3\2\2\2GH\3\2\2\2HJ\7\b\2\2IC\3\2\2\2ID\3\2\2\2J\13\3\2\2"+
		"\2KP\7\67\2\2LM\7\3\2\2MO\7\67\2\2NL\3\2\2\2OR\3\2\2\2PN\3\2\2\2PQ\3\2"+
		"\2\2Q\r\3\2\2\2RP\3\2\2\2ST\b\b\1\2TU\7\7\2\2UV\5 \21\2VW\7\b\2\2WX\5"+
		"\16\b\22Xg\3\2\2\2YZ\t\2\2\2Zg\5\16\b\21[\\\t\3\2\2\\g\5\16\b\20]^\7\7"+
		"\2\2^_\5\16\b\2_`\7\b\2\2`g\3\2\2\2ag\5\24\13\2bg\7/\2\2cg\5\26\f\2dg"+
		"\5\20\t\2eg\5*\26\2fS\3\2\2\2fY\3\2\2\2f[\3\2\2\2f]\3\2\2\2fa\3\2\2\2"+
		"fb\3\2\2\2fc\3\2\2\2fd\3\2\2\2fe\3\2\2\2g\u00a4\3\2\2\2hi\f\17\2\2ij\t"+
		"\4\2\2j\u00a3\5\16\b\20kl\f\16\2\2lm\t\2\2\2m\u00a3\5\16\b\17no\f\r\2"+
		"\2op\t\5\2\2p\u00a3\5\16\b\16qr\f\f\2\2rs\t\6\2\2s\u00a3\5\16\b\rtu\f"+
		"\n\2\2uv\t\7\2\2v\u00a3\5\16\b\13wx\f\t\2\2xy\7\35\2\2y\u00a3\5\16\b\n"+
		"z{\f\b\2\2{|\7\36\2\2|\u00a3\5\16\b\t}~\f\7\2\2~\177\7\37\2\2\177\u00a3"+
		"\5\16\b\b\u0080\u0081\f\6\2\2\u0081\u0082\7 \2\2\u0082\u00a3\5\16\b\7"+
		"\u0083\u0084\f\5\2\2\u0084\u0085\7!\2\2\u0085\u00a3\5\16\b\6\u0086\u0087"+
		"\f\4\2\2\u0087\u0088\7\"\2\2\u0088\u0089\5\16\b\2\u0089\u008a\7#\2\2\u008a"+
		"\u008b\5\16\b\4\u008b\u00a3\3\2\2\2\u008c\u008d\f\3\2\2\u008d\u008e\7"+
		"$\2\2\u008e\u00a3\5\16\b\4\u008f\u0090\f\25\2\2\u0090\u0091\7\t\2\2\u0091"+
		"\u00a3\7\67\2\2\u0092\u0093\f\24\2\2\u0093\u0094\7\n\2\2\u0094\u0095\5"+
		"\16\b\2\u0095\u0096\7\13\2\2\u0096\u00a3\3\2\2\2\u0097\u0098\f\23\2\2"+
		"\u0098\u0099\7\t\2\2\u0099\u009a\7\67\2\2\u009a\u009c\7\7\2\2\u009b\u009d"+
		"\5\22\n\2\u009c\u009b\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e\3\2\2\2"+
		"\u009e\u00a3\7\b\2\2\u009f\u00a0\f\13\2\2\u00a0\u00a1\7\32\2\2\u00a1\u00a3"+
		"\5 \21\2\u00a2h\3\2\2\2\u00a2k\3\2\2\2\u00a2n\3\2\2\2\u00a2q\3\2\2\2\u00a2"+
		"t\3\2\2\2\u00a2w\3\2\2\2\u00a2z\3\2\2\2\u00a2}\3\2\2\2\u00a2\u0080\3\2"+
		"\2\2\u00a2\u0083\3\2\2\2\u00a2\u0086\3\2\2\2\u00a2\u008c\3\2\2\2\u00a2"+
		"\u008f\3\2\2\2\u00a2\u0092\3\2\2\2\u00a2\u0097\3\2\2\2\u00a2\u009f\3\2"+
		"\2\2\u00a3\u00a6\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5"+
		"\17\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00a8\5 \21\2\u00a8\u00a9\7\t\2"+
		"\2\u00a9\u00aa\7%\2\2\u00aa\21\3\2\2\2\u00ab\u00b0\5\16\b\2\u00ac\u00ad"+
		"\7\3\2\2\u00ad\u00af\5\16\b\2\u00ae\u00ac\3\2\2\2\u00af\u00b2\3\2\2\2"+
		"\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\23\3\2\2\2\u00b2\u00b0"+
		"\3\2\2\2\u00b3\u00b6\5\30\r\2\u00b4\u00b6\5\32\16\2\u00b5\u00b3\3\2\2"+
		"\2\u00b5\u00b4\3\2\2\2\u00b6\25\3\2\2\2\u00b7\u00b8\7\67\2\2\u00b8\27"+
		"\3\2\2\2\u00b9\u00ba\t\b\2\2\u00ba\31\3\2\2\2\u00bb\u00bc\t\t\2\2\u00bc"+
		"\33\3\2\2\2\u00bd\u00be\5\36\20\2\u00be\u00bf\5\"\22\2\u00bf\35\3\2\2"+
		"\2\u00c0\u00c1\7\31\2\2\u00c1\u00c6\5 \21\2\u00c2\u00c3\7\3\2\2\u00c3"+
		"\u00c5\5 \21\2\u00c4\u00c2\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6\u00c4\3\2"+
		"\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00c9\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c9"+
		"\u00ca\7\30\2\2\u00ca\37\3\2\2\2\u00cb\u00d0\5&\24\2\u00cc\u00cd\7\n\2"+
		"\2\u00cd\u00cf\7\13\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0"+
		"\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00dc\3\2\2\2\u00d2\u00d0\3\2"+
		"\2\2\u00d3\u00d8\5(\25\2\u00d4\u00d5\7\n\2\2\u00d5\u00d7\7\13\2\2\u00d6"+
		"\u00d4\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2"+
		"\2\2\u00d9\u00dc\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00cb\3\2\2\2\u00db"+
		"\u00d3\3\2\2\2\u00dc!\3\2\2\2\u00dd\u00de\7\67\2\2\u00de\u00df\5$\23\2"+
		"\u00df#\3\2\2\2\u00e0\u00e2\7\7\2\2\u00e1\u00e3\5\22\n\2\u00e2\u00e1\3"+
		"\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5\7\b\2\2\u00e5"+
		"%\3\2\2\2\u00e6\u00e8\5\26\f\2\u00e7\u00e9\5\36\20\2\u00e8\u00e7\3\2\2"+
		"\2\u00e8\u00e9\3\2\2\2\u00e9\u00f1\3\2\2\2\u00ea\u00eb\7\t\2\2\u00eb\u00ed"+
		"\7\67\2\2\u00ec\u00ee\5\36\20\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2"+
		"\2\u00ee\u00f0\3\2\2\2\u00ef\u00ea\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef"+
		"\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\'\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4"+
		"\u00f5\t\n\2\2\u00f5)\3\2\2\2\u00f6\u00f8\79\2\2\u00f7\u00f9\5,\27\2\u00f8"+
		"\u00f7\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9+\3\2\2\2\u00fa\u00fb\7\7\2\2"+
		"\u00fb\u00fc\5\22\n\2\u00fc\u00fd\7\b\2\2\u00fd-\3\2\2\2\27\60\63=FIP"+
		"f\u009c\u00a2\u00a4\u00b0\u00b5\u00c6\u00d0\u00d8\u00db\u00e2\u00e8\u00ed"+
		"\u00f1\u00f8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
	}
}