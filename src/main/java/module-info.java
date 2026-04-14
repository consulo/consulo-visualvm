/**
 * @author VISTALL
 * @since 2024-04-27
 */
open module consulo.visualvm {
	// platform
	requires consulo.execution.api;
	requires consulo.configurable.api;
	requires consulo.project.ui.api;
	requires consulo.ui.ex.api;
	requires consulo.file.editor.api;

	// java
	requires consulo.java.language.api;
	requires consulo.java.execution.api;
	requires consulo.java.execution.impl;
	requires consulo.java.debugger.impl;

	// TODO remove in future
	requires java.desktop;
	requires forms.rt;
}