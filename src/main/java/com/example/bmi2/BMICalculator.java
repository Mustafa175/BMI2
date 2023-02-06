package com.example.bmi2;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BMICalculator extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("BMI Calculator");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);
        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 2);

        Label ageLabel = new Label("Age:");
        grid.add(ageLabel, 0, 3);
        TextField ageTextField = new TextField();
        grid.add(ageTextField, 1, 3);

        Label genderLabel = new Label("Gender:");
        grid.add(genderLabel, 0, 4);
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        grid.add(genderComboBox, 1, 4);

        Label weightLabel = new Label("Weight (kg):");
        grid.add(weightLabel, 0, 5);
        TextField weightTextField = new TextField();
        grid.add(weightTextField, 1, 5);

        Label heightLabel = new Label("Height (m):");
        grid.add(heightLabel, 0, 6);
        TextField heightTextField = new TextField();
        grid.add(heightTextField, 1, 6);
        Button calculateBMIButton = new Button("Calculate BMI");
        grid.add(calculateBMIButton, 1, 7);

        Label bmiLabel = new Label();
        grid.add(bmiLabel, 1, 8);

        Button viewAverageBMIButton = new Button("View Average BMI");
        grid.add(viewAverageBMIButton, 1, 9);

        Label averageBmiLabel = new Label();
        grid.add(averageBmiLabel, 1, 10);

        calculateBMIButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double weight = Double.parseDouble(weightTextField.getText());
                double height = Double.parseDouble(heightTextField.getText());
                double bmi = weight / (height * height);
                bmiLabel.setText("BMI: " + bmi);

                // Store the measurement data in the database
                try (Connection conn = DriverManager.getConnection("jdbc:sqlserver:// EASV-DB4:1433;databaseName=BMI2;userName=CSe2022t_t_4;password=CSe2022tT4#;encrypt=true;trustServerCertificate=true")) {
                    PreparedStatement insertPerson = conn.prepareStatement(
                            "INSERT INTO Person (Name, LastName, Age, Gender) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    insertPerson.setString(1, nameTextField.getText());
                    insertPerson.setString(2, lastNameTextField.getText());
                    insertPerson.setInt(3, Integer.parseInt(ageTextField.getText()));
                    insertPerson.setString(4, genderComboBox.getValue());
                    insertPerson.executeUpdate();

                    ResultSet generatedKeys = insertPerson.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int personId = generatedKeys.getInt(1);
                        PreparedStatement insertMeasurement = conn.prepareStatement(
                                "INSERT INTO Measurement (PersonId, BMI, Date) VALUES (?, ?, ?)");
                        insertMeasurement.setInt(1, personId);
                        insertMeasurement.setDouble(2, bmi);
                        insertMeasurement.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        insertMeasurement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        viewAverageBMIButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try (Connection conn = DriverManager.getConnection("jdbc:sqlserver:// EASV-DB4:1433;databaseName=BMI2;userName=CSe2022t_t_4;password=CSe2022tT4#;encrypt=true;trustServerCertificate=true")) {


                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT AVG(BMI) FROM Measurement");
                    if (resultSet.next()) {
                        double averageBmi = resultSet.getDouble(1);
                        averageBmiLabel.setText("Average BMI: " + averageBmi);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        });
    }


}