package org.example.testkoltin

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import org.example.OriginalData
import java.io.FileInputStream
import java.util.*


class MainApplication : Application() {
    private var depoNo="34"//To be changed later......
    private var scheduleNo="100"
    private var busType="TMP"
    private var tripNo = "1"
    private var departureTime = "06.20"
    private var stPlace = ""
    private var via = ""
    private var destination = ""
    private var arrivalTime = ""
    private var kilometer = ""
    private var etm = ""

    override fun start(primaryStage: Stage) {

        initializeFirebase()
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference(depoNo)



        val vbox = VBox()
            .apply {
            style = "-fx-background-color: #95B2B8;-fx-text-fill: white; -fx-font-weight: bold"
        }
        val separator = Separator()
        vbox.children.add(separator)
        vbox.padding = Insets(0.0, 0.0, 0.0, 20.0)

        // Add a spacer for vertical spacing
        val spacer = Region().apply {
            prefHeight = 20.0
        }
        val customTitleBar = HBox().apply {
            // Style the custom title bar
            style = "-fx-background-color: #336699;"

            // Create a label for the title and style it
            val titleLabel = Label("Desktop Application").apply {
                style = "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 0;"
            }

            // Add the title label to the custom title bar
            children.add(titleLabel)
            alignment = Pos.TOP_CENTER
        }

        // Add the custom title bar and the button to the VBox
        vbox.children.addAll(customTitleBar)
        vbox.children.add(spacer)
        addLabelAndTextField(vbox, "Trip No", tripNo, KeyboardType.NUMBER) { tripNo = it }
        addLabelAndTextField(vbox, "Start Time", departureTime, KeyboardType.NUMBER) { departureTime = it }
        addLabelAndTextField(vbox, "Start Place", stPlace) { stPlace = it }
        addLabelAndTextField(vbox, "Via", via) { via = it }
        addLabelAndTextField(vbox, "Destination", destination) { destination = it }
        addLabelAndTextField(vbox, "Arrival Time", arrivalTime, KeyboardType.NUMBER) { arrivalTime = it }
        addLabelAndTextField(vbox, "Kilometer", kilometer, KeyboardType.NUMBER) { kilometer = it }
        val optionalLabel = Label("Optional").apply {
            font = Font.font(18.0)
            textFill = Color.DARKGRAY

            style = "-fx-padding: 0 0 0 0;-fx-background-color: #336699;"
        }
        vbox.children.add(optionalLabel)
        // Add a smaller spacer for additional separation

        addLabelAndTextField(vbox, "ETM_root_No", etm, KeyboardType.NUMBER) { etm = it }
        val myButton = Button("UPLOAD")
        val smSpacer = Region().apply {
            prefHeight = 10.0
        }
        vbox.children.add(smSpacer)
        myButton.style = """
           -fx-background-color: red;
             -fx-text-fill: white;
             """.trimIndent()
        myButton.setMaxSize(90.00,70.00)
        myButton.padding = Insets(10.0, 0.0, 10.0, 0.0)
        myButton.setOnAction {
            val originalDatabase = OriginalData(
                startPlace = stPlace,
                via = via,
                destinationPlace = destination,
                departureTime = departureTime,
                arrivalTime = arrivalTime,
                kilometer = kilometer,
                bustype = busType,
                etmNo = etm
            )
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(depoNo)

                    myRef.child(busType).child(scheduleNo).child(tripNo)
            .setValue(originalDatabase) { error, _ ->
                if (error == null) {
                    // Operation successful
                    // Reset fields upon success
                    tripNo = ""
                    stPlace = ""
                    departureTime = ""
                    via = ""
                    destination = ""
                    arrivalTime = ""
                    kilometer = ""
                    etm = ""
                    // Provide user feedback, if needed
                } else {
                    // Handle failure
                   println("Failed to set value in Firebase: ${error.message}")
                    // Provide user feedback, if needed (e.g., show an alert dialog)
                }
            }



            println("Button clicked!")
          //  val alert = Alert(Alert.AlertType.INFORMATION)
           // alert.title = "Button Clicked"
           // alert.headerText = null
           // alert.contentText = "Verify upload later!"
            myButton.style = "-fx-background-color: green; -fx-text-fill: white;"
           // alert.showAndWait()
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Platform.runLater {
                        // Update the button style back to red and white
                        myButton.style = "-fx-background-color: red; -fx-text-fill: white;"

                        // Create a label to display the "Uploaded" message
                        val myLabel = Label("Uploaded")

                        // Add the label to the VBox
                        vbox.children.add(myLabel)

                        // Optional: You can add a FadeTransition here if you want to automatically fade out the label after a certain time
                        val fadeOut = FadeTransition(Duration.seconds(3.0), myLabel)
                        fadeOut.fromValue = 1.0
                        fadeOut.toValue = 0.0
                        fadeOut.cycleCount = 1
                        fadeOut.onFinished = EventHandler {
                            vbox.children.remove(myLabel)
                        }
                        fadeOut.play()
                   }
                }
            }, 1000)

        }
        val smallSpacer = Region().apply {
            prefHeight = 30.0
        }
        vbox.children.add(smallSpacer)

        vbox.children.add(myButton)
        val pacer = Region().apply {
            prefHeight = 50.0  // Adjust the height for the desired amount of space
        }
        vbox.children.add(pacer)
        // Create the scene and set it in the primary stage
        val scene = Scene(vbox, 290.0, 500.0) // Adjust size as needed
       // primaryStage.title = "Desktop Application"
        primaryStage.scene = scene
        primaryStage.isResizable = false
        primaryStage.show()
    }
    private fun initializeFirebase() {
        val serviceAccountPath = "C:/Users/abhi/Downloads/googleJsonKey.json"
        val databaseUrl = "https://livedata-b5ba1-default-rtdb.firebaseio.com"

        // Load the service account credentials
        val serviceAccount = FileInputStream(serviceAccountPath)

        // Build Firebase options with the credentials and database URL
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl(databaseUrl)
            .build()

        // Initialize the default Firebase app
        FirebaseApp.initializeApp(options)
    }
    // Helper function to add a label and a text field to the VBox
    private fun addLabelAndTextField(
        vbox: VBox,
        label: String,
        initialValue: String,
        keyboardType: KeyboardType = KeyboardType.DEFAULT,
        onValueChange: (String) -> Unit
    ) {
        val myLabel = Label(label)
        vbox.children.add(myLabel)

        val textField = TextField().apply {
            promptText = label
            text = initialValue
            prefWidth = 200.0  // Set the preferred width
            maxWidth = 200.0
            style = "-fx-text-fill: #D63604; -fx-pref-width: 175; -fx-pref-height: 51; -fx-padding: 0 0 0 10;"

            if (keyboardType == KeyboardType.NUMBER) {
                textProperty().addListener { _, _, newValue ->
                    onValueChange(newValue)
                }
            } else {
                textProperty().addListener { _, _, newValue ->
                    onValueChange(newValue)
                }
            }
        }

        vbox.children.add(textField)
    }

    enum class KeyboardType {
        DEFAULT, NUMBER
    }
}

// Entry point
fun main() {
    Application.launch(MainApplication::class.java)
    println("Execution over")
}

