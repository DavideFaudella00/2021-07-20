package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event> {
	public enum EventType {
		DA_INTERVISTARE, FERIE
	}

	private int giorno;
	private EventType tipo;
	private User intervistate;
	private Giornalista giornalista;

	public Event(int giorno, EventType tipo, User intervistate, Giornalista giornalista) {
		super();
		this.giorno = giorno;
		this.tipo = tipo;
		this.intervistate = intervistate;
		this.giornalista = giornalista;
	}

	public int getGiorno() {
		return giorno;
	}

	public User getIntervistate() {
		return intervistate;
	}

	public Giornalista getGiornalista() {
		return giornalista;
	}

	public EventType getTipo() {
		return tipo;
	}

	@Override
	public int compareTo(Event other) {
		return this.giorno - other.giorno;
	}

}
