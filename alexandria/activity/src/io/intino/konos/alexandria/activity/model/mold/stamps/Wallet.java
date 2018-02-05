package io.intino.konos.alexandria.activity.model.mold.stamps;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
	private List<Card> cards = new ArrayList();

	public List<Card> items() {
		return this.cards;
	}

	public Wallet add(Card item) {
		cards.add(item);
		return this;
	}

	public static class Card {
		private String label;
		private List<String> lines = new ArrayList();

		public String label() {
			return this.label;
		}

		public Card label(String label) {
			this.label = label;
			return this;
		}

		public List<String> lines() {
			return this.lines;
		}

		public Card add(String line) {
			this.lines.add(line);
			return this;
		}
	}
}