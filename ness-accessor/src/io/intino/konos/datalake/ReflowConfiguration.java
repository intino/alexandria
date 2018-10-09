package io.intino.konos.datalake;

import java.time.Instant;

public class ReflowConfiguration implements java.io.Serializable {

	private Boolean cleanStore = true;
	private Integer blockSize = 0;
	private java.util.List<Tank> tankList = new java.util.ArrayList<>();

	public Integer blockSize() {
		return this.blockSize;
	}

	public java.util.List<Tank> tankList() {
		return this.tankList;
	}

	public ReflowConfiguration blockSize(Integer blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public ReflowConfiguration tankList(java.util.List<Tank> tankList) {
		this.tankList = tankList;
		return this;
	}

	public ReflowConfiguration addTank(Tank tank) {
		this.tankList.add(tank);
		return this;
	}

	public Boolean cleanStore() {
		return this.cleanStore;
	}

	public ReflowConfiguration cleanStore(Boolean cleanStore) {
		this.cleanStore = cleanStore;
		return this;
	}

	public static class Tank implements java.io.Serializable {

		private String name = "";
		private java.time.Instant from = Instant.MIN;
		private java.time.Instant to = Instant.MAX;

		public String name() {
			return this.name;
		}

		public java.time.Instant from() {
			return this.from;
		}

		public Tank name(String name) {
			this.name = name;
			return this;
		}

		public Tank from(java.time.Instant from) {
			this.from = from;
			return this;
		}

		public Tank to(java.time.Instant from) {
			this.from = from;
			return this;
		}

	}
}