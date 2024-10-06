package io.xpipe.ext.base.browser.compress;

import io.xpipe.app.browser.file.BrowserEntry;
import io.xpipe.app.browser.fs.OpenFileSystemModel;
import io.xpipe.app.browser.icon.BrowserIconFileType;
import io.xpipe.app.browser.icon.BrowserIcons;
import io.xpipe.app.core.AppI18n;
import io.xpipe.core.process.CommandBuilder;
import io.xpipe.core.process.OsType;
import io.xpipe.core.store.FileNames;

import io.xpipe.ext.base.browser.ExecuteApplicationAction;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

import java.util.List;

public abstract class BaseUnzipUnixAction extends ExecuteApplicationAction {

    private final boolean toDirectory;

    public BaseUnzipUnixAction(boolean toDirectory) {this.toDirectory = toDirectory;}

    @Override
    public Node getIcon(OpenFileSystemModel model, List<BrowserEntry> entries) {
        return BrowserIcons.createIcon(BrowserIconFileType.byId("zip")).createRegion();
    }

    @Override
    public String getExecutable() {
        return "unzip";
    }

    @Override
    protected boolean refresh() {
        return true;
    }

    @Override
    protected CommandBuilder createCommand(OpenFileSystemModel model, BrowserEntry entry) {
        var command = CommandBuilder.of().add("unzip", "-o").addFile(entry.getRawFileEntry().getPath());
        if (toDirectory) {
            command.add("-d").addFile(getTarget(entry.getRawFileEntry().getPath()));
        }
        return command;
    }

    @Override
    public Category getCategory() {
        return Category.CUSTOM;
    }

    @Override
    public ObservableValue<String> getName(OpenFileSystemModel model, List<BrowserEntry> entries) {
        var sep = model.getFileSystem().getShell().orElseThrow().getOsType().getFileSystemSeparator();
        var dir = entries.size() > 1 ? "[...]" : getTarget(entries.getFirst().getFileName()) + sep;
        return toDirectory ? AppI18n.observable("unzipDirectory", dir) : AppI18n.observable("unzipHere");
    }

    private String getTarget(String name) {
        return name.replaceAll("\\.zip$", "");
    }

    @Override
    public boolean isApplicable(OpenFileSystemModel model, List<BrowserEntry> entries) {
        return entries.stream().allMatch(entry -> entry.getRawFileEntry().getPath().endsWith(".zip"))
                && !model.getFileSystem().getShell().orElseThrow().getOsType().equals(OsType.WINDOWS);
    }
}
