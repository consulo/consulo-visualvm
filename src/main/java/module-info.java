/**
 * @author VISTALL
 * @since 2024-04-27
 */
open module consulo.visualvm {
	requires consulo.ide.api;

	requires consulo.java.language.api;
	requires consulo.java.execution.api;
	requires consulo.java.execution.impl;
	requires consulo.java.debugger.impl;

	// TODO remove in future
	requires java.desktop;
	requires forms.rt;
}