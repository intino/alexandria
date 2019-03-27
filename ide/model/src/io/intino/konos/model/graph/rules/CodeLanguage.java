package io.intino.konos.model.graph.rules;

import io.intino.tara.lang.model.Rule;

public enum CodeLanguage implements Rule<Enum> {
	JavaScript, ABAP, ABC, ActionScript, ADA, ApacheConf, Apex, AsciiDoc, ASL, Assemblyx86, AutoHotkeyAutoIt, BatchFile, Bro, C, CPlusPlus, CSharp, C9SearchResults,
	Cirru, Clojure, Cobol, CoffeeScript, ColdFusion, Csound, CsoundDocument, CsoundScore, CSS, Curly, D, Dart, Diff, Django, Dockerfile, Dot, Drools,
	Edifact, Eiffel, EJS, Elixir, Elm, Erlang, Forth, Fortran, FreeMarker, FSharp, FSL, Gcode, Gherkin, Gitignore, Glsl, Go, Gobstones, GraphQLSchema,
	Groovy, HAML, Handlebars, Haskell, HaskellCabal, haXe, Hjson, HTML, HTMLElixir, HTMLRuby, INI, Io, Jack, Jade, Java, JSON, JSONiq, JSP, JSSM, JSX,
	Julia, Kotlin, LaTeX, LESS, Liquid, Lisp, LiveScript, LogiQL, LSL, Lua, LuaPage, Lucene, Makefile, Markdown, Mask, MATLAB, Maze, MEL, MIXAL, MUSHCode,
	MySQL, Nix, NSIS, ObjectiveC, OCaml, Pascal, Perl, Perl6, pgSQL, PHP, PHPBladeTemplate, Pig, PlainText, Powershell, Praat, Prolog, Properties,
	Protobuf, Puppet, Python, R, Razor, RDoc, Red, RHTML, RST, Ruby, Rust, SASS, SCAD, Scala, Scheme, SCSS, SH, SJS, Slim, Smarty, snippets, SoyTemplate,
	Space, SQL, SQLServer, Stylus, SVG, Swift, Tcl, Terraform, Tex, Text, Textile, Toml, TSX, Twig, Typescript, Vala, VBScript, Velocity, Verilog, VHDL,
	Visualforce, Wollok, XML, XQuery, YAML;

	@Override
	public boolean accept(Enum value) {
		return value instanceof CodeLanguage;
	}
}
