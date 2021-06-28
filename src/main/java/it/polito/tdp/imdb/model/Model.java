package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

private SimpleWeightedGraph <Actor, DefaultWeightedEdge> grafo;
private ImdbDAO dao;
private Map <Integer, Actor> idMap;
Map <Actor , Actor> predecessore;

	public Model() {
		
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		dao.listAllActors(idMap);
		
		
	}
	
	public void creaGrafo(String genere) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//agg vertici
		Graphs.addAllVertices(grafo, dao.getVertici(genere, idMap));
		
		//agg archi
		for(Adiacenza a : dao.getAdiacenze(genere, idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(),a.getA2(), a.getPeso());
			
		}
	
		
	}
	
	public List <String> getGeneri(){
		return this.dao.getGeneri();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Set <Actor> getVertici(){
		return this.grafo.vertexSet();
	}
	
	/*
	 Alla pressione del bottone “Attori Simili”, si stampino, in ordine alfabetico
	 (campo last_name), tutti gli attori “collegati” ad a, ovvero tutti gli attori
	 che si possono raggiungere nel grafo, anche attraverso più passaggi, a partire da a.
	 */

	public List<Actor> attoriRaggiungibili(Actor a) {
	
		BreadthFirstIterator <Actor, DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo, a) ;
		
		this.predecessore = new HashMap<>() ;
		this.predecessore.put(a, null) ;
		
		bfv.addTraversalListener(new TraversalListener<Actor, DefaultWeightedEdge>() {

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				DefaultWeightedEdge arco = e.getEdge() ;
				Actor a = grafo.getEdgeSource(arco);
				Actor b = grafo.getEdgeTarget(arco);
				// ho scoperto 'a' arrivando da 'b' (se 'b' lo conoscevo) b->a
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					predecessore.put(a, b) ;

				} else if(predecessore.containsKey(a) && !predecessore.containsKey(b)) {
					// di sicuro conoscevo 'a' e quindi ho scoperto 'b'
					predecessore.put(b, a) ;

				}
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Actor> e) {
			}
		});
		
//		DepthFirstIterator<Fermata, DefaultEdge> dfv = 
//				new DepthFirstIterator<>(this.grafo, partenza) ;
		
		List<Actor> result = new ArrayList<>() ;
		
		while(bfv.hasNext()) {
			Actor act = bfv.next() ;
			result.add(act) ;
		}
		
		return result ;
	}
			
		}
	
	
