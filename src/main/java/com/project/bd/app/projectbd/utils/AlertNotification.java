package com.project.bd.app.projectbd.utils;

import javafx.scene.control.Alert;

public class AlertNotification {
    public static void showError(String message) throws Exception {
        try{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public static void showSuccess(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
