package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

public class CT_Table implements Initializable {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private File f;

	public final class CSVData extends TableView<String> {

		public CSVData(String delimiter, File file) throws IOException {
			// Get CSV file lines as List
			List<String> lines = Files.readAllLines(Paths.get(file.toURI()));

			// Get the header row
			String[] firstRow = lines.get(0).split(delimiter);

			// For each header/column, create TableColumn
			for (String columnName : firstRow) {
				TableColumn<String, String> column = new TableColumn<>(columnName);
				column.setReorderable(false);
				column.setSortable(isTableMenuButtonVisible());
				this.getColumns().add(column);

				column.setCellValueFactory(cellDataFeatures -> {
					String values = cellDataFeatures.getValue();
					String[] cells = values.split(delimiter);
					int columnIndex = cellDataFeatures.getTableView().getColumns()
							.indexOf(cellDataFeatures.getTableColumn());
					if (columnIndex >= cells.length) {
						return new SimpleStringProperty("");
					} else {
						return new SimpleStringProperty(cells[columnIndex]);
					}
				});

				this.setItems(FXCollections.observableArrayList(lines));
				// Remove header row, as it will be added to the data at this point
				// this only works if we're sure that our CSV file has a header,
				// otherwise, we're just deleting data at this point.
				this.getItems().remove(0);
			}

			this.getColumns().forEach(e -> e.setReorderable(false));
		}
	}

	@FXML
	private TableView<String> table;
	private CSVData temp;

	@FXML
	private Button homeBtn;

	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1) {
//		table.setVisible(false);
	}

	public File arffConvert(File file) throws IOException {
		int offset = file.getName().indexOf(".arff");
		String filename = file.getName().substring(0, offset) + ".csv";
		File output = new File(filename);
		Instances data;

		ArffLoader arff = new ArffLoader();
		arff.setSource(file);
		data = arff.getDataSet();

		CSVSaver csv = new CSVSaver();
		csv.setInstances(data);
		csv.setFile(output);
		csv.writeBatch();
		return output;
	}

	@FXML
	public void chooseCSV(ActionEvent event) throws IOException {
		Window window = ((Node) (event.getSource())).getScene().getWindow();
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("."));
//		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ARFF Files", "*.arff"),
//				new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.arff", "*.csv"));
		f = fc.showOpenDialog(window);
		if (f == null)
			return;
		else {
			if (f.getName().endsWith(".arff"))
				f = arffConvert(f);
			temp = new CSVData(",", f);
			table.getColumns().setAll(temp.getColumns());
			table.setItems(temp.getItems());
			table.setVisible(true);

			// set selection mode to multi-cell-selection
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			table.getSelectionModel().setCellSelectionEnabled(true);
			table.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal.getTableColumn() != null) {
					table.getSelectionModel().selectRange(0, newVal.getTableColumn(), table.getItems().size(),
							newVal.getTableColumn());
				}
			});
		}

	}

	@FXML
	public void chartPlot(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/CT_Scene2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}
