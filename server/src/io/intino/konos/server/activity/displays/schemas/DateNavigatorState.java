package io.intino.konos.server.activity.displays.schemas;

public class DateNavigatorState implements java.io.Serializable {

	private Boolean playing = true;
	private Boolean canPlay = true;
	private Boolean canPrevious = true;
	private Boolean canNext = true;

	public Boolean playing() {
		return this.playing;
	}

	public Boolean canPlay() {
		return this.canPlay;
	}

	public Boolean canPrevious() {
		return this.canPrevious;
	}

	public Boolean canNext() {
		return this.canNext;
	}

	public DateNavigatorState playing(Boolean playing) {
		this.playing = playing;
		return this;
	}

	public DateNavigatorState canPlay(Boolean canPlay) {
		this.canPlay = canPlay;
		return this;
	}

	public DateNavigatorState canPrevious(Boolean canPrevious) {
		this.canPrevious = canPrevious;
		return this;
	}

	public DateNavigatorState canNext(Boolean canNext) {
		this.canNext = canNext;
		return this;
	}
}