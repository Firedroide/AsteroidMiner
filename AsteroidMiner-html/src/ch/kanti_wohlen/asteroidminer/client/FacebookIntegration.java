package ch.kanti_wohlen.asteroidminer.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;

public class FacebookIntegration {

	public static native void init() /*-{
		$doc.FB.init({
			appId : '425756014191466',
			xfbml : true,
			status : true,
			cookie : true,
		});
	}-*/;

	public static native void logIn() /*-{
		// Only log in if user is not logged in yet.
		$doc.FB.getLoginStatus(function logInIfNecessary(response) {
			if (response && response.status == 'connected') {
				$doc.connected = true;
				$doc.userID = response.authResponse.userID;
			} else {
				$doc.FB.login(function checkLoginStatus(response) {
					if (response && response.status == 'connected') {
						$doc.connected = true;
						$doc.userID = response.authResponse.userID;
					}
				}, {
					scope : 'publish_stream,friends_games_activity'
				});
			}
		});
	}-*/;

	public static native boolean isLoggedIn() /*-{
		return $doc.connected;
	}-*/;

	public static native void updateHighscore(int newScore) /*-{
		$doc.FB.api("/me/scores", "GET", function(currentScore) {
			var getUserScore = function(scores) {
				if (!(scores && scores.data)) {
					$doc.highscores = [];
					return 2147483647;
				} else {
					$doc.highscores = scores.data;
					for (var i = 0; i < scores.data.length; ++i) {
						var score = scores.data[i];
						//alert(JSON.stringify(score));
						if ((score.application.id == '425756014191466')
								&& (score.user.id == $doc.userID)) {
							score.user.name = 'Your previous highscore';
							return score.score;
						}
					}
				}
				return 0;
			}

			//alert(JSON.stringify(currentScore));
			var score = getUserScore(currentScore);
			//alert("Current highscore: " + score);
			$doc.highscores.push({
				user : {
					name : "This game",
					id : $doc.userID
				},
				score : newScore,
				application : {
					id : '425756014191466'
				}
			});
			if (newScore > score) {
				$doc.FB.api("/me/scores", "POST", {
					score : newScore
				}, function(response) {
					//alert(JSON.stringify(response));
					//alert("Updated score!");
				});
			}
		});
	}-*/;

	// TODO: Limit number of maximum highscores to 10
	public static List<JavaScriptHighscore> getHighscores() {
		final JsArray<JavaScriptHighscore> nativeHighscores = getFriendHighscores();
		final List<JavaScriptHighscore> highscores = new ArrayList<JavaScriptHighscore>(nativeHighscores.length());
		for (int i = 0; i < nativeHighscores.length(); ++i) {
			JavaScriptHighscore highscore = nativeHighscores.get(i);
			if (highscore.getApplicationID().equals("425756014191466")) {
				highscores.add(nativeHighscores.get(i));
			}
		}
		return highscores;
	}

	private static native JsArray<JavaScriptHighscore> getFriendHighscores() /*-{
		//alert("Getting cached highscores...");
		return $doc.highscores;
	}-*/;

	public static native void postScoreMessage(int score) /*-{
		// TODO: make not todo anymore.
	}-*/;

	public static native void postFriendScoreBeaten() /*-{
		// TODO: also todo...
	}-*/;
}
