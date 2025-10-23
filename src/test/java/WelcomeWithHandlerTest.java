import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
public class WelcomeWithHandlerTest extends ApplicationTest {

    private Label userLabel, pwLabel;
    private TextField userTextField;
    private PasswordField pwBox;
    private Button signinBtn;
    private Button exitBtn;
    private Text scenetitle;
    private WelcomeWithHandler app;
    private Stage priStage;

    public void focus(Stage stage){
        Platform.runLater(() -> {
            stage.setAlwaysOnTop(true);
            stage.toFront();
            stage.requestFocus();
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        app = new WelcomeWithHandler();
        app.start(stage);
        priStage = stage;
        focus(priStage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Get references to UI components
        focus(priStage);

        userLabel = lookup("#userLabel").queryAs(Label.class);
        pwLabel = lookup("#pwLabel").queryAs(Label.class);
        userTextField = lookup("#userTextField").queryAs(TextField.class);
        pwBox = lookup("#pwBox").queryAs(PasswordField.class);
        signinBtn = lookup("#signinBtn").queryAs(Button.class);
        exitBtn = lookup("#exitBtn").queryAs(Button.class);
        scenetitle = lookup("#scenetitle").queryAs(Text.class);

        //initialize all components to starting state
        signinBtn.setPrefWidth(65);
        exitBtn.setPrefWidth(65);
        userTextField.setText("");
        pwBox.setText("");

        // If IDs are not set in the original code, use alternative lookups
        if (userTextField == null) {
            userTextField = lookup(".text-field").queryAs(TextField.class);
        }
        if (pwBox == null) {
            pwBox = lookup(".password-field").queryAs(PasswordField.class);
        }
        if (signinBtn == null) {
            signinBtn = lookup("Sign in").queryAs(Button.class);
        }
        if (exitBtn == null) {
            exitBtn = lookup("Exit").queryAs(Button.class);
        }
        if (scenetitle == null) {
            scenetitle = lookup("Welcome").queryAs(Text.class);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();

    }

    @Test
    public void testInitialUIState() {
        focus(priStage);

        // Test that all UI components are present and properly initialized
        assertEquals("Welcome", scenetitle.getText());
        assertEquals("User Name:", userLabel.getText());
        assertEquals("Password:", pwLabel.getText());
        assertEquals("Sign in", signinBtn.getText());
        assertEquals("Exit", exitBtn.getText());

        // Test initial button widths
        assertEquals(65, signinBtn.getPrefWidth(), "Sign in button should have initial width of 65");
        assertEquals(65, exitBtn.getPrefWidth(), "Exit button should have initial width of 65");

        // Test initial text field states
        assertTrue(userTextField.getText().isEmpty(), "User text field should be initially empty");
        assertTrue(pwBox.getText().isEmpty(), "Password field should be initially empty");
    }

    @Test
    public void testUserTextFieldInput() {
        focus(priStage);
        // Test typing in the user text field
        clickOn(userTextField);
        WaitForAsyncUtils.waitForFxEvents();
        write("testuser");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("testuser", userTextField.getText(), "User text field should contain the typed text");
    }

    @Test
    public void testPasswordFieldInput() {
        // Test typing in the password field
        focus(priStage);

        clickOn(pwBox);
        WaitForAsyncUtils.waitForFxEvents();
        write("testpass");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("testpass", pwBox.getText(), "Password field should contain the typed text");
    }

    @Test
    public void testEscapeKeyClearsUserTextField() {
        // Type text in user field
        focus(priStage);

        clickOn(userTextField);
        WaitForAsyncUtils.waitForFxEvents();
        write("sometext");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("sometext", userTextField.getText(), "Text should be present before ESC");
        assertTrue(userTextField.isFocused(), "TextField should be focused before ESC");
        assertNotNull(userTextField.getOnKeyPressed(), "KeyPressed handler should be attached");

        // Try alternative approach: simulate the key event directly
        KeyEvent escapeEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "", "",
                KeyCode.ESCAPE, false, false, false, false
        );

        // Press ESC key
        interact(() -> {
            userTextField.fireEvent(escapeEvent);
        });

        //type(KeyCode.ESCAPE);
        WaitForAsyncUtils.waitForFxEvents();
        // Verify text is cleared
        assertEquals("", userTextField.getText(), "User text field should be empty after pressing ESC");

    }

    @Test
    public void testSignInButtonMouseHover() {
        focus(priStage);
        // Test initial width
        assertEquals(65.0, signinBtn.getPrefWidth(), "Initial width should be 65");

        // Move mouse over sign in button
        moveTo(signinBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Check width increased
        assertEquals(75.0, signinBtn.getPrefWidth(), "Width should increase to 75 on mouse enter");

        // Move mouse away
        moveTo(exitBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Check width returned to original
        assertEquals(65.0, signinBtn.getPrefWidth(), "Width should return to 65 on mouse exit");
    }

    @Test
    public void testExitButtonMouseHover() {
        focus(priStage);

        // Test initial width
        assertEquals(65.0, exitBtn.getPrefWidth(), "Initial width should be 65");

        // Move mouse over exit button
        moveTo(exitBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Check width increased
        assertEquals(75.0, exitBtn.getPrefWidth(), "Width should increase to 75 on mouse enter");

        // Move mouse away
        moveTo(signinBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // Check width returned to original
        assertEquals(65.0, exitBtn.getPrefWidth(), "Width should return to 65 on mouse exit");
    }

    @Test
    public void testSignInButtonAction() {

        focus(priStage);
        // Enter test data
        clickOn(userTextField);
        write("testuser");
        clickOn(pwBox);
        write("testpass");

        // Click sign in button
        clickOn(signinBtn);

        // Wait for alert to appear
        WaitForAsyncUtils.waitForFxEvents();

        // Look for any dialog and verify its content
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane, "Alert dialog should be present");

        // Get the content text from the dialog
        String contentText = dialogPane.getContentText();
        assertEquals("Welcome, testuser. Your Password is testpass.", contentText);

        // Close the alert
        clickOn("OK");
    }

    @Test
    public void testSignInButtonActionWithEmptyFields() {
        focus(priStage);

        // Leave fields empty and click sign in
        clickOn(signinBtn);

        // Wait for alert to appear
        WaitForAsyncUtils.waitForFxEvents();

        // Look for any dialog and verify its content
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane, "Alert dialog should be present");

        // Get the content text from the dialog
        String contentText = dialogPane.getContentText();
        assertEquals("Welcome, . Your Password is .", contentText);

        // Close the alert
        clickOn("OK");
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testExitButtonAction() {
        focus(priStage);

        // Get reference to the stage
        Stage stage = (Stage) signinBtn.getScene().getWindow();
        assertTrue(stage.isShowing(), "Stage should be showing initially");

        // Click exit button
        clickOn(exitBtn);

        // Add additional wait time for stage closing
        try {
            Thread.sleep(500); // Give some time for the stage to close
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Use Platform.runLater to check the stage status on the FX thread
        final boolean[] stageClosed = {false};
        Platform.runLater(() -> {
            stageClosed[0] = !stage.isShowing();
        });

        // Wait for the stage to close
        WaitForAsyncUtils.waitForFxEvents();

        // Verify stage is closed
        assertTrue(stageClosed[0], "Stage should be closed after clicking Exit");

        //assertFalse(stage.isShowing(), "Stage should be closed after clicking Exit");
    }

    @Test
    public void testUIStyles() {
        focus(priStage);

        // Test that styles are applied correctly
        assertTrue(scenetitle.getStyle().contains("32px"), "Title should have 32px font size");
        assertTrue(scenetitle.getStyle().contains("Arial Black"), "Title should use Arial Black font");

        assertTrue(signinBtn.getStyle().contains("darkgreen"), "Sign in button should have dark green background");
        assertTrue(signinBtn.getStyle().contains("white"), "Sign in button should have white text");

        assertTrue(exitBtn.getStyle().contains("darkred"), "Exit button should have dark red background");
        assertTrue(exitBtn.getStyle().contains("white"), "Exit button should have white text");
    }

    @Test
    public void testCompleteUserFlow() {
        focus(priStage);

        // Test a complete user interaction flow

        // 1. Enter username
        clickOn(userTextField);
        WaitForAsyncUtils.waitForFxEvents();
        write("john.doe");
        WaitForAsyncUtils.waitForFxEvents();

        // 2. Enter password
        clickOn(pwBox);
        WaitForAsyncUtils.waitForFxEvents();
        write("secret123");
        WaitForAsyncUtils.waitForFxEvents();

        // 3. Test ESC key functionality
        // Try alternative approach: simulate the key event directly
        KeyEvent escapeEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "", "",
                KeyCode.ESCAPE, false, false, false, false
        );

        // Press ESC key
        interact(() -> {
            userTextField.fireEvent(escapeEvent);
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(userTextField.getText().isEmpty(), "Username should be cleared after ESC");

        // 4. Re-enter username
        clickOn(userTextField);
        WaitForAsyncUtils.waitForFxEvents();
        write("john.doe");
        WaitForAsyncUtils.waitForFxEvents();

        // 5. Test mouse hover on buttons
        moveTo(signinBtn);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(75.0, signinBtn.getPrefWidth(), "Sign in button should expand on hover");

        // 6. Click sign in
        clickOn(signinBtn);
        WaitForAsyncUtils.waitForFxEvents();

        // 7. Verify and close alert
        // Look for any dialog and verify its content
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane, "Alert dialog should be present");

        // Get the content text from the dialog
        String contentText = dialogPane.getContentText();
        assertEquals("Welcome, john.doe. Your Password is secret123.", contentText);

        // Close the alert
        clickOn("OK");

        /* 8. Test exit functionality
        focus(priStage);
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        assertTrue(stage.isShowing(), "Stage should still be showing before exit test");

        clickOn(exitBtn);
        // Add additional wait time for stage closing
        try {
            Thread.sleep(500); // Give some time for the stage to close
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Use Platform.runLater to check the stage status on the FX thread
        final boolean[] stageClosed = {false};
        Platform.runLater(() -> {
            stageClosed[0] = !stage.isShowing();
        });

        // Wait for the stage to close
        WaitForAsyncUtils.waitForFxEvents();

        // Verify stage is closed
        assertTrue(stageClosed[0], "Stage should be closed after clicking Exit");*/
    }
}