package org.example.testkoltin

import javafx.fxml.FXML
import javafx.scene.control.Label

class HelloController {
    lateinit var uploaded: Label

    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "uploaded"
    }
}