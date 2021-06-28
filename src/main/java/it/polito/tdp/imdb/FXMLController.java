/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {

    	txtResult.clear();
    	/*
    	if(this.model.grafo==null) { //cioè se il grafo non è ancora stato creato
    		txtResult.appendText("Creare prima il grafico!");
    		return;
    	}
    	*/
    	Actor attore = this.boxAttore.getValue();
    	List <Actor> raggiungibili = new ArrayList<>();
    	for(Actor a : this.model.attoriRaggiungibili(attore)) {
    		raggiungibili.add(a);
    		//txtResult.appendText(""+a+"\n");
    	}
    	
    	Collections.sort(raggiungibili, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				
				return o1.getLastName().compareTo(o2.getLastName());
			}
    		
    	});
    	
    	for(Actor aaa: raggiungibili) {
    		if(!aaa.equals(attore)) {
    			txtResult.appendText(aaa+"\n");
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	this.boxAttore.getItems().clear();
    	if(this.boxGenere.getValue()==null) {
    		txtResult.appendText("Selezionare un genere!");
    		return;
    	}
    	
    	this.model.creaGrafo(this.boxGenere.getValue());
    	
    	txtResult.appendText("GRAFO CREATO!"+"\n");
    	txtResult.appendText("# vertici : "+this.model.nVertici()+"\n");
    	txtResult.appendText("# archi : "+this.model.nArchi()+"\n");
    	
      	this.boxAttore.getItems().addAll(model.getVertici());
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}
