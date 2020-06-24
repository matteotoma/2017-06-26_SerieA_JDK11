package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SquadraTifo;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

//controller turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private ChoiceBox<Season> boxStagione;

    @FXML
    private Button btnCalcolaConnessioniSquadra;

    @FXML
    private Button btnSimulaTifosi;

    @FXML
    private Button btnAnalizzaSquadre;

    @FXML
    private TextArea txtResult;

    @FXML
    void doAnalizzaSquadre(ActionEvent event) {
    	txtResult.clear();
    	model.creaGrafo();
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi!\n", model.Nvertici(), model.Narchi()));
    	this.boxSquadra.getItems().addAll(model.getVertici());
    	this.boxStagione.getItems().addAll(model.getSeasons());
    }

    @FXML
    void doCalcolaConnessioniSquadra(ActionEvent event) {
    	txtResult.clear();
    	Team t = this.boxSquadra.getValue();
    	if(t == null) {
    		txtResult.appendText("Devi selezionare una squadra!\n");
    		return;
    	}
    	txtResult.appendText(String.format("La squadra %s ha giocato con: \n", t.getTeam()));
    	for(Adiacenza a: model.getVicini(t))
    		txtResult.appendText(String.format("%s %d\n", a.getT2(), a.getPeso()));
    }

    @FXML
    void doSimulaTifosi(ActionEvent event) {
    	txtResult.clear();
    	Season s = this.boxStagione.getValue();
    	if(s == null) {
    		txtResult.appendText("Devi selezionare una stagione!\n");
    		return;
    	}
    	model.simula(s);
    	for(SquadraTifo s1: model.getSquadre())
    		txtResult.appendText(String.format("%s Punti: %d Tifosi: %d\n", s1.getTeam().getTeam(), s1.getPunti(), s1.getTifosi()));
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxStagione != null : "fx:id=\"boxStagione\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnCalcolaConnessioniSquadra != null : "fx:id=\"btnCalcolaConnessioniSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSimulaTifosi != null : "fx:id=\"btnSimulaTifosi\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnAnalizzaSquadre != null : "fx:id=\"btnAnalizzaSquadre\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}