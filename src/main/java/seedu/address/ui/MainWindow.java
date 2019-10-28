package seedu.address.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Lesson;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private StudentListPanel studentListPanel;
    private AssignmentListPanel assignmentListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private ReminderListPanel reminderListPanel;
    private ArrayList<Lesson> previousLessonsCopy;

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";
    public static final String ALERT_SOUND_PATH = "/alert.wav";


    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private SplitPane splitPane;

    @FXML
    private StackPane studentListPanelPlaceholder;

    @FXML
    private StackPane combinedListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane reminderListPanelPlaceholder;

    @FXML
    private StackPane assignmentListPanelPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        studentListPanel = new StudentListPanel(logic.getFilteredStudentList());
        //studentListPanelPlaceholder.getChildren().add(studentListPanel.getRoot());

        assignmentListPanel = new AssignmentListPanel(logic.getFilteredAssignmentList());
        //assignmentListPanelPlaceholder.getChildren().add(assignmentListPanel.getRoot());

        combinedListPanelPlaceholder.getChildren().add(studentListPanel.getRoot());

        reminderListPanel = new ReminderListPanel(logic.getFilteredLessonList());
        reminderListPanelPlaceholder.getChildren().add(reminderListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getClassroomFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public StudentListPanel getStudentListPanel() {
        return studentListPanel;
    }
    public ReminderListPanel getReminderListPanel() {
        return reminderListPanel;
    }

    public AssignmentListPanel getAssignmentListPanel() {
        return assignmentListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            previousLessonsCopy = new ArrayList<Lesson>(logic.getFilteredLessonList());
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            createAllReminders();

            if (logic.isDisplayStudents()) {
                combinedListPanelPlaceholder.getChildren().clear();
                combinedListPanelPlaceholder.getChildren().add(studentListPanel.getRoot());
            } else {
                combinedListPanelPlaceholder.getChildren().clear();
                combinedListPanelPlaceholder.getChildren().add(assignmentListPanel.getRoot());
            }


            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(getPrimaryStage(), type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, Alert.AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    /**
     * function to make all reminders
     */
    public void createAllReminders() {
        ObservableList<Lesson> allReminders = logic.getFilteredLessonList();
        for (Lesson lesson : allReminders) {
            if (!previousLessonsCopy.contains(lesson)) {
                Calendar lessonTime = lesson.getStartTime().getTime();
                long initialDelay = lessonTime.getTimeInMillis() - System.currentTimeMillis();
                createReminder(initialDelay, "Lesson", lesson.toString());
            }
        }
    }

    /**
     * function to make reminders
     * called by scheduler commands
     * @param duration
     * @param reminderType
     * @param reminderDetails
     */
    private void createReminder(long duration, String reminderType, String reminderDetails) {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(duration),
                ae -> countDownAlert(reminderType, reminderDetails)));
        timeline.play();
    }

    /**
     * alert for scheduler.
     * sets properties of alert then
     * plays sound file and shows alert dialog
     */
    private void countDownAlert(String reminderType, String reminderDetails) {

        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(getPrimaryStage());
        alert.setTitle("Reminder!");
        alert.setHeaderText(reminderType);
        alert.setContentText(reminderDetails);
        playSound();
        alert.show();

    }
    /**
     * handles playing alert audio for scheduled alert.
     * get .wav file from resource folder as input stream,
     * then open and play.
     */
    private void playSound() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(ALERT_SOUND_PATH);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            Clip sound = AudioSystem.getClip();
            sound.open(audioStream);
            sound.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }
}
