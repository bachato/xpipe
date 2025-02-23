package io.xpipe.app.core.mode;

import io.xpipe.app.core.window.AppMainWindow;
import io.xpipe.app.issue.TrackEvent;
import io.xpipe.app.util.PlatformThread;

import javafx.stage.Stage;

public class GuiMode extends PlatformMode {

    @Override
    public String getId() {
        return "gui";
    }

    @Override
    public void onSwitchFrom() {
        PlatformThread.runLaterIfNeededBlocking(() -> {
            TrackEvent.info("Closing windows");
            Stage.getWindows().stream().toList().forEach(w -> {
                w.hide();
            });
        });
    }

    @Override
    public void onSwitchTo() throws Throwable {
        super.onSwitchTo();
        PlatformThread.runLaterIfNeededBlocking(() -> {
            AppMainWindow.getInstance().show();
        });
    }
}
