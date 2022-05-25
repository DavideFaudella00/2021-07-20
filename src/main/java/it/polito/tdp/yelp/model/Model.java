package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {

	private YelpDao dao;
	private Graph<User, DefaultWeightedEdge> grafo;
	private List<User> utenti;

	public Model() {
		dao = new YelpDao();
	}

	public String creaGrafo(int minRevisioni, int anno) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		utenti = dao.getUsersWithReviews(minRevisioni);
		Graphs.addAllVertices(grafo, utenti);

		for (User u1 : utenti) {
			for (User u2 : utenti) {
				if (!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId()) < 0) {
					int sim = dao.calcolaSimilarita(u1, u2, anno);
					if (sim > 0) {
						Graphs.addEdge(grafo, u1, u2, sim);
					}
				}
			}
		}
		return "Grafo creato con " + grafo.vertexSet().size() + " vertici e " + grafo.edgeSet().size() + " archi";
	}

	public List<User> utentiPiuSimili(User u) {
		int max = 0 ;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)>max) {
				max = (int)this.grafo.getEdgeWeight(e);
			}
		}
		
		List<User> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if((int)this.grafo.getEdgeWeight(e) == max) {
				User u2 = Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(u2);
			}
		}
		return result ;
	}

	public List<User> getusers() {
		return this.utenti;
	}
}
