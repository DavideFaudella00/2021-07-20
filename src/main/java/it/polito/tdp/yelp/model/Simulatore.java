package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;
import javafx.print.Collation;

public class Simulatore {
	private int x1;
	private int x2;
	private List<Giornalista> giornalisti;
	private int numeroGiorni;

	private Set<User> intervistati;

	private Graph<User, DefaultWeightedEdge> grafo;

	private PriorityQueue<Event> queue;

	public Simulatore(Graph<User, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;

	}

	public void init(int x1, int x2) {
		this.x1 = x1;
		this.x2 = x2;
		intervistati = new HashSet<User>();
		numeroGiorni = 0;

		giornalisti = new ArrayList<>();
		for (int id = 0; id < this.x1; id++) {
			giornalisti.add(new Giornalista(id));
		}
		for (Giornalista g : giornalisti) {
			User intervistato = selezioneIntervistato(this.grafo.vertexSet());
			intervistati.add(intervistato);
			g.incrementeNumeroIntervistati();
			this.queue.add(new Event(1, EventType.DA_INTERVISTARE, intervistato, g));
		}
	}

	public void run() {
		while (!queue.isEmpty() && intervistati.size() < x2) {
			Event e = queue.poll();
			this.numeroGiorni = e.getGiorno();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getTipo()) {
		case DA_INTERVISTARE:
			double caso = Math.random();
			
			if (caso < 0.6) {
				User vicino = selezionaAdiacente(e.getIntervistate());
				if (vicino == null) {
					vicino = selezioneIntervistato(this.grafo.vertexSet());
				}
				this.queue.add(new Event(e.getGiorno() + 1, EventType.DA_INTERVISTARE, vicino, e.getGiornalista()));
				this.intervistati.add(vicino);
				e.getGiornalista().incrementeNumeroIntervistati();
			} else if (caso < 0.8) {
				this.queue.add(new Event(e.getGiorno() + 1, EventType.FERIE, e.getIntervistate(), e.getGiornalista()));
			} else {
				this.queue.add(new Event(e.getGiorno() + 1, EventType.DA_INTERVISTARE, e.getIntervistate(),
						e.getGiornalista()));
			}
			break;
		case FERIE:
			break;
		default:
			break;

		}

	}

	private User selezionaAdiacente(User u) {
		List<User> vicini = Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.intervistati);
		if (vicini.size() == 0) {
			return null;
		}
		double max = 0;
		for (User v : vicini) {
			double peso = grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if (peso > max) {
				max = peso;
			}
		}
		List<User> migliori = new ArrayList<>();
		for (User v : vicini) {
			double peso = grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if (peso == max) {
				migliori.add(v);
			}
		}
		int scelto = (int) (Math.random() * migliori.size());
		return (new ArrayList<User>(migliori)).get(scelto);
	}

	private User selezioneIntervistato(Collection<User> lista) {
		Set<User> candidati = new HashSet<User>(lista);
		candidati.removeAll(this.intervistati);
		int scelto = (int) (Math.random() * candidati.size());
		return (new ArrayList<User>(candidati)).get(scelto);
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public int getNumeroGiorni() {
		return numeroGiorni;
	}

}
