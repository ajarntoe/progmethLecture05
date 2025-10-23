import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class FXHelloWorldWithHandlerTest extends ApplicationTest {

    private FXHelloWorldWithHandler app;
    Stage priStage;

    public void focus(Stage stage){
        Platform.runLater(() -> {
            stage.setAlwaysOnTop(true);
            stage.toFront();
            stage.requestFocus();
        });
    }


    @Override
    public void start(Stage stage) throws Exception {
        app = new FXHelloWorldWithHandler();
        app.start(stage);
        priStage = stage;
        focus(priStage);

    }

    @Test
    void should_initialize_button_with_correct_text() {
        // Test initial state
        focus(priStage);
        Button button = lookup("#helloButton").queryAs(Button.class);

        assertEquals("Hello world", button.getText());
    }

    @Test
    void should_increment_counter_when_button_clicked() {
        // Click the button once
        focus(priStage);
        clickOn("#helloButton");

        // Wait for JavaFX to process the event. Add this after any JavaFx autoclick command.
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the text changed
        Button button = lookup("#helloButton").queryAs(Button.class);
        assertEquals("Hello World 1", button.getText());
    }

    @Test
    void should_increment_counter_multiple_times() {
        // Click the button multiple times
        focus(priStage);
        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();

        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();

        // Verify the counter incremented correctly
        Button button = lookup("#helloButton").queryAs(Button.class);
        assertEquals("Hello World 3", button.getText());
    }

    @Test
    void should_handle_many_clicks() {
        // Test with many clicks
        focus(priStage);

        for (int i = 0; i < 10; i++) {
            clickOn("#helloButton");
            WaitForAsyncUtils.waitForFxEvents();

            //add small delay
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Button button = lookup("#helloButton").queryAs(Button.class);
        assertEquals("Hello World 10", button.getText());
    }

    @Test
    void should_verify_button_properties() {
        focus(priStage);
        Button button = lookup("#helloButton").queryAs(Button.class);

        // Test button properties
        assertNotNull(button);
        assertTrue(button.isVisible());
        assertFalse(button.isDisabled());
        assertEquals("helloButton", button.getId());
    }

    @Test
    void should_handle_clicks_with_verification_between() {
        focus(priStage);

        // Test with verification after each click
        Button button = lookup("#helloButton").queryAs(Button.class);

        // First click
        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Hello World 1", button.getText());

        // Second click
        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Hello World 2", button.getText());

        // Third click
        clickOn("#helloButton");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Hello World 3", button.getText());
    }
}
