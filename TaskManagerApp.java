import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagerApp extends Application {
    private List<String> users = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    private VBox toDoBox = new VBox();
    private VBox inProgressBox = new VBox();
    private VBox doneBox = new VBox();
    private ListView<String> userListView = new ListView<>();

    private static final Map<String, String> STATUS_COLORS = new HashMap<>() {{
        put("To Do", "red");
        put("In Progress", "yellow");
        put("Done", "green");
    }};

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Manager");

        BorderPane mainPane = new BorderPane(); //głowna ramka
        HBox taskSections = new HBox(10); //konterner na taski 
        taskSections.setPadding(new Insets(10)); //
        mainPane.setCenter(taskSections);

        ScrollPane toDoScrollPane = createScrollPane("Do zrobienia", toDoBox);
        ScrollPane inProgressScrollPane = createScrollPane("W trakcie", inProgressBox);
        ScrollPane doneScrollPane = createScrollPane("Zrobione", doneBox);

        taskSections.getChildren().addAll(toDoScrollPane, inProgressScrollPane, doneScrollPane);

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));
        mainPane.setBottom(buttons);

        Button addTaskButton = new Button("Dodaj zadanie");
        addTaskButton.setOnAction(e -> addTask());

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.setOnAction(e -> addUser());

        Button reportButton = new Button("Wygeneruj raport");
        reportButton.setOnAction(e -> generateReport());

        Button saveButton = new Button("Zapisz zadania do formatu JSON");
        saveButton.setOnAction(e -> saveData());

        Button loadButton = new Button("Załaduj zadania z pliku tasks.Json");
        loadButton.setOnAction(e -> loadData());

        buttons.getChildren().addAll(addTaskButton, addUserButton, reportButton, saveButton, loadButton);

        VBox userSection = new VBox(10);
        userSection.setPadding(new Insets(10));
        userSection.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        userSection.getChildren().add(new Label("Członkowie zespołu"));
        userSection.getChildren().add(userListView);
        mainPane.setRight(userSection);

        loadUserData();
        loadData();

        primaryStage.setScene(new Scene(mainPane, 800, 600));
        primaryStage.show();
    }

    private ScrollPane createScrollPane(String title, VBox box) {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10));
        Label label = new Label(title);
        label.setStyle("-fx-background-color: " + STATUS_COLORS.get(title));
        section.getChildren().add(label);
        section.getChildren().add(box);
        
        ScrollPane scrollPane = new ScrollPane(section);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(250);
        scrollPane.setPadding(new Insets(10));

        return scrollPane;
    }

    private void addTask() {
        if (users.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Brak użytkownika w liście użytkowników", "Proszę dodać użytkownika przed dodaniem zadania");
            return;
        }

        Stage addTaskStage = new Stage();
        addTaskStage.setTitle("Dodaj Zadanie");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Nazwa Zadania");

        TextField taskDescriptionField = new TextField();
        taskDescriptionField.setPromptText("Opis Zadania");

        ComboBox<String> assignToComboBox = new ComboBox<>();
        assignToComboBox.getItems().addAll(users);

        Button saveButton = new Button("Dodaj Zadanie");
        saveButton.setOnAction(e -> {
            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();
            String assignedTo = assignToComboBox.getValue();

            if (taskName != null && taskDescription != null && assignedTo != null) {
                tasks.add(new Task(taskName, taskDescription, "To Do", assignedTo, LocalDateTime.now().toString(), null));
                updateTaskList();
                addTaskStage.close();
            } else {
                showAlert(Alert.AlertType.WARNING, "Błąd dodawania zadania", "Wszystkie pola muszą być wypełnione");
            }
        });

        vbox.getChildren().addAll(new Label("Nazwa Zadania"), taskNameField, new Label("Opis Zadania"), taskDescriptionField, new Label("Przypisz do"), assignToComboBox, saveButton);

        addTaskStage.setScene(new Scene(vbox, 300, 300));
        addTaskStage.show();
    }

    private void addUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dodawanie użytkownika");
        dialog.setHeaderText("Podaj nazwę nowego użytkownika");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty() && !users.contains(name.trim())) {
                users.add(name.trim());
                updateUserList();
            } else {
                showAlert(Alert.AlertType.WARNING, "Zduplikowany użytkownik", "Użytkownik z podaną nazwą już istnieje");
            }
        });
    }

    private void updateTaskList() {
        toDoBox.getChildren().clear();
        inProgressBox.getChildren().clear();
        doneBox.getChildren().clear();
        //iteracja rzez liste zadan 
        for (Task task : tasks) {
            VBox taskCard = new VBox(5);
            taskCard.setPadding(new Insets(10));
            String borderColor = STATUS_COLORS.get(task.status);
            taskCard.setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: 2px;");

            Label nameLabel = new Label("Zadanie: " + task.name);
            Label descriptionLabel = new Label("Opis Zadania: " + task.description);
            descriptionLabel.setWrapText(true);
            Label userLabel = new Label("Przypisane Do: " + task.user);
            Label statusLabel = new Label("Status: " + task.status);
            Label assignedAtLabel = new Label("Przypisane o: " + task.assignedAt);

            taskCard.getChildren().addAll(nameLabel, descriptionLabel, userLabel, statusLabel, assignedAtLabel);

            if (task.status.equals("To Do")) {
                toDoBox.getChildren().add(taskCard);
            } else if (task.status.equals("In Progress")) {
                inProgressBox.getChildren().add(taskCard);
            } else {
                doneBox.getChildren().add(taskCard);
            }

            taskCard.setOnMouseClicked(e -> showTaskMenu(e, taskCard, task));
        }
    }

    private void updateUserList() {
        userListView.getItems().clear();
        userListView.getItems().addAll(users);
    }

    private void showTaskMenu(MouseEvent event, VBox taskCard, Task task) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem toDoItem = new MenuItem("To Do");
        toDoItem.setOnAction(e -> changeTaskStatus(task, "To Do"));

        MenuItem inProgressItem = new MenuItem("In Progress");
        inProgressItem.setOnAction(e -> changeTaskStatus(task, "In Progress"));

        MenuItem doneItem = new MenuItem("Done");
        doneItem.setOnAction(e -> changeTaskStatus(task, "Done"));

        MenuItem editItem = new MenuItem("Edytuj zadanie");
        editItem.setOnAction(e -> editTask(task));

        contextMenu.getItems().addAll(toDoItem, inProgressItem, doneItem, editItem);
        contextMenu.show(taskCard, event.getScreenX(), event.getScreenY());
    }

    private void changeTaskStatus(Task task, String status) {
        if (status.equals("Done") && !task.status.equals("Done")) {
            task.completedAt = LocalDateTime.now().toString();
        }
        task.status = status;
        updateTaskList();
    }

    private void editTask(Task task) {
        Stage editTaskStage = new Stage();
        editTaskStage.setTitle("Modyfikacja zadania");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField taskNameField = new TextField(task.name);
        TextField taskDescriptionField = new TextField(task.description);

        ComboBox<String> assignToComboBox = new ComboBox<>();
        assignToComboBox.getItems().addAll(users);
        assignToComboBox.setValue(task.user);

        Button saveButton = new Button("Zapisz Zmiany");
        saveButton.setOnAction(e -> {
            task.name = taskNameField.getText();
            task.description = taskDescriptionField.getText();
            task.user = assignToComboBox.getValue();
            updateTaskList();
            editTaskStage.close();
        });

        vbox.getChildren().addAll(new Label("Zmień nazwę"), taskNameField, new Label("Zmień opis"), taskDescriptionField, new Label("Zmień przypisanie"), assignToComboBox, saveButton);

        editTaskStage.setScene(new Scene(vbox, 300, 300));
        editTaskStage.show();
    }

    private void generateReport() {
        Stage reportStage = new Stage();
        reportStage.setTitle("RAPORT ZADAŃ");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);

        Map<String, Integer> userTaskCount = new HashMap<>();
        Map<String, Long> userTaskTime = new HashMap<>();

        for (String user : users) {
            userTaskCount.put(user, 0);
            userTaskTime.put(user, 0L);
        }

        for (Task task : tasks) {
            userTaskCount.put(task.user, userTaskCount.get(task.user) + 1);

            if (task.status.equals("Done") && task.completedAt != null && task.assignedAt != null) {
                LocalDateTime startTime = LocalDateTime.parse(task.assignedAt);
                LocalDateTime endTime = LocalDateTime.parse(task.completedAt);
                long duration = java.time.Duration.between(startTime, endTime).getSeconds();
                userTaskTime.put(task.user, userTaskTime.get(task.user) + duration);
            }
        }

        reportArea.appendText("Ilość zadań przypisanych do usera:\n");
        for (Map.Entry<String, Integer> entry : userTaskCount.entrySet()) {
            reportArea.appendText(entry.getKey() + ": " + entry.getValue() + " tasks\n");
        }

        reportArea.appendText("\nCzas spędzony przez użytkownika na swoich zadaniach:\n");
        for (Map.Entry<String, Long> entry : userTaskTime.entrySet()) {
            long timeSpent = entry.getValue();
            long hours = timeSpent / 3600;
            long minutes = (timeSpent % 3600) / 60;
            long seconds = timeSpent % 60;
            reportArea.appendText(entry.getKey() + ": " + hours + "h " + minutes + "m " + seconds + "s\n");
        }

        vbox.getChildren().add(reportArea);

        reportStage.setScene(new Scene(vbox, 400, 400));
        reportStage.show();
    }

    private void saveData() {
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        data.put("tasks", tasks);

        try (Writer writer = new FileWriter("tasks.json")) {
            Gson gson = new Gson();
            gson.toJson(data, writer);
            showAlert(Alert.AlertType.INFORMATION, "Zapisz dane", "Użytkownicy oraz zadania zostały zapisane");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File("tasks.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Type taskListType = new TypeToken<List<Task>>() {}.getType();
                Type userListType = new TypeToken<List<String>>() {}.getType();
                Map<String, Object> data = gson.fromJson(reader, Map.class);

                users = gson.fromJson(gson.toJson(data.get("users")), userListType);
                tasks = gson.fromJson(gson.toJson(data.get("tasks")), taskListType);

                updateUserList();
                updateTaskList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Załaduj dane", "Nie znaleziono zapisanego pliku, lub zapisany został w innej nazwie niż tasks.json");
        }
    }

    private void loadUserData() {
        userListView.getItems().addAll(users);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class Task {
        String name;
        String description;
        String status;
        String user;
        String assignedAt;
        String completedAt;

        Task(String name, String description, String status, String user, String assignedAt, String completedAt) {
            this.name = name;
            this.description = description;
            this.status = status;
            this.user = user;
            this.assignedAt = assignedAt;
            this.completedAt = completedAt;
        }
    }
}
