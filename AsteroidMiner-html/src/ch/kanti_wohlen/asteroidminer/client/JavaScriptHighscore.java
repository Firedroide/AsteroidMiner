package ch.kanti_wohlen.asteroidminer.client;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptHighscore extends JavaScriptObject {

	protected JavaScriptHighscore() {}

	public final native String getUserName() /*-{
		return this.user.name;
	}-*/;

	public final native String getUserID() /*-{
		return this.user.id;
	}-*/;

	public final native int getScore() /*-{
		return this.score;
	}-*/;

	public final native String getApplicationID() /*-{
		return this.application.id;
	}-*/;
}
